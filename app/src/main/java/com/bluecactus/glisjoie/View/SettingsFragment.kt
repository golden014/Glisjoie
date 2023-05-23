package com.bluecactus.glisjoie.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.bluecactus.glisjoie.R
import com.bluecactus.glisjoie.ViewModel.UserViewModel

class SettingsFragment : Fragment() {

    lateinit var userViewModel: UserViewModel
    lateinit var adminSettingsLayout: RelativeLayout
    lateinit var credentialSettingsLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminSettingsLayout = view.findViewById(R.id.admin_setting_layout)
        credentialSettingsLayout = view.findViewById(R.id.credential_setting_layout)

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        userViewModel.getCurrUser {
            if (it.role != "Admin") {
                adminSettingsLayout.visibility = View.GONE
            }
        }

        adminSettingsLayout.setOnClickListener{
            //TODO:direct ke admin setting
        }

        credentialSettingsLayout.setOnClickListener{
            //TODO: direct ke credential setting
        }


    }


}