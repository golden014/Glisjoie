package edu.bluejack22_2.Glisjoie.ViewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.glisjoie.R
import edu.bluejack22_2.Glisjoie.Model.CommentModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(private val id : Int, private var comments: List<CommentModel>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var profileImage: CircleImageView? = itemView.findViewById(R.id.profileImage)
        internal var commentRating: RatingBar? = itemView.findViewById(R.id.commentRating)
        internal var commentContent: TextView = itemView.findViewById(R.id.commentContent)
    }

    fun updateData(comments: List<CommentModel>) {
        clearData()
        this.comments = comments
        notifyDataSetChanged()
    }

    private fun clearData() {
        this.comments = emptyList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(id, parent, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        Picasso.get()
            .load(comment.userID)
            .placeholder(R.drawable.logoglisjoie)
            .error(R.drawable.logoglisjoie)
            .into(holder.profileImage, object : Callback {
                override fun onSuccess() {
                    Log.d("Picasso", "Image loaded successfully")
                }

                override fun onError(e: Exception) {
                    Log.e("Picasso", "Error loading image", e)
                }
            })
        holder.commentRating?.rating = comment.rating!!;
        holder.commentRating?.setIsIndicator(true)
        holder.commentContent?.text = comment.description;
    }

    override fun getItemCount(): Int {
        return comments.size;
    }
}
