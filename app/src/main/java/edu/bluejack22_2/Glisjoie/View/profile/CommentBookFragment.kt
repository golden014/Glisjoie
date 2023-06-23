package edu.bluejack22_2.Glisjoie.View.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.Glisjoie.Repository.CommentRepository
import edu.bluejack22_2.Glisjoie.ViewModel.CommentAdapter
import edu.bluejack22_2.Glisjoie.ViewModel.HomeViewModel
import edu.bluejack22_2.Glisjoie.ViewModel.UserViewModel
import edu.bluejack22_2.glisjoie.R

class CommentBookFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CommentAdapter
    lateinit var homeViewModel: HomeViewModel;
    lateinit var userViewModel: UserViewModel
    lateinit var emptyComment: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_comment_book, container, false)
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getCurrUser {
            CommentRepository().getCommentByUserID(it.userDocumentID){arr ->
                adapter.updateData(arr.toList());
                if(arr.size == 0){
                    emptyComment.visibility = View.VISIBLE
                }else{
                    emptyComment.visibility = View.INVISIBLE
                }
            };
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        homeViewModel=  ViewModelProvider(requireActivity()).get(HomeViewModel::class.java);
        userViewModel=  ViewModelProvider(requireActivity()).get(UserViewModel::class.java);

        emptyComment = view.findViewById(R.id.emptyComment)
        recyclerView = view.findViewById(R.id.commentRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = CommentAdapter(R.layout.item_comment, mutableListOf())
        recyclerView.adapter = adapter

        emptyComment.visibility = View.INVISIBLE

        userViewModel.getCurrUser {
            CommentRepository().getCommentByUserID(it.userDocumentID){ arr ->
                adapter.updateData(arr.toList());
                if(arr.size == 0){
                    emptyComment.visibility = View.VISIBLE
                }else{
                    emptyComment.visibility = View.INVISIBLE
                }
            };
        }
    }
}