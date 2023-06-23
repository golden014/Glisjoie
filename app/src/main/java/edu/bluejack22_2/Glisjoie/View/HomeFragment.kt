package edu.bluejack22_2.Glisjoie.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.Glisjoie.Model.BookPreviewModel
import edu.bluejack22_2.Glisjoie.ViewModel.BookPreviewAdapter
import edu.bluejack22_2.Glisjoie.ViewModel.HomeViewModel
import edu.bluejack22_2.glisjoie.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var listView: ListView
    var names = arrayOf("josua", "jevon", "aaaa")
    lateinit var arrayAdapter: ArrayAdapter<String>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: BookPreviewAdapter
    lateinit var firstData: Array<BookPreviewModel>
    lateinit var message: TextView
    var emptyData: Boolean = false
    lateinit var tes: TextView;
    lateinit var homeViewModel: HomeViewModel;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel=  ViewModelProvider(requireActivity()).get(HomeViewModel::class.java);
        message = view.findViewById<TextView>(R.id.noBooksMessage)

//        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = BookPreviewAdapter(R.layout.list_item_book_preview, mutableListOf()) // Create adapter instance here
        recyclerView.adapter = adapter

        homeViewModel.currBooks.observe(viewLifecycleOwner) { bookPreviewModels ->
//            emptyData = false
//            firstData = bookPreviewModels
//            adapter = BookPreviewAdapter(R.layout.list_item_book_preview, bookPreviewModels.toList())
//            recyclerView.adapter = adapter
//            adapter.notifyDataSetChanged()
            adapter.updateData(bookPreviewModels.toList())
//            Log.e("fragment2", bookPreviewModels[0].title)
            Log.e("fragment1", "currBook changed")
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}