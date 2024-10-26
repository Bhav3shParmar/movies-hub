package com.app.mvvmdemo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.DetailsPageActivity
import com.app.mvvmdemo.databinding.HomeItemViewBinding
import com.app.mvvmdemo.databinding.PeopleDetailsItemViewBinding
import com.app.mvvmdemo.models.KnownFor
import com.bumptech.glide.Glide

class PeopleDetailAdapter(
    private val context: Context,
    private val dataList: ArrayList<KnownFor>
) :
    RecyclerView.Adapter<PeopleDetailAdapter.MyViewHolder>() {
    inner class MyViewHolder(private var inflate: PeopleDetailsItemViewBinding) :
        RecyclerView.ViewHolder(inflate.root) {
        fun bind(knownFor:  KnownFor, context: Context) {
            Glide.with(context).load("https://image.tmdb.org/t/p/original${knownFor.poster_path}").into(inflate.img)
            itemView.setOnClickListener {
                val intent = Intent(context, DetailsPageActivity::class.java)
                intent.putExtra("id",knownFor.id)
                intent.putExtra("title",knownFor.title)
                intent.putExtra("img",knownFor.backdrop_path)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PeopleDetailsItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }
}