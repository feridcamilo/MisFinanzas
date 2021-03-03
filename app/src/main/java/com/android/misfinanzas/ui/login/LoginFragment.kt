package com.android.misfinanzas.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.domain.AppConfig
import com.android.misfinanzas.R
import com.android.misfinanzas.databinding.FragmentLoginBinding
import com.android.misfinanzas.utils.*
import com.android.misfinanzas.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModel<LoginViewModel>()
    private val binding by viewBinding<FragmentLoginBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        showLoader()
        viewModel.checkSession()
    }

    private val viewStateObserver = Observer<LoginViewState> { state ->
        if (state !is LoginViewState.Logged) {
            hideLoader()
        }

        when (state) {
            is LoginViewState.NotLogged -> setupEvents()
            is LoginViewState.Logged -> goToLoggedScreen()
            is LoginViewState.WrongUserOrPassword -> context?.showShortToast(R.string.info_wrong_user_or_password)
        }
    }

    private fun setupEvents() {
        context?.showShortToast(R.string.info_please_log_in)

        binding.btnLogin.setOnClickListener {
            if (context?.isConnected(getString(R.string.error_not_network_no_login)) == true) {
                val user = binding.etUser.text.trim().toString()
                val password = binding.etPassword.text.trim().toString()
                if (user.isEmpty() || password.isEmpty()) {
                    context?.showShortToast(R.string.info_enter_user_and_password)
                } else {
                    showLoader()
                    viewModel.login(user, password)
                }
            }
        }

        binding.tvLink.setOnClickListener { context?.openURL(AppConfig.BASE_URL) }
    }

    private fun goToLoggedScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

}
