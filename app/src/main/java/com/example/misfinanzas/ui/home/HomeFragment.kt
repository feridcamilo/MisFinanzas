package com.example.misfinanzas.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.misfinanzas.R
import com.example.misfinanzas.communication.IGoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class HomeFragment : Fragment() {

    private val TAG: String = this.toString()
    private val RC_SIGN_IN: Int = 0
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var googleSignIn: IGoogleSignIn
    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            googleSignIn = activity as IGoogleSignIn
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString() + " must implement IGoogleSignIn"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        signInButton = root.findViewById(R.id.sign_in_button)
        signOutButton = root.findViewById(R.id.sign_out_button)
        signInButton.setOnClickListener { signIn() }
        signOutButton.setOnClickListener { signOut() }

        updateUI(googleSignIn.getGoogleSignInAccount())

        return root
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignIn.getGoogleSignInClient().signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // TODO show an error message
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account == null) {
            signInButton.visibility = View.VISIBLE
            signOutButton.visibility = View.GONE
            Toast.makeText(activity, "Please Sign-In", Toast.LENGTH_LONG).show()
        } else {
            signInButton.visibility = View.GONE
            signOutButton.visibility = View.VISIBLE
            getGoogleInfo()
        }
    }

    private fun getGoogleInfo() {
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        if (account != null) {
            val personName = account.displayName
            val personGivenName = account.givenName
            val personFamilyName = account.familyName
            val personEmail = account.email
            val personId = account.id
            val personPhoto: Uri? = account.photoUrl

            Toast.makeText(activity, "Signed-In with " + personEmail, Toast.LENGTH_LONG).show()
        }
    }

    private fun signOut() {
        googleSignIn.getGoogleSignInClient().signOut()
            .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                Toast.makeText(activity, "Your session has been closed", Toast.LENGTH_LONG).show()
                updateUI(null)
            })
    }
}
