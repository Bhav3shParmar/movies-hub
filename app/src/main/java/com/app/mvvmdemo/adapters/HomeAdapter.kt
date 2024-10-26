package com.app.mvvmdemo.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmdemo.DetailsPageActivity
import com.app.mvvmdemo.PeopleDetailsActivity
import com.app.mvvmdemo.R
import com.app.mvvmdemo.TVDetailsPageActivity
import com.app.mvvmdemo.databinding.HomeItemViewBinding
import com.app.mvvmdemo.models.Result
import com.bumptech.glide.Glide

class HomeAdapter(
    private val context: Context,
    private val dataList: ArrayList<Result>
) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    inner class MyViewHolder(private var inflate: HomeItemViewBinding) :
        RecyclerView.ViewHolder(inflate.root) {
        fun bind(result: Result, context: Context) {
            if (result.poster_path.isNullOrEmpty()) {
                if (result.profile_path.isNullOrEmpty()) {
                    inflate.img.setImageResource(R.drawable.place_holder)
                    inflate.img.scaleType = ImageView.ScaleType.CENTER_INSIDE
                }
                Glide.with(context)
                    .load("https://image.tmdb.org/t/p/original${result.profile_path}")
                    .into(inflate.img)
            } else {
                Glide.with(context).load("https://image.tmdb.org/t/p/original${result.poster_path}")
                    .into(inflate.img)
            }
            itemView.setOnClickListener {
                Log.e("GGG", result.id.toString())
                when {
                    result.media_type.equals("movie") -> {
                        val intent = Intent(context, DetailsPageActivity::class.java)
                        intent.putExtra("id", result.id)
                        intent.putExtra("title", result.title)
                        intent.putExtra("img", result.backdrop_path)
                        context.startActivity(intent)
                    }
                    result.media_type.equals("tv") -> {
                        val intent = Intent(context, TVDetailsPageActivity::class.java)
                        intent.putExtra("id", result.id)
                        intent.putExtra("title", result.name)
                        intent.putExtra("img", result.backdrop_path)
                        context.startActivity(intent)
                    }

                    result.media_type.equals("person") -> {
                        val intent = Intent(context, PeopleDetailsActivity::class.java)
                        intent.putExtra("id", result.id)
                        intent.putExtra("name", result.name)
                        context.startActivity(intent)
                    }

                    else -> {
                        return@setOnClickListener
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            HomeItemViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }
}