package com.android.misfinanzas.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.domain.AppConfig
import com.android.misfinanzas.R
import com.android.misfinanzas.base.BaseFragment
import com.android.misfinanzas.databinding.FragmentLoginBinding
import com.android.misfinanzas.utils.isConnected
import com.android.misfinanzas.utils.openURL
import com.android.misfinanzas.utils.showShortToast
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private val TAG = this.javaClass.name

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        progressListener.show()
        lifecycleScope.launchWhenCreated {
            viewModel.checkSession()
        }
    }

    private val viewStateObserver = Observer<LoginViewState> { state ->
        if (state !is LoginViewState.Logged) {
            progressListener.hide()
        }

        when (state) {
            is LoginViewState.NotLogged -> setupEvents()
            is LoginViewState.Logged -> goToLoggedScreen()
            is LoginViewState.WrongUserOrPassword -> context?.showShortToast(R.string.info_wrong_user_or_password)
        }
    }

    //is Result.Error -> showExceptionMessage(TAG, getString(R.string.error_getting_user, result.exception), ErrorType.TYPE_RETROFIT)

    private fun setupEvents() {
        context?.showShortToast(R.string.info_please_log_in)

        binding.btnLogin.setOnClickListener {
            if (context?.isConnected(getString(R.string.error_not_network_no_login)) == true) {
                val user = binding.etUser.text.trim().toString()
                val password = binding.etPassword.text.trim().toString()
                if (user.isEmpty() || password.isEmpty()) {
                    context?.showShortToast(R.string.info_enter_user_and_password)
                } else {
                    progressListener.show()
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
