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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.apidata.ApiService
import com.app.mvvmdemo.databinding.ActivityPeopleDetailsBinding
import com.app.mvvmdemo.databinding.PeopleDetailsItemViewBinding
import com.app.mvvmdemo.models.CastX
import com.app.mvvmdemo.models.PeopleDetailsResponse
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

class PeopleDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeopleDetailsBinding
    private var id = 0
    private lateinit var viewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPeopleDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            DataViewModelFactory((application as MyApplication).repo)
        )[DataViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.people_details)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        intent?.let {
            binding.toolPeopleDetails.title = it.getStringExtra("name")
            id = it.getIntExtra("id", 0)
        }

        getDetails(id)

        binding.rcvMovies.setItemViewCacheSize(20)
        binding.rcvMovies.setHasFixedSize(true)

        binding.toolPeopleDetails.setNavigationOnClickListener {
            finish()
        }

    }

    private fun getDetails(id: Int) {
        viewModel.getPersonDetail(id = id)
        viewModel.mainData4.observe(this) {
            when (it) {
                is Response.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.errorMsg, Toast.LENGTH_SHORT).show()
                }

                is Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setData(it.data!!)
                }
            }
        }
    }

    private fun setData(data: PeopleDetailsResponse) {
        data.profile_path?.let {
            Glide.with(this).load("https://image.tmdb.org/t/p/original${it}")
                .into(binding.profileImg)
        }
        binding.tvOverview.text = (if (data.biography.isNullOrEmpty()) "-" else data.biography)
        binding.tvDepartment.text = data.known_for_department ?: "-"
        binding.tvBirthDate.text = setBirthday(data.birthday)
        binding.tvBirthPlace.text = data.place_of_birth ?: "-"

        CoroutineScope(Dispatchers.IO).launch {
            getMovieAndTvCredit(data.id!!)
        }
    }

    private suspend fun getMovieAndTvCredit(id: Int) {
        val response = ApiService.service.getCombineCredits(id)
        if (response.isSuccessful) {
            response.body()?.let {
              val adapter = PersonMovieAdapter(it.cast!!)
                withContext(Dispatchers.Main) {
                    binding.loading.visibility = View.GONE
                    binding.rcvMovies.adapter = adapter
                }

            }

        } else {
            withContext(Dispatchers.Main) {
                binding.loading.visibility = View.GONE
                Toast.makeText(
                    this@PeopleDetailsActivity,
                    "Unable to get Movie list",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setBirthday(birthday: String?): String {
        if (birthday.isNullOrEmpty()) {
            return "-"
        }
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return outputFormat.format(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                birthday
            )!!
        )
    }

    inner class PersonMovieAdapter(private val cast: List<CastX>) :
        RecyclerView.Adapter<PersonMovieAdapter.ViewHolder>() {
        inner class ViewHolder(private val homeItemView: PeopleDetailsItemViewBinding) :
            RecyclerView.ViewHolder(homeItemView.root) {
            fun bind(castX: CastX) {
                if (!castX.poster_path.isNullOrEmpty()) {
                    Glide.with(this@PeopleDetailsActivity)
                        .load("https://image.tmdb.org/t/p/original${castX.poster_path}")
                        .into(homeItemView.img)
                }
                itemView.setOnClickListener {
                    when {
                        castX.media_type.equals("movie") -> {
                            val intent =
                                Intent(this@PeopleDetailsActivity, DetailsPageActivity::class.java)
                            intent.putExtra("id", castX.id)
                            intent.putExtra("title", castX.title)
                            intent.putExtra("img", castX.backdrop_path)
                            startActivity(intent)
                        }

                        castX.media_type.equals("tv") -> {
                            val intent = Intent(
                                this@PeopleDetailsActivity,
                                TVDetailsPageActivity::class.java
                            )
                            intent.putExtra("id", castX.id)
                            intent.putExtra("title", castX.name)
                            intent.putExtra("img", castX.backdrop_path)
                            startActivity(intent)
                        }

                        else -> {
                            return@setOnClickListener
                        }
                    }
                    finish()
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): PersonMovieAdapter.ViewHolder {
            return ViewHolder(
                PeopleDetailsItemViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: PersonMovieAdapter.ViewHolder, position: Int) {
            holder.bind(cast[position])
        }

        override fun getItemCount(): Int {
            return cast.size
        }
    }
}