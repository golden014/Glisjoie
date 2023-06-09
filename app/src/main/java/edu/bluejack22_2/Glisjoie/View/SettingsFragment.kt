package edu.bluejack22_2.Glisjoie.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack22_2.Glisjoie.ViewModel.UserViewModel
import edu.bluejack22_2.glisjoie.R

class SettingsFragment : Fragment() {

    lateinit var userViewModel: UserViewModel
    lateinit var adminSettingsLayout: RelativeLayout
    lateinit var credentialSettingsLayout: RelativeLayout
    lateinit var logoutButton: RelativeLayout
    lateinit var viewHistoryLayout: RelativeLayout

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
        viewHistoryLayout = view.findViewById(R.id.view_history_setting_button)
        logoutButton = view.findViewById(R.id.logout_setting)

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        userViewModel.getCurrUser {
            if (it.role == "Admin") {
                adminSettingsLayout.visibility = View.VISIBLE
            }
        }

        adminSettingsLayout.setOnClickListener{
            val intent = Intent(requireActivity(), AdminPanelActivity::class.java)
            startActivity(intent)
        }

        credentialSettingsLayout.setOnClickListener{
            val intent = Intent(requireActivity(), CredentialSettingsActivity::class.java)
            startActivity(intent)
        }

        viewHistoryLayout.setOnClickListener{
            val intent = Intent(requireActivity(), ViewHistoryActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            this.activity?.finishAffinity()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }




    }


}