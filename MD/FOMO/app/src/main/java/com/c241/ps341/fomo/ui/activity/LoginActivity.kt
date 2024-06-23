@file:Suppress("DEPRECATION")

package com.c241.ps341.fomo.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.c241.ps341.fomo.BuildConfig
import com.c241.ps341.fomo.data.UserData
import com.c241.ps341.fomo.data.repository.UserRepository
import com.c241.ps341.fomo.databinding.ActivityLoginBinding
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var viewModel: MainViewModel
    private var showPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        auth = Firebase.auth
        db = Firebase.database
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]

        with(binding) {
            setContentView(root)
            btnLogin.isEnabled = false
            btnPassword.visibility = View.GONE

            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnLogin.isEnabled =
                        etEmail.text.toString()
                            .isNotEmpty() && etEmail.error == null && etPassword.text.toString()
                            .isNotEmpty() && etPassword.error == null
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        btnPassword.visibility = View.GONE
                    } else {
                        btnPassword.visibility = View.VISIBLE
                    }

                    btnLogin.isEnabled =
                        etEmail.text.toString()
                            .isNotEmpty() && etEmail.error == null && etPassword.text.toString()
                            .isNotEmpty() && etPassword.error == null
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            btnPassword.setOnClickListener {
                showPassword = !showPassword

                if (showPassword) {
                    ivPasswordOff.visibility = View.GONE
                    ivPasswordOn.visibility = View.VISIBLE
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    ivPasswordOff.visibility = View.VISIBLE
                    ivPasswordOn.visibility = View.GONE
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }

            btnLogin.setOnClickListener {
                login(etEmail.text.toString(), etPassword.text.toString())
            }

            btnGoogle.setOnClickListener {
                loginWithGoogle()
            }

            val text = "if you don’t an account you can Register here!"
            val spannable = SpannableString(text)

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View) {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = Color.BLUE
                }
            }

            spannable.setSpan(
                clickableSpan,
                32, text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvRegister.text = spannable
            tvRegister.movementMethod = LinkMovementMethod.getInstance()
            tvRegister.highlightColor = Color.TRANSPARENT
        }
    }

    private fun loginWithGoogle() {
        val credentialManager = CredentialManager.create(this)
        val googleIdOption = GetGoogleIdOption
            .Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.CLIENT_ID).build()
        val request = GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                when (val credential = result.credential) {
                    is CustomCredential -> {
                        if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            try {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)
                                authWithGoogle(googleIdTokenCredential.idToken)
                            } catch (e: GoogleIdTokenParsingException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            } catch (e: GetCredentialException) {
                e.printStackTrace()
            }
        }
    }

    private fun authWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userRepository = UserRepository()
                    val password = hashPassword("FOMO_google_${user!!.uid}")
                    userRepository.createUser(
                        user.uid,
                        user.displayName.toString(),
                        password,
                        user.email.toString(),
                        user.photoUrl.toString()
                    )
                    setCurrentUser(user)
                }
            }
    }

    private fun login(email: String, password: String) {
        val progressDialog = ProgressDialog.show(this, null, "Harap tunggu")
        val userRef = db.reference.child("user")
        val query = userRef.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(UserData::class.java)

                        if (user?.password == hashPassword(password)) {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this@LoginActivity) { it2 ->
                                    if (it2.isSuccessful) {
                                        progressDialog.dismiss()
                                        auth.currentUser?.uid?.let { it1 ->
                                            viewModel.setId(it1)
                                        }

                                        user.name?.let { it1 -> viewModel.setName(it1) }
                                        user.email?.let { it1 -> viewModel.setEmail(it1) }
                                        user.photoUrl?.let { it1 -> viewModel.setPhoto(it1) }
                                        setCurrentUser(auth.currentUser)
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Database error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "Kata sandi yang anda masukkan salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Email yang anda masukkan tidak ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setCurrentUser(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            viewModel.setId(currentUser.uid)
            currentUser.displayName?.let { value -> viewModel.setName(value) }
            currentUser.email?.let { value -> viewModel.setEmail(value) }
            currentUser.photoUrl?.let { value -> viewModel.setPhoto(value.toString()) }
            SplashActivity().generateToken(this, auth, viewModel)
        }
    }

    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}