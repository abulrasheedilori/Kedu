package com.brainstem.mvvmapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brainstem.mvvmapp.R
import com.brainstem.mvvmapp.model.Post
import com.brainstem.mvvmapp.views.DetailItemPage

class HomeRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder>() {

    var adapterList = ArrayList<Post>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val body: TextView = itemView.findViewById(R.id.body)
        val userId : TextView = itemView.findViewById(R.id.name)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_post_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.userId.text = adapterList[position].id.toString()
        holder.title.text = adapterList[position].title
        holder.body.text = adapterList[position].body

        //passing info to detail page using intent
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailItemPage::class.java)
            intent.putExtra("id", adapterList[position].id.toString())
            intent.putExtra("userId", adapterList[position].userId.toString())
            intent.putExtra("title", adapterList[position].title)
            intent.putExtra("body", adapterList[position].body)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }
    //@SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Post>?){
        if (newList != null) {
            adapterList = newList
        }
        notifyDataSetChanged()
    }
}