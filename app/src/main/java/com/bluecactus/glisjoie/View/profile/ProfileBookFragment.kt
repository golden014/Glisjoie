package com.bluecactus.glisjoie.View.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    lateinit var homeViewModel: HomeViewModel;
    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_profile_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel=  ViewModelProvider(requireActivity()).get(HomeViewModel::class.java);
        userViewModel=  ViewModelProvider(requireActivity()).get(UserViewModel::class.java);

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = BookPreviewAdapter(R.layout.list_item_book_preview, mutableListOf())
        recyclerView.adapter = adapter

        userViewModel.getCurrUser {
            BookRepository().getPreviewModelByUser(it.userDocumentID){arr ->
                adapter.updateData(arr.toList());
            };
        }
    }


}