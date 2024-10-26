package com.app.mvvmdemo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.MyApplication
import com.app.mvvmdemo.R
import com.app.mvvmdemo.adapters.HomeAdapter
import com.app.mvvmdemo.databinding.FragmentMovieBinding
import com.app.mvvmdemo.helper.myToast
import com.app.mvvmdemo.models.Result
import com.app.mvvmdemo.repositorys.DataViewModel
import com.app.mvvmdemo.repositorys.DataViewModelFactory
import com.app.mvvmdemo.repositorys.Response


class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var viewModel: DataViewModel
    private var list: ArrayList<Result> = ArrayList()
    private lateinit var homeAdapter: HomeAdapter
    private val refreshData = MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        binding = FragmentMovieBinding.bind(view)
        viewModel = ViewModelProvider(
            requireActivity(),
            DataViewModelFactory(repo = (requireActivity().application as MyApplication).repo)
        )[DataViewModel::class.java]
        setRcv()
        loadData()
        refreshData.observe(requireActivity()) {
            binding.swipeToRefresh.isRefreshing = it
        }
        binding.swipeToRefresh.setOnRefreshListener {
            list.clear()
            viewModel.page = 0
            viewModel.loadData()
        }

        return view

    }

    private fun setRcv() {
        homeAdapter = HomeAdapter(requireContext(), list)
        binding.rcv.adapter = homeAdapter

        binding.rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (list.isNotEmpty()) {
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.loadData()
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {

        viewModel.loadData()

        viewModel.mainData.observe(requireActivity()) {

            when (it) {
                is Response.Error -> {
                    refreshData.value = false
                    requireContext().myToast("connection time out")
                }

                is Response.Loading -> {
                    refreshData.value = true
                }

                is Response.Success -> {
                    refreshData.value = false
                    it.data?.let { it1 ->
                        it1.results?.let { it2 ->
                            list.addAll(it2)
                            homeAdapter.notifyDataSetChanged()
                        }
                    }

                }

            }
        }
    }


}