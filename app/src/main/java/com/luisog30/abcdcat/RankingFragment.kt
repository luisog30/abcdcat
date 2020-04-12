package com.luisog30.abcdcat

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_ranking.*
import kotlinx.android.synthetic.main.recyclerview_row.*
import kotlinx.android.synthetic.main.recyclerview_row.view.*


class RankingFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var logout_btn: Button
    private lateinit var back_btn: Button
    private lateinit var user_info_label: TextView
    private lateinit var loading_label: TextView

    private lateinit var username: String
    private lateinit var score: String
    private var rankingpos: String = ""
    private lateinit var telephone: String

    private lateinit var ranking_recycler_view: RecyclerView

    private var ranking_list: ArrayList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.fragment_ranking, container, false)

        configureView(view)

        FirebaseDatabase.getInstance().reference
            .child("users").orderByChild("score").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var i = 1

                    dataSnapshot.children.forEach{

                        val us = it.getValue(Userclass::class.java)

                        us?.let{

                            ranking_list.add(us.username + " - " + us.score.toString() + " " + getString(R.string.points_string))

                            if (us.username == username) {

                                rankingpos = i.toString()
                                user_info_label.text = String.format(getString(R.string.user_info_string), username, telephone, score, rankingpos)

                            }

                        }

                        i += 1

                    }

                    view.setAllEnabled(true)

                    ranking_recycler_view.layoutManager = LinearLayoutManager(context)
                    ranking_recycler_view.adapter = UserAdapter(ranking_list, context!!)

                    loading_label.visibility = View.GONE

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }

            })

        FirebaseDatabase.getInstance().reference
            .child("users").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val us = dataSnapshot.getValue(Userclass::class.java)

                    us?.let{

                        username = it.username.toString()
                        score = it.score.toString()
                        telephone = auth.currentUser!!.phoneNumber.toString()

                        user_info_label.text = String.format(getString(R.string.user_info_string), username, telephone, score, rankingpos)

                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {

                    user_info_label.text = getString(R.string.database_connection_error_string)

                }

            })

        return view
    }

    fun View.setAllEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup) children.forEach { child -> child.setAllEnabled(enabled) }
    }

    class UserAdapter(val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false))
        }

        // Gets the number of animals in the list
        override fun getItemCount(): Int {
            return items.size
        }

        // Binds each animal in the ArrayList to a view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.row_recyclerview?.text = (position + 1).toString() + ". " + items.get(position)
        }

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val row_recyclerview = view.textbox_test_name
    }

    data class Userclass(
        var username: String? = "",
        var score: Int? = 0
    )

    private fun configureView(view: View) {

        view.setAllEnabled(false)

        ranking_recycler_view = view.findViewById(R.id.rankingRecyclerView)
        loading_label = view.findViewById(R.id.label_loading)

        logout_btn = view.findViewById(R.id.btn_logout)
        back_btn = view.findViewById(R.id.btn_back)
        user_info_label = view.findViewById(R.id.label_user_info)

        back_btn.setOnClickListener {

            findNavController().navigate(R.id.action_rankingFragment_to_categoriesFragment)

        }

        logout_btn.setOnClickListener {

            lateinit var dialog: AlertDialog

            val builder = AlertDialog.Builder(this.context)

            builder.setTitle(R.string.perform_logout_string)

            builder.setMessage(R.string.sure_logout_string)

            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> logout()
                    DialogInterface.BUTTON_NEGATIVE -> return@OnClickListener
                }
            }

            builder.setPositiveButton("\uD83D\uDE3F SÍ", dialogClickListener)
            builder.setNegativeButton("\uD83D\uDC45 NO", dialogClickListener)

            dialog = builder.create()

            dialog.show()

        }

    }

    private fun logout(){

        auth.signOut()

        val intent = Intent(this.context, MainActivity::class.java)
        startActivity(intent)

        activity!!.finish()

    }

}
