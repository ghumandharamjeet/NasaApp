package com.app.nasasearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.nasasearch.Datum
import com.app.nasasearch.SearchedItems
import com.app.nasasearch.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_item_search.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(){

    inner class SearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallBack = object: DiffUtil.ItemCallback<SearchedItems>(){

        override fun areItemsTheSame(oldItem: SearchedItems, newItem: SearchedItems): Boolean {
            return oldItem.data[0].nasaID == newItem.data[0].nasaID
        }

        override fun areContentsTheSame(oldItem: SearchedItems, newItem: SearchedItems): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer<SearchedItems>(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {

        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_item_search,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        val item = (differ.currentList[position])
        holder.itemView.apply {
           Glide.with(this).load(item.links[0].href).into(iv_nasa_image)
            tv_title.text = item.data[0].title
            setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((SearchedItems) -> Unit)? = null

    fun setOnItemClickListener(listener: (SearchedItems) -> Unit) {
        onItemClickListener = listener
    }
}