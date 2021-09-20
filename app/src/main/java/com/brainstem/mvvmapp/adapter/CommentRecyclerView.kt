package com.brainstem.mvvmapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brainstem.mvvmapp.R
import com.brainstem.mvvmapp.model.Comment

class CommentRecyclerView : RecyclerView.Adapter<CommentRecyclerView.ViewHolder>(){
    var commentAdapterList = ArrayList<Comment>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var nameView: TextView = itemView.findViewById(R.id.recycler_comment_name)
        var commentView: TextView = itemView.findViewById(R.id.recycler_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_recycler_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = commentAdapterList[position].name
        holder.commentView.text = commentAdapterList[position].body
    }

    override fun getItemCount(): Int {
        return commentAdapterList.size
    }

    fun passDataToMainView(newData: ArrayList<Comment>){
        commentAdapterList = newData
        notifyDataSetChanged()
    }
}