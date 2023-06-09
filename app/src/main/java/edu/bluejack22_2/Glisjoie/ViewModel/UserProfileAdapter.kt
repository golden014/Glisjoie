package edu.bluejack22_2.Glisjoie.ViewModel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.Glisjoie.Model.UserModel
import edu.bluejack22_2.glisjoie.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class UserProfileAdapter(
    private  val resource: Int,
    private var objects: List<UserModel>,
    private val app: AppCompatActivity,
): RecyclerView.Adapter<UserProfileAdapter.ViewHolder>() {

    public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var profilePic: ImageView = itemView.findViewById(R.id.user_image_admin_page)
        internal var email: TextView = itemView.findViewById(R.id.email_user_list)
        internal var username: TextView = itemView.findViewById(R.id.username_user_list)
        internal var actionButton: Button = itemView.findViewById(R.id.ban_unban_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        return UserProfileAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val user = objects[position]

        Picasso.get()
            .load(user.profilePictureURL)
            .placeholder(R.drawable.logoglisjoie)
            .error(R.drawable.logoglisjoie)
            .into(holder.profilePic, object : Callback {
                override fun onSuccess() {
                    Log.d("loadImageHistory", "Success")
                }

                override fun onError(e: Exception?) {
                    Log.d("loadImageHistory", "Fail loading image")
                }
            })

        holder.username.text = user.username
        holder.email.text = user.email

        val status = user.status

        if (status == "Banned") {
            holder.actionButton.text = "Unban"
        } else if (status == "Active") {
            holder.actionButton.text = "Ban"
        }

        val userViewModel = ViewModelProvider(app).get(UserViewModel::class.java)


        holder.actionButton.setOnClickListener {
            if (status == "Banned") {
                //unban
                userViewModel.actionBanUser(user.userDocumentID, "Active") {
                    if (it == 200) {
                        reloadData()
                    }
                }
            } else if (status == "Active") {
                //ban
                userViewModel.actionBanUser(user.userDocumentID, "Banned") {
                    if (it == 200) {
                        reloadData()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    fun updateData(newViewHistoryModels: List<UserModel>) {
        clearData()
        this.objects = newViewHistoryModels
        notifyDataSetChanged()
    }

    fun clearData() {
        this.objects = emptyList()
        notifyDataSetChanged()
    }

    fun reloadData() {
        val adminViewModel = ViewModelProvider(app).get(AdminViewModel::class.java)

        adminViewModel.getAllCustomer {
            updateData(it.toList())
        }
    }

}