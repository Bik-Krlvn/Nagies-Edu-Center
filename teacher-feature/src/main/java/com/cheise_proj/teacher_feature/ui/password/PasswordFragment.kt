package com.cheise_proj.teacher_feature.ui.password


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cheise_proj.presentation.factory.ViewModelFactory
import com.cheise_proj.presentation.model.vo.STATUS
import com.cheise_proj.presentation.utils.IPreference
import com.cheise_proj.presentation.utils.InputValidation
import com.cheise_proj.presentation.viewmodel.user.UserViewModel
import com.cheise_proj.teacher_feature.R
import com.cheise_proj.teacher_feature.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_password.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class PasswordFragment : BaseFragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var prefs: IPreference

    @Inject
    lateinit var inputValidation: InputValidation

    private lateinit var viewModel: UserViewModel
    private var identifier: Int = -1
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = Snackbar.make(root, "", Snackbar.LENGTH_LONG)
        btn_change_password.setOnClickListener {
            inputValidation.hideKeyboard(it)
            changeAccountPassword()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        identifier = prefs.getUserSession().uuid
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
    }

    private fun changeAccountPassword() {
        if (!inputValidation.isEditTextFilled(et_old_password)) return
        if (!inputValidation.isEditTextFilled(et_new_password)) return
        if (!inputValidation.isEditTextFilled(et_confirm_password)) return

        val oldPassword = et_old_password.text.toString().trim()
        val newPassword = et_new_password.text.toString().trim()
        val confirmPassword = et_confirm_password.text.toString().trim()
        if (newPassword != confirmPassword) {
            showSnackMessage(getString(R.string.password_reset_mismatch))
            return
        }

        viewModel.updateAccountPassword(identifier.toString(), oldPassword, newPassword)
            .observe(viewLifecycleOwner,
                Observer {
                    when (it.status) {
                        STATUS.LOADING -> showSnackMessage(getString(R.string.password_reset_loading))
                        STATUS.SUCCESS -> {
                            it.data?.let { isSuccess ->
                                if (isSuccess) {
                                    resetPasswordField()
                                    showSnackMessage(getString(R.string.password_reset_success))
                                } else {
                                    showSnackMessage(getString(R.string.password_reset_failed))
                                }
                            }
                        }
                        STATUS.ERROR -> showSnackMessage(it.message!!)
                    }
                })

    }

    private fun showSnackMessage(msg: String = "", show: Boolean = true) {
        if (show) {
            snackbar.setText(msg).show()
        } else {
            snackbar.dismiss()
        }
    }

    private fun resetPasswordField() {
        et_old_password.text.clear()
        et_new_password.text.clear()
        et_confirm_password.text.clear()
    }

    override fun onPause() {
        super.onPause()
        inputValidation.hideKeyboard(root)
    }

}
