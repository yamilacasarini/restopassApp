package com.example.restopass.main.ui.settings.communications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.login.domain.Validation
import com.example.restopass.login.domain.ValidationFactory
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertBody
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.main.ui.settings.personalInfo.PersonalInfoViewModel
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_communications.*
import kotlinx.coroutines.*
import timber.log.Timber

class CommunicationsFragment : Fragment(), SecondaryEmailAdapter.SecondaryEmailListener {

    private lateinit var viewModel: PersonalInfoViewModel

    private lateinit var secondaryEmailsRecyclerView: RecyclerView
    private lateinit var emailAdapter: SecondaryEmailAdapter

    private val emailRegexes = ValidationFactory.emailValidations

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_communications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(PersonalInfoViewModel::class.java)
        emailAdapter =
            SecondaryEmailAdapter(
                this
            )
        secondaryEmailsRecyclerView = secondaryEmailsRecyclerContainer.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = emailAdapter
        }

        addSecondaryEmailButton.setOnClickListener {
            onAddEmailClick(secondaryEmailInputLayout.editText?.text.toString())
        }

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.communications_settings)
            visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        communicationsLoader.visibility = View.VISIBLE
        communicationsSection.visibility = View.GONE
        coroutineScope.launch {
            try {
                viewModel.get()
                emailAdapter.emails = viewModel.personalInfo!!.secondaryEmails!!
                emailAdapter.notifyDataSetChanged()


                communicationsLoader.visibility = View.GONE
                communicationsSection.visibility = View.VISIBLE
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    communicationsLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, communicationsContainer, view).show()
                }
            }
        }


    }

    private fun isValid(validations: List<Validation>, layout: TextInputLayout) : Boolean {
        validations.find {
            !it.regex.matches(layout.editText?.text.toString())
        }?.let {
            layout.error = it.errorMessage
            return false
        }

        layout.error = null
        return true
    }

    private fun onAddEmailClick(email: String) {
        val emailInput = secondaryEmailInputLayout
        if (isValid(emailRegexes, emailInput)) {
            toggleLoader()
            coroutineScope.launch {
                try {

                    viewModel.addSecondaryEmail(email)
                    emailAdapter.notifyDataSetChanged()
                    emailInput.editText?.text?.clear()

                    toggleLoader()
                } catch (e: Exception) {
                    if (isActive) {
                        Timber.e(e)
                        toggleLoader()
                        AlertDialogUtils.buildAlertDialog(
                            e,
                            layoutInflater,
                            communicationsContainer,
                            view
                        ).show()
                    }
                }
            }
        }
    }

    private fun toggleLoader() {
        addEmailLoader.visibility = if (addEmailLoader.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        addSecondaryEmailButton.visibility = if (addSecondaryEmailButton.visibility == View.VISIBLE) View.GONE else View.VISIBLE

        secondaryEmailInputLayout.isEnabled = !secondaryEmailInputLayout.isEnabled
    }

    override suspend fun onDeleteEmailClick(email: String) {
        AlertDialog.getActionDialogWithParams(context, layoutInflater,
            communicationsContainer, ::deleteEmail, email, AlertBody(getString(R.string.deleteEmailTitle))
        ).show()
    }

    private fun deleteEmail(email: Any) {
        toggleLoader()
        coroutineScope.launch {
            try {
                viewModel.deleteSecondaryEmail(email as String)

                emailAdapter.notifyDataSetChanged()
                toggleLoader()

            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    toggleLoader()
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, communicationsContainer).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}