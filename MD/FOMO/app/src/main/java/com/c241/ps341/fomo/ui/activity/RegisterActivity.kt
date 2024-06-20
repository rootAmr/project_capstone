@file:Suppress("DEPRECATION")

package com.c241.ps341.fomo.ui.activity

import android.app.ProgressDialog
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
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.c241.ps341.fomo.data.UserData
import com.c241.ps341.fomo.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private var showPassword = false
    private var showConfirmPassword = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        db = Firebase.database
        val userRef = db.reference.child("user")

        with(binding) {
            setContentView(root)
            btnRegister.isEnabled = false
            btnPassword.visibility = View.GONE
            btnConfirmPassword.visibility = View.GONE

            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnRegister.isEnabled =
                        etName.text.toString().trim()
                            .isNotEmpty() && etEmail.text.toString()
                            .isNotEmpty() && etEmail.error == null && etPassword.text.toString()
                            .isNotEmpty() && etPassword.error == null && etConfirmPassword.text.toString()
                            .isNotEmpty() && etConfirmPassword.error == null && etPassword.text.toString() == etConfirmPassword.text.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnRegister.isEnabled =
                        etName.text.toString().trim()
                            .isNotEmpty() && etEmail.text.toString()
                            .isNotEmpty() && etEmail.error == null && etPassword.text.toString()
                            .isNotEmpty() && etPassword.error == null && etConfirmPassword.text.toString()
                            .isNotEmpty() && etConfirmPassword.error == null && etPassword.text.toString() == etConfirmPassword.text.toString()
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

                    btnRegister.isEnabled =
                        etName.text.toString().trim()
                            .isNotEmpty() && etEmail.text.toString()
                            .isNotEmpty() && etEmail.error == null && etPassword.text.toString()
                            .isNotEmpty() && etPassword.error == null && etConfirmPassword.text.toString()
                            .isNotEmpty() && etConfirmPassword.error == null && etPassword.text.toString() == etConfirmPassword.text.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            etConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isEmpty()) {
                        btnConfirmPassword.visibility = View.GONE
                    } else {
                        btnConfirmPassword.visibility = View.VISIBLE
                    }

                    if (s.length in 1..7) {
                        etConfirmPassword.setError("Password must be more than 8 characters", null)
                    } else {
                        if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                            etConfirmPassword.setError("Password doesn't match", null)
                        } else {
                            etConfirmPassword.error = null
                        }
                    }

                    btnRegister.isEnabled =
                        etName.text.toString().trim()
                            .isNotEmpty() && etEmail.text.toString()
                            .isNotEmpty() && etEmail.error == null && etPassword.text.toString()
                            .isNotEmpty() && etPassword.error == null && etConfirmPassword.text.toString()
                            .isNotEmpty() && etConfirmPassword.error == null && etPassword.text.toString() == etConfirmPassword.text.toString()
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

            btnConfirmPassword.setOnClickListener {
                showConfirmPassword = !showConfirmPassword

                if (showConfirmPassword) {
                    ivConfirmPasswordOff.visibility = View.GONE
                    ivConfirmPasswordOn.visibility = View.VISIBLE
                    etConfirmPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                } else {
                    ivConfirmPasswordOff.visibility = View.VISIBLE
                    ivConfirmPasswordOn.visibility = View.GONE
                    etConfirmPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                }
            }

            btnRegister.setOnClickListener {
                val progressDialog =
                    ProgressDialog.show(this@RegisterActivity, null, "Harap tunggu")
                val password = LoginActivity().hashPassword(etPassword.text.toString())

                val data = UserData(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    password,
                    ""
                )

                auth.createUserWithEmailAndPassword(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                ).addOnCompleteListener(this@RegisterActivity) {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        userRef.child(auth.currentUser?.uid!!).setValue(data) { error, _ ->
                            if (error != null) {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Gagal mendaftar akun",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("mgrlog", error.message)
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Daftar akun berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Gagal mendaftar akun",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("mgrlog", it.exception.toString())
                    }
                }
            }

            val text = "if you have account you can Login here!"
            val spannable = SpannableString(text)

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View) {
                    finish()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = Color.BLUE
                }
            }

            spannable.setSpan(
                clickableSpan,
                28, text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvLogin.text = spannable
            tvLogin.movementMethod = LinkMovementMethod.getInstance()
            tvLogin.highlightColor = Color.TRANSPARENT
        }
    }
}