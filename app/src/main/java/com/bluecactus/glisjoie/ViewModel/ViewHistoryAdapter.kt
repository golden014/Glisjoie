package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.ViewHistoryModel
import com.bluecactus.glisjoie.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.lang.Exception

class ViewHistoryAdapter(
    private  val resource: Int,
    private var objects: List<ViewHistoryModel>
): RecyclerView.Adapter<ViewHistoryAdapter.ViewHolder>() {

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var bookImage: ImageView = itemView.findViewById(R.id.book_image_history)
        internal var bookTitle: TextView = itemView.findViewById(R.id.title_view_history)
        internal var authorName: TextView = itemView.findViewById(R.id.author_view_history)
        internal var date: TextView = itemView.findViewById(R.id.date_view_history)
        internal var deleteButton: Button = itemView.findViewById(R.id.single_delete_history_button)
    }

    fun updateData(newViewHistoryModels: List<ViewHistoryModel>) {
        clearData()
    }

    fun clearData() {
        this.objects = emptyList()
//        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        return ViewHistoryAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookHistory = objects[position]
        Picasso.get()
            .load(bookHistory.cover)
            .placeholder(R.drawable.search_icon)
            .error(R.drawable.auth_background)
            .into(holder.bookImage, object : Callback {
                override fun onSuccess() {
                    Log.d("loadImageHistory", "Success")
                }

                override fun onError(e: Exception?) {
                    Log.d("loadImageHistory", "Fail loading image")
                }
            })

        holder.bookTitle.text = bookHistory.title
        holder.authorName.text = bookHistory.authorName
        holder.date.text = bookHistory.date

        holder.deleteButton.setOnClickListener {
            // TODO: tambahin soft delete per item
        }

    }

    override fun getItemCount(): Int {
        return objects.size
    }
}