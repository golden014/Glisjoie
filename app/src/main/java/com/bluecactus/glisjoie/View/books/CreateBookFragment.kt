package com.bluecactus.glisjoie.View.books

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.Model.BookModel
import com.bluecactus.glisjoie.Model.UserModel
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.Repository.CommentRepository
import com.bluecactus.glisjoie.View.HomeActivity
import com.bluecactus.glisjoie.ViewModel.BookViewModel
import com.bluecactus.glisjoie.ViewModel.ImageSelectionViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateBookFragment : Fragment() {

    private lateinit var viewModel : ImageSelectionViewModel
    private lateinit var bookViewModel : BookViewModel
    private lateinit var selectImageBtn : Button
    private lateinit var addBookBtn : Button
    private lateinit var imageView : ImageView
    private lateinit var titleTF : EditText
    private lateinit var descriptionTF : EditText



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        //Membuat instance viewmodel
        viewModel=  ViewModelProvider(requireActivity(), ImageSelectionViewModel.Factory(this as CreateBookFragment)).get(ImageSelectionViewModel::class.java);
        bookViewModel = ViewModelProvider(requireActivity())[BookViewModel::class.java]
        //Ngambil component di xml
        imageView = view.findViewById<ImageView>(R.id.CoverImage)
        titleTF = view.findViewById<EditText>(R.id.BookTitleTF)
        descriptionTF = view.findViewById<EditText>(R.id.DescriptionTF)

        addBookBtn = view.findViewById<Button>(R.id.AddBookBtn)
        selectImageBtn = view.findViewById<Button>(R.id.selectImageBtn)

        //Inisialiasi gambar awal kucing no image
        imageView.setImageResource(R.drawable.image)

        //Observer jika image data berubah
        viewModel.imageData.observe(viewLifecycleOwner) {
            viewModel.setView(imageView)
        }

        //[SELECT IMAGE BTN] Buka view untuk melakukan select image
        selectImageBtn.setOnClickListener {
            viewModel.selectImage()
        }

        //[ADD BOOK BTN] Buat ngeupload book
        addBookBtn.setOnClickListener{
            bookViewModel.createBook(viewModel.imageData.value?.imageUri, titleTF.text.toString(), descriptionTF.text.toString())
        }

        //[ ALERT ] observe response buat alert klo misal product berhasil/gagal
        bookViewModel.response.observe(viewLifecycleOwner) { message ->
            if(message != ""){
                AlertDialog.Builder(this.requireContext())
                    .setMessage(message)
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                        Log.d("A", "onViewCreated:TROLOLO ")
                        if (activity != null) {
                            val intent = Intent(activity, HomeActivity::class.java)
                            // Optionally you can add flags to clear the activity stack
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            activity?.finish()
                        }
                    })
                    .create()
                    .show()




            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                CreateBookFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}