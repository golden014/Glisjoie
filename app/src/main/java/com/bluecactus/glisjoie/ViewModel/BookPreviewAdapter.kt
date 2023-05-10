package com.bluecactus.glisjoie.ViewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.BookPreviewModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.View.HomeActivity
import com.bluecactus.glisjoie.View.SearchResultsFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class BookPreviewAdapter(
    private val resource: Int,
    private var objects: List<BookPreviewModel>) : RecyclerView.Adapter<BookPreviewAdapter.ViewHolder>() {

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var textViewTitle: TextView? = itemView.findViewById(R.id.textViewTitle)
        internal var textViewAuthor: TextView? = itemView.findViewById(R.id.textViewAuthor)
        internal var backgroundImageView: ImageView = itemView.findViewById(R.id.backgroundImageView)
    }

//    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var convertView = convertView
//
//        val bookPreview = getItem(position)
//
//        val viewHolder: ViewHolder
//
//        if (convertView == null) {
//            viewHolder = ViewHolder()
//            val inflater = LayoutInflater.from(context)
//            convertView = inflater.inflate(R.layout.list_item_book_preview, parent, false)
//            viewHolder.textViewTitle = convertView.findViewById<TextView>(R.id.textViewTitle)
//            viewHolder.textViewAuthor = convertView.findViewById<TextView>(R.id.textViewAuthor)
//            convertView.tag = viewHolder
//        } else {
//            viewHolder = convertView.tag as ViewHolder
//        }
//
//        viewHolder.textViewTitle?.text = bookPreview?.title
//        viewHolder.textViewAuthor?.text = bookPreview?.authorName
//
//        return convertView!!
//    }
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
//
        val bookPreview = objects[position]
//        Glide.with(context)
//            .load("https://firebasestorage.googleapis.com/v0/b/glisjoie.appspot.com/o/images%2F76d76bef-1c1a-41c4-890e-14b1269ee985.jpg?alt=media&token=b102ce93-bae7-4b32-9e34-05f35874b01b")
//            .centerCrop()
//            .into(holder.backgroundImageView)

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

        //https://firebasestorage.googleapis.com/v0/b/glisjoie.appspot.com/o/images%2F203fb714-ecfb-484e-baec-f08f101292e1.jpg?alt=media&token=9d75e2e9-764f-4ebc-b58a-3d6cfe9be40b

        holder.textViewTitle?.text = bookPreview.title
        holder.textViewAuthor?.text = bookPreview.authorName

    }

    override fun getItemCount(): Int {
//
        return objects.size
    }
}
