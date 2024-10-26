package com.app.mvvmdemo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.apidata.ApiService
import com.app.mvvmdemo.databinding.ActivityTvdetailsPageBinding
import com.app.mvvmdemo.databinding.ProductionItemViewBinding
import com.app.mvvmdemo.helper.myToast
import com.app.mvvmdemo.models.Cast
import com.app.mvvmdemo.models.TvDetailsDataModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TVDetailsPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTvdetailsPageBinding
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTvdetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tv_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        intent?.also {
            binding.toolTitle.title = it.getStringExtra("title")
            id = it.getIntExtra("id", 0)
            val poster = it.getStringExtra("img")
            if (!poster.isNullOrEmpty()) {
                binding.imgPoster.visibility = View.VISIBLE
                Glide.with(this@TVDetailsPageActivity)
                    .load("https://image.tmdb.org/t/p/original${poster}").into(binding.imgPoster)
            }
        }

        lifecycleScope.launch {
            getDetails(id)
        }

        binding.toolTitle.setNavigationOnClickListener {
            finish()
        }

    }

    private suspend fun getDetails(id: Int) {
        val response = ApiService.service.getTVDetails(id)
        if (response.isSuccessful) {
            binding.tvProgressBar.visibility = View.GONE
            setData(response.body()!!)
        } else {
            binding.tvProgressBar.visibility = View.GONE
            myToast("Unable to connect server")
        }
    }

    private fun setData(tvDetailsDataModel: TvDetailsDataModel?) {
        binding.apply {
            tvOverview.text = tvDetailsDataModel?.overview ?: "-"
            val genreBuilder = StringBuilder()
            for (i in 0 until (tvDetailsDataModel?.genres?.size ?: 0)) {
                if (i > 0) {
                    genreBuilder.append(", ")
                }
                genreBuilder.append(tvDetailsDataModel?.genres?.get(i)?.name ?: "-")
            }
            binding.tvGenereType.text =
                (if (genreBuilder.isEmpty()) "-" else genreBuilder.toString())
            CoroutineScope(Dispatchers.IO).launch {
                getCastDetails(tvDetailsDataModel?.id!!)
            }

        }
    }

    private suspend fun getCastDetails(id: Int) {
        val response = ApiService.service.getTvCast(id)
        if (response.isSuccessful) {
            response.body()?.let {
                withContext(Dispatchers.Main) {
                    binding.loading.visibility = View.GONE
                    binding.rcvProduction.adapter = CastAndCrewAdapter(it.cast!!)
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                binding.loading.visibility = View.GONE
                myToast("Unable to get series cast")
            }
        }
    }

    inner class CastAndCrewAdapter(private val cast: List<Cast?>) :
        RecyclerView.Adapter<CastAndCrewAdapter.ViewHolder>() {
        inner class ViewHolder(private val vBinding: ProductionItemViewBinding) :
            RecyclerView.ViewHolder(vBinding.root) {
            fun bind(cast: Cast) {
                vBinding.tvName.text = cast.name
                vBinding.tvCountry.text = cast.character
                if (cast.profile_path.isNullOrEmpty()) {
                    Glide.with(this@TVDetailsPageActivity).load(R.drawable.place_holder)
                        .into(vBinding.imgLogo)
                } else {
                    Glide.with(this@TVDetailsPageActivity)
                        .load("https://image.tmdb.org/t/p/original${cast.profile_path}")
                        .into(vBinding.imgLogo)
                }
                itemView.setOnClickListener {
                    val intent =
                        Intent(this@TVDetailsPageActivity, PeopleDetailsActivity::class.java)
                    intent.putExtra("id", cast.id)
                    intent.putExtra("name", cast.name)
                    startActivity(intent)
                    finish()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ProductionItemViewBinding.inflate(
                    LayoutInflater.from(this@TVDetailsPageActivity),
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return cast.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(cast[position]!!)
        }

    }
}