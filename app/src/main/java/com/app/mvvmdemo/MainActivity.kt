package com.app.mvvmdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.mvvmdemo.adapters.ViewPagerAdapter
import com.app.mvvmdemo.apidata.ApiService
import com.app.mvvmdemo.databinding.ActivityMainBinding
import com.app.mvvmdemo.fragments.MovieFragment
import com.app.mvvmdemo.fragments.PeopleFragment
import com.app.mvvmdemo.helper.MyHelpPreference
import com.app.mvvmdemo.helper.myToast
import com.app.mvvmdemo.models.ResponseDataModel
import com.app.mvvmdemo.repositorys.DataViewModel
import com.app.mvvmdemo.repositorys.DataViewModelFactory
import com.app.mvvmdemo.repositorys.Response
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fragmentList = listOf(MovieFragment(), PeopleFragment())

    private val backPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackPress()
        }
    }
    private val queryList: ArrayList<String> = ArrayList()

    private val viewModel by viewModels<DataViewModel> {
        DataViewModelFactory((application as MyApplication).repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyHelpPreference.init(this)
        binding.viewPager.isUserInputEnabled = false
        onBackPressedDispatcher.addCallback(this, backPress)

        binding.viewPager.adapter = ViewPagerAdapter(
            listFragment = fragmentList,
            lifecycle = lifecycle,
            fm = supportFragmentManager
        )
        binding.viewPager.offscreenPageLimit = 2
        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.movieId -> {
                    binding.viewPager.currentItem = 0
                    true
                }

                R.id.peopleID -> {
                    binding.viewPager.currentItem = 1
                    true
                }

                else -> false
            }

        }
        setSearchView()
        getSearchQuery("")
    }

    private fun setSearchAdapter() {
        binding.listView.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, queryList)

        binding.listView.setOnItemClickListener { _, _, p2, _ ->
            searchMovie(queryList[p2])
        }
    }

    private fun setSearchView() {

        binding.searchView.editText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchMovie(binding.searchView.text.toString())
                    true
                }

                else -> false
            }

        }
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.setting) {
                startActivity(Intent(this, SettingActivity::class.java))
            }
            true
        }
    }

    private fun searchMovie(text: String) {
        binding.searchProgressBar.visibility = View.VISIBLE
        ApiService.service.getSearchMovie(
            movieName = text,
            isAdult = MyHelpPreference.isAdult,
            page = 1
        ).enqueue(object : Callback<ResponseDataModel> {
            override fun onResponse(
                p0: Call<ResponseDataModel>,
                p1: retrofit2.Response<ResponseDataModel>
            ) {
                if (p1.isSuccessful) {
                    binding.searchProgressBar.visibility = View.GONE
                    binding.searchView.hide()
                    val list: ArrayList<ResponseDataModel> = ArrayList()
                    list.add(p1.body()!!)
                    openNextActivity(list, text)
                }
            }

            override fun onFailure(p0: Call<ResponseDataModel>, p1: Throwable) {
                binding.searchProgressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, p1.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openNextActivity(dataList: ArrayList<ResponseDataModel>, text: String) {
        if (dataList[0].results!!.isEmpty()) {
            myToast("No Result Found")
            return
        }
        val intent = Intent(this, SearchMovieActivity::class.java).apply {
            putParcelableArrayListExtra("list", dataList)
            putExtra("page", dataList[0].total_pages)
            putExtra("name", text)
        }
        startActivity(intent)
    }

    private fun getSearchQuery(query: String) {
        viewModel.getQueryResult(query)
        viewModel.mainDataQuery.observe(this) {
            when (it) {
                is Response.Error -> {

                }

                is Response.Loading -> {

                }

                is Response.Success -> {
                    queryList.clear()
                    it.data?.let { apiResponse ->
                        val movieNames = apiResponse.results.map { result ->
                            val document = Jsoup.parse(result)
                            document.select("[data-search-name]").attr("data-search-name")
                        }
                        queryList.addAll(movieNames)
                    }
                    setSearchAdapter()

                }
            }
        }
    }

    private fun handleBackPress() {
        if (binding.searchView.isShowing) {
            binding.searchView.hide()
            return
        }
        finish()
    }
}