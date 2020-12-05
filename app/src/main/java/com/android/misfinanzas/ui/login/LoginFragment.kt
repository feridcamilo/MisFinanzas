package com.android.misfinanzas.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.android.domain.UserSesion
import com.android.domain.model.User
import com.android.domain.result.Result
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.base.LoginListener
import com.android.misfinanzas.databinding.FragmentLoginBinding
import com.android.misfinanzas.utils.isConnected
import com.android.misfinanzas.utils.showShortToast
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private val TAG = this.javaClass.name

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: FragmentLoginBinding

    private var loginListener: LoginListener? = null
    private lateinit var syncUserObserver: Observer<Result<User?>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setupUserObserver()
    }

    fun setListener(listener: LoginListener) {
        loginListener = listener
    }

    private fun setupEvents() {
        binding.btnLogin.setOnClickListener {
            if (context?.isConnected(getString(R.string.error_not_network_no_login)) == true) {
                val credential = getCredential()
                if (credential != null) {
                    viewModel.setCredential(credential)
                }
            }
        }
    }

    private fun setupUserObserver() {
        syncUserObserver = Observer { result ->
            when (result) {
                is Result.Loading -> progressListener.show()
                is Result.Success -> {
                    if (result.data == null) {
                        context?.showShortToast(R.string.info_wrong_user_or_password)
                        progressListener.hide()
                    } else {
                        context?.showShortToast(R.string.info_user_saved)
                        makeLogin(result.data!!)
                    }
                }
                is Result.Error -> showExceptionMessage(
                    TAG,
                    getString(R.string.error_getting_user, result.exception),
                    ErrorType.TYPE_RETROFIT
                )
            }
        }

        viewModel.syncUser.observe(viewLifecycleOwner, syncUserObserver)
    }

    private fun makeLogin(user: User) {
        UserSesion.setUser(user)
        loginListener?.onLogged()
    }

    private fun getCredential(): UserCredential? {
        val user = binding.etUser.text.trim().toString()
        val password = binding.etPassword.text.trim().toString()
        return if (user.isEmpty() || password.isEmpty()) {
            context?.showShortToast(R.string.info_enter_user_and_password)
            null
        } else {
            UserCredential(user, password)
        }
    }

}
