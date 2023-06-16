package com.bluecactus.glisjoie.View.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.Repository.BookRepository
import com.bluecactus.glisjoie.ViewModel.BookPreviewAdapter
import com.bluecactus.glisjoie.ViewModel.HomeViewModel
import com.bluecactus.glisjoie.ViewModel.UserViewModel

class ProfileBookFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: BookPreviewAdapter
    lateinit var homeViewModel: HomeViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var emptyBook: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_profile_book, container, false)
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getCurrUser {
            BookRepository().getPreviewModelByUser(it.userDocumentID){arr ->
                adapter.updateData(arr.toList());
                if(arr.size == 0){
                    emptyBook.visibility = View.VISIBLE
                }else{
                    emptyBook.visibility = View.INVISIBLE
                }
            };
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel=  ViewModelProvider(requireActivity()).get(HomeViewModel::class.java);
        userViewModel=  ViewModelProvider(requireActivity()).get(UserViewModel::class.java);

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = BookPreviewAdapter(R.layout.list_item_book_preview, mutableListOf())
        recyclerView.adapter = adapter

        emptyBook = view.findViewById(R.id.emptyBook)

        emptyBook.visibility = View.VISIBLE

        userViewModel.getCurrUser {
            BookRepository().getPreviewModelByUser(it.userDocumentID){arr ->
                adapter.updateData(arr.toList());
                if(arr.size == 0){
                    emptyBook.visibility = View.INVISIBLE
                }else{
                    emptyBook.visibility = View.VISIBLE
                }
            };
        }
    }


}