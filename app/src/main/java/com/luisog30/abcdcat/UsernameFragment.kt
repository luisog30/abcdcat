package com.luisog30.abcdcat

import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class UsernameFragment : Fragment() {

    private lateinit var username_textbox: EditText
    private lateinit var send_username_btn: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_username, container, false)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        configureView(view)

        return view
    }

    class InputFilterSpace(): InputFilter {

        override fun filter(source:CharSequence, start:Int, end:Int, dest: Spanned, dstart:Int, dend:Int): CharSequence? {


            try {

                val input = (dest.subSequence(0, dstart).toString() + source + dest.subSequence(
                    dend,
                    dest.length
                ))

                if (input[input.length - 1].toString() == " ") {

                    return "_"

                } else {

                    return null

                }

            } catch (e: Exception) {

                return null

            }

        }

    }

    private fun configureView(view: View) {


        username_textbox = view.findViewById(R.id.textbox_username)
        send_username_btn = view.findViewById(R.id.btn_send_username)

        username_textbox.setFilters(arrayOf<InputFilter>(InputFilterSpace()))

        send_username_btn.setOnClickListener{

            if (username_textbox.text.toString() != "") {

                database.child("users").child(auth.currentUser!!.uid).child("username")
                    .setValue(username_textbox.text.toString())

                findNavController().navigate(R.id.action_usernameFragment_to_categoriesFragment)

            }

        }

        view.setOnClickListener {

            it.hideKeyboard()

            username_textbox.clearFocus()

        }

    }

    private fun View.hideKeyboard() {

        val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)

    }

}
