package com.c241.ps341.fomo.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText

class CustomEditEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs), View.OnTouchListener {
    init {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                // Do nothing.
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                val emailMatcher = Regex(emailPattern)

                error = if (p0.isNotEmpty() && !emailMatcher.matches(p0.toString())) {
                    "Invalid email format"
                } else {
                    null
                }
            }

            override fun afterTextChanged(p0: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }
}