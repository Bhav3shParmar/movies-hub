package com.app.mvvmdemo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.MyApplication
import com.app.mvvmdemo.R
import com.app.mvvmdemo.adapters.PeopleListAdapter
import com.app.mvvmdemo.databinding.FragmentPeopleBinding
import com.app.mvvmdemo.helper.myToast
import com.app.mvvmdemo.models.Result
import com.app.mvvmdemo.repositorys.DataViewModel
import com.app.mvvmdemo.repositorys.DataViewModelFactory
import com.app.mvvmdemo.repositorys.Response


class PeopleFragment : Fragment() {

    private lateinit var binding: FragmentPeopleBinding
    private lateinit var viewModel: DataViewModel
    private lateinit var adapter: PeopleListAdapter
    private var list: ArrayList<Result> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_people, container, false)
        binding = FragmentPeopleBinding.bind(view)

        adapter = PeopleListAdapter(requireContext(), list)
        binding.rcvPeople.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            DataViewModelFactory(repo = (requireActivity().application as MyApplication).repo)
        )[DataViewModel::class.java]

        viewModel.getPeopleList()

        viewModel.mainData3.observe(requireActivity()) { data ->
            when (data) {
                is Response.Error -> {
                    binding.pbar2.visibility = View.GONE
                    requireContext().myToast("Error getting People List")
                }

                is Response.Loading -> {
                    binding.pbar2.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    binding.pbar2.visibility = View.GONE
                    data.data?.forEach {
                        it.results?.let { it1 ->
                            list.addAll(it1)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        binding.rcvPeople.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (list.isNotEmpty()) {
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.getPeopleList()
                    }
                }
            }
        })

        return view
    }
}