package com.app.mvvmdemo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.adapters.HomeAdapter
import com.app.mvvmdemo.apidata.ApiService
import com.app.mvvmdemo.databinding.ActivitySearchMovieBinding
import com.app.mvvmdemo.helper.MyHelpPreference
import com.app.mvvmdemo.models.ResponseDataModel
import com.app.mvvmdemo.models.Result
import retrofit2.Call
import retrofit2.Callback

class SearchMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchMovieBinding
    private var page = 1
    private var defaultPage = 1
    private var name: String? = null
    private val list: ArrayList<Result> = ArrayList()
    private lateinit var adapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_movie)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        MyHelpPreference.init(this)
        binding.tool.setNavigationOnClickListener {
            finish()
        }

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("list", ResponseDataModel::class.java)
        } else {
            intent.getParcelableArrayListExtra("list")
        }
        page = intent.getIntExtra("page", 1)
        name = intent.getStringExtra("name")
        binding.tool.title = name

        data?.forEach {
            it.results.let { it1 -> list.addAll(it1!!) }
        }
        adapter = HomeAdapter(this@SearchMovieActivity, list)
        binding.rcvData.adapter = adapter

        binding.rcvData.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!recyclerView.canScrollVertically(1) && defaultPage <= page && !name.isNullOrEmpty()) {
                    defaultPage++
                    getApiCall()
                }
            }
        })
    }

    private fun getApiCall() {
        ApiService.service.getSearchMovie(
            movieName = name!!,
            isAdult = MyHelpPreference.isAdult,
            page = defaultPage
        ).enqueue(object :
            Callback<ResponseDataModel> {
            override fun onResponse(
                p0: Call<ResponseDataModel>,
                p1: retrofit2.Response<ResponseDataModel>
            ) {
                if (p1.isSuccessful) {
                    val dataAdd: ArrayList<ResponseDataModel> = ArrayList()
                    dataAdd.add(p1.body()!!)
                    addData(dataAdd)
                }
            }

            override fun onFailure(p0: Call<ResponseDataModel>, p1: Throwable) {
                Toast.makeText(this@SearchMovieActivity, p1.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(data: ArrayList<ResponseDataModel>?) {
        data!![0].results.let {
            list.addAll(it!!)
            adapter.notifyDataSetChanged()
        }

    }
}