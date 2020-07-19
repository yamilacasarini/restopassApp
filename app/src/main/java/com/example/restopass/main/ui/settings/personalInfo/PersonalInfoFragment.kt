package com.example.restopass.main.ui.settings.personalInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.MainActivity
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_communications.*
import kotlinx.android.synthetic.main.fragment_personal_info.*
import kotlinx.coroutines.*
import timber.log.Timber

class PersonalInfoFragment : Fragment() {

    private lateinit var viewModel: PersonalInfoViewModel

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(PersonalInfoViewModel::class.java)

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.personal_info)
            visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        personalInfoLoader.visibility = View.VISIBLE
        personalInfoSection.visibility = View.GONE
        coroutineScope.launch {
            try {
                viewModel.get()

                fillLayout()

                personalInfoLoader.visibility = View.GONE
                personalInfoSection.visibility = View.VISIBLE
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    personalInfoLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, personalInfoContainer, view).show()
                }
            }
        }

    }

    private fun fillLayout() {
        viewModel.personalInfo?.let {
            personalInfoNameInput.setText(it.name)
            personalInfoLastNameInput.setText(it.lastName)
            principalEmailInput.setText(it.email)
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}