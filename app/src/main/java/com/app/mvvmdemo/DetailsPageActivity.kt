package com.app.mvvmdemo

import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.apidata.ApiService
import com.app.mvvmdemo.databinding.ActivityDetailsPageBinding
import com.app.mvvmdemo.databinding.ProductionItemViewBinding
import com.app.mvvmdemo.models.Cast
import com.app.mvvmdemo.models.MovieDetailsResponse
import com.app.mvvmdemo.repositorys.DataViewModel
import com.app.mvvmdemo.repositorys.DataViewModelFactory
import com.app.mvvmdemo.repositorys.Response
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.floor


class DetailsPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsPageBinding
    private lateinit var viewModel: DataViewModel
    private var mainID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsPageBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            DataViewModelFactory(repo = (application as MyApplication).repo)
        )[DataViewModel::class.java]

        binding.apply {
            if (!intent.extras?.isEmpty!!) {
                toolTitle.title = intent.extras!!.getString("title")
                intent.extras!!.getString("img")?.let {
                    if (it.isNotEmpty()) {
                        imgPoster.visibility = View.VISIBLE
                        Glide.with(this@DetailsPageActivity)
                            .load("https://image.tmdb.org/t/p/original${it}")
                            .into(imgPoster)
                    }
                }
                mainID = intent.extras!!.getInt("id", 0)
                getData(mainID)
            }

            toolTitle.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun getData(id: Int) {
        viewModel.getMovieDetail(id)

        viewModel.mainData2.observe(this) {
            when (it) {
                is Response.Error -> {
                    binding.pBar.visibility = View.GONE
                    Toast.makeText(this@DetailsPageActivity, it.errorMsg, Toast.LENGTH_LONG).show()
                }

                is Response.Loading -> {
                    binding.pBar.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    binding.pBar.visibility = View.GONE
                    binding.ratingBar.visibility = View.VISIBLE
                    setData(it.data!!)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: MovieDetailsResponse) {
        binding.tvRating.text = floor(data.vote_average ?: 0.0).toString()
        binding.tvVotes.text = "${data.vote_count ?: 0} vote"
        binding.tvOverview.text = (if (data.overview.isNullOrEmpty()) "-" else data.overview)
        val genreBuilder = StringBuilder()
        for (i in 0 until (data.genres?.size ?: 0)) {
            if (i > 0) {
                genreBuilder.append(", ")
            }
            genreBuilder.append(data.genres?.get(i)?.name ?: "-")
        }
        binding.tvGenereType.text = (if (genreBuilder.isEmpty()) "-" else genreBuilder.toString())
        getCertification(mainID)
        CoroutineScope(Dispatchers.IO).launch {
            getMovieCast(data.id!!)
        }

    }

    private fun getCertification(mainID: Int) {
        viewModel.getCertification(mainID)
        viewModel.mainData5.observe(this) { data ->
            when (data) {
                is Response.Error -> {

                }

                is Response.Loading -> {

                }

                is Response.Success -> {
                    val result = data.data?.results
                    val certification = result?.find { it.iso_3166_1 == "IN" }
                    certification?.let {
                        binding.llCertification.visibility = View.VISIBLE
                        if (it.release_dates[0].certification.isNotEmpty()) {
                            binding.tvCertification.visibility = View.VISIBLE
                            binding.tvCertification.text = it.release_dates[0].certification
                        }
                        it.release_dates[0].release_date.let { it2 ->
                            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it2).let { date ->
                                    binding.tvReleaseDate.text = date
                                }
                        }
                    }

                }
            }
        }
    }

    private suspend fun getMovieCast(id: Int) {
        val response = ApiService.service.getMovieCast(id)
        if (response.isSuccessful) {
            response.body()?.let {
                withContext(Dispatchers.Main) {
                    binding.loading.visibility = View.GONE
                    binding.rcvProduction.adapter = ProductionAdapter(it.cast!!)
                }
            }

        } else {
            withContext(Dispatchers.Main) {
                binding.loading.visibility = View.GONE
                Toast.makeText(
                    this@DetailsPageActivity,
                    "Unable to get Movie Cast",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    inner class ProductionAdapter(private val cast: List<Cast?>) :
        RecyclerView.Adapter<ProductionAdapter.ViewHolder>() {
        inner class ViewHolder(private val vBinding: ProductionItemViewBinding) :
            RecyclerView.ViewHolder(vBinding.root) {
            fun bind(cast: Cast) {
                vBinding.tvName.text = cast.name
                vBinding.tvCountry.text = cast.character
                if (cast.profile_path.isNullOrEmpty()) {
                    Glide.with(this@DetailsPageActivity).load(R.drawable.place_holder)
                        .into(vBinding.imgLogo)
                } else {
                    Glide.with(this@DetailsPageActivity)
                        .load("https://image.tmdb.org/t/p/original${cast.profile_path}")
                        .into(vBinding.imgLogo)
                }
                itemView.setOnClickListener {
                    val intent = Intent(this@DetailsPageActivity, PeopleDetailsActivity::class.java)
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
                    LayoutInflater.from(this@DetailsPageActivity),
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