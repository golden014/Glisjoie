package com.bluecactus.glisjoie.ViewModel

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.View.HomeActivity
import com.bluecactus.glisjoie.View.SearchResultsFragment
import com.bluecactus.glisjoie.View.books.BookDetailActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class BookPreviewAdapter(
    private val resource: Int,
    private var objects: List<BookPreviewModel>) : RecyclerView.Adapter<BookPreviewAdapter.ViewHolder>() {

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var textViewTitle: TextView? = itemView.findViewById(R.id.textViewTitle)
        internal var textViewAuthor: TextView? = itemView.findViewById(R.id.textViewAuthor)
        internal var backgroundImageView: ImageView = itemView.findViewById(R.id.backgroundImageView)
        internal var itemLayout: RelativeLayout = itemView.findViewById(R.id.preview_card)
    }
    fun updateData(newBookPreviewModels: List<BookPreviewModel>) {
        this.objects = newBookPreviewModels
        notifyDataSetChanged()
    }

    fun clearData() {
        this.objects = emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookPreview = objects[position]
        Picasso.get()
            .load(bookPreview.cover)
            .placeholder(R.drawable.search_icon)
            .error(R.drawable.auth_background)
//            .into(holder.backgroundImageView)
            .into(holder.backgroundImageView, object : Callback {
                override fun onSuccess() {
                    Log.d("Picasso", "Image loaded successfully")
                }

                override fun onError(e: Exception) {
                    Log.e("Picasso", "Error loading image", e)
                }
            })


        holder.textViewTitle?.text = bookPreview.title
        holder.textViewAuthor?.text = bookPreview.authorName
        val context = holder.itemView.context

        holder.itemLayout.setOnClickListener {
            Log.e("bookDetail", "clicked $context")
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra("bookID", bookPreview.bookID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
//
        return objects.size
    }
}
