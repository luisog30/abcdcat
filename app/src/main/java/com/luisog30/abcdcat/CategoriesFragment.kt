package com.luisog30.abcdcat

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CategoriesFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    lateinit var A1_btn: Button
    lateinit var A2_btn: Button
    lateinit var A3_btn: Button

    lateinit var B1_btn: Button
    lateinit var B2_btn: Button
    lateinit var B3_btn: Button

    lateinit var C1_btn: Button
    lateinit var C2_btn: Button
    lateinit var C3_btn: Button

    lateinit var loading_label: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        auth = FirebaseAuth.getInstance()

        configureView(view)

        FirebaseDatabase.getInstance().reference
            .child("users").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val us = dataSnapshot.getValue(RankingFragment.Userclass::class.java)

                    us?.let{

                        if (us.username == "") {

                            findNavController().navigate(R.id.action_categoriesFragment_to_usernameFragment)

                        }

                    }

                    loading_label.visibility = View.GONE
                    view.setAllEnabled(true)

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }

            })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        super.onPrepareOptionsMenu(menu)
        menu.setGroupVisible(R.id.ranking_group, true)

        menu.findItem(R.id.btn_ranking).setOnMenuItemClickListener {

            if (loading_label.visibility == View.GONE) {

                findNavController().navigate(R.id.action_categoriesFragment_to_rankingFragment)

            }

            true

        }


    }

    fun View.setAllEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup) children.forEach { child -> child.setAllEnabled(enabled) }
    }

    private fun configureView(view: View) {

        view.setAllEnabled(false)

        loading_label = view.findViewById(R.id.label_loading2)

        A1_btn = view.findViewById(R.id.btn_A1)
        A2_btn = view.findViewById(R.id.btn_A2)
        A3_btn = view.findViewById(R.id.btn_A3)

        B1_btn = view.findViewById(R.id.btn_B1)
        B2_btn = view.findViewById(R.id.btn_B2)
        B3_btn = view.findViewById(R.id.btn_B3)

        C1_btn = view.findViewById(R.id.btn_C1)
        C2_btn = view.findViewById(R.id.btn_C2)
        C3_btn = view.findViewById(R.id.btn_C3)

        view.findViewById<Button>(R.id.btn_goto_example).setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_exampleFragment)

        }

        A1_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "A1", "icon" to A1_btn.text, "cat" to getString(R.string.languages_string)))

        }

        A2_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "A2", "icon" to A2_btn.text, "cat" to getString(R.string.sports_string)))

        }

        A3_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "A3", "icon" to A3_btn.text, "cat" to getString(R.string.economy_string)))

        }

        B1_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "B1", "icon" to B1_btn.text, "cat" to getString(R.string.science_string)))

        }

        B2_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "B2", "icon" to B2_btn.text, "cat" to getString(R.string.geography_string)))

        }

        B3_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "B3", "icon" to B3_btn.text, "cat" to getString(R.string.maths_string)))

        }

        C1_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "C1", "icon" to C1_btn.text, "cat" to getString(R.string.history_string)))

        }

        C2_btn.setOnClickListener {

            findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "C2", "icon" to C2_btn.text, "cat" to getString(R.string.general_culture_string)))

        }

        C3_btn.setOnClickListener {
            
            Toast.makeText(this.context, getString(R.string.no_tests_string), Toast.LENGTH_SHORT).show()
            //findNavController().navigate(R.id.action_categoriesFragment_to_chooseTestFragment, bundleOf("id" to "C3", "icon" to C3_btn.text, "cat" to getString(R.string.others_string)))

        }

    }

}
