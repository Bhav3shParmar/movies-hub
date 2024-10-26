package com.app.mvvmdemo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.DetailsPageActivity
import com.app.mvvmdemo.PeopleDetailsActivity
import com.app.mvvmdemo.models.Result
import com.app.mvvmdemo.databinding.HomeItemViewBinding
import com.app.mvvmdemo.databinding.PeopleItemListBinding
import com.bumptech.glide.Glide

class PeopleListAdapter(
    private val context: Context,
    private val dataList: ArrayList<Result>
) :
    RecyclerView.Adapter<PeopleListAdapter.MyViewHolder>() {
    inner class MyViewHolder(private var inflate: PeopleItemListBinding) :
        RecyclerView.ViewHolder(inflate.root) {
        fun bind(result: Result, context: Context) {
            inflate.name.text = result.name
            Glide.with(context).load("https://image.tmdb.org/t/p/original${result.profile_path}").circleCrop().into(inflate.imgPeople)
            itemView.setOnClickListener {
                val intent = Intent(context, PeopleDetailsActivity::class.java)
                intent.putExtra("id",result.id)
                intent.putExtra("name",result.name)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PeopleItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }
}