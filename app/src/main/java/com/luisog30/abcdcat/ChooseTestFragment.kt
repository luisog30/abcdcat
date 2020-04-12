package com.luisog30.abcdcat

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.recyclerview_row_button.view.*


class ChooseTestFragment : Fragment() {

    var category: String = ""
    var cat_id: String = ""

    public var lang: Boolean = false

    private lateinit var add_test_btn: Button
    private lateinit var category_label: TextView
    private lateinit var loading_label: TextView
    private lateinit var test_recycler_view: RecyclerView

    private lateinit var frag: Fragment

    private var test_list: ArrayList<String> = ArrayList()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_choose_test, container, false)
        frag = this

        auth = FirebaseAuth.getInstance()

        configureView(view)

        lang = this.lang

        FirebaseDatabase.getInstance().reference
            .child("tests").child(cat_id).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val c = dataSnapshot.getValue(RankingFragment.Userclass::class.java)

                    c?.let{

                        //test_list.add

                    }

                    view.setAllEnabled(true)
                    loading_label.visibility = View.GONE

                    test_recycler_view.layoutManager = LinearLayoutManager(context)
                    test_recycler_view.adapter = TestAdapter(test_list, context!!, frag, lang)

                    loading_label.visibility = View.GONE


                }

                override fun onCancelled(databaseError: DatabaseError) {

                    view.setAllEnabled(true)
                    loading_label.visibility = View.GONE

                }

            })

        return view

    }

    class TestAdapter(val items : ArrayList<String>, val context: Context, val parentFragment: Fragment, val lang:Boolean) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_row_button, parent, false))

        }

        // Gets the number of animals in the list
        override fun getItemCount(): Int {
            return items.size
        }

        // Binds each animal in the ArrayList to a view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.row_recyclerview.text = items.get(position)

            holder.row_recyclerview.setOnClickListener{

                if (lang) {

                    parentFragment.findNavController().navigate(R.id.action_chooseTestFragment_to_exampleFragment)

                } else {

                    parentFragment.findNavController().navigate(R.id.action_chooseTestFragment_to_testFragment, bundleOf("cat" to parentFragment.arguments!!.getString("cat").toString()))

                }

            }

        }

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val row_recyclerview: Button = view.btn_recycler_view

    }

    fun View.setAllEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup) children.forEach { child -> child.setAllEnabled(enabled) }

    }

    private fun configureView(view: View) {

        view.setAllEnabled(false)

        category = arguments!!.getString("id").toString()

        add_test_btn = view.findViewById(R.id.btn_create_test)
        category_label = view.findViewById(R.id.label_category)
        loading_label = view.findViewById(R.id.label_loading4)

        add_test_btn.setOnClickListener {

            Toast.makeText(this.context, getString(R.string.not_add_test_string), Toast.LENGTH_LONG).show()

        }

        test_recycler_view = view.findViewById(R.id.chooseTestRecyclerView)

        test_list.add(getString(R.string.example_test_string))


        category_label.text = arguments!!.getString("icon").toString() + " " + arguments!!.getString("cat").toString()

        if (arguments!!.getString("cat") == getString(R.string.languages_string)) {

            this.lang = true

        }

        test_recycler_view.layoutManager = LinearLayoutManager(context)
        test_recycler_view.adapter = TestAdapter(test_list, context!!, frag, this.lang)

    }

}
