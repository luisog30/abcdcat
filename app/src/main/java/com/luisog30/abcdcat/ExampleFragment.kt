package com.luisog30.abcdcat

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class ExampleFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var score: Int = 0

    lateinit var next_btn: Button
    lateinit var a_btn: Button
    lateinit var b_btn: Button
    lateinit var c_btn: Button
    lateinit var d_btn: Button

    lateinit var question_label: TextView
    lateinit var solution_label: TextView
    lateinit var point_label: TextView
    lateinit var title_emojis_label: TextView
    lateinit var loading_label: TextView

    var answer: Int = 0
    var answered: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_example, container, false)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        configureView(view)

        loadScore(view)

        return view
    }

    fun View.setAllEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup) children.forEach { child -> child.setAllEnabled(enabled) }
    }

    private fun loadScore(view: View){

        FirebaseDatabase.getInstance().reference
            .child("users").child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val us = dataSnapshot.getValue(RankingFragment.Userclass::class.java)

                    us?.let{

                        score = it.score!!.toInt()

                    }

                    point_label.text = score.toString() + " " + getString(R.string.points_string)
                    point_label.setTextColor(getColor(context!!, R.color.colorAccent))

                    view.setAllEnabled(true)
                    loading_label.visibility = View.GONE

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }

            })

    }

    private fun getRandomWord(fileLines: List<String>,  ex: Int): String {

        var rnd = (0 until (fileLines.size)).random()
        var rnd2 = (1 until (fileLines[rnd].split("$").size)).random()

        while (fileLines[ex].contains(fileLines[rnd].split("$")[rnd2])) {
            rnd = (0 until (fileLines.size)).random()
            rnd2 = (1 until (fileLines[rnd].split("$").size)).random()
        }

        return fileLines[rnd].split("$")[rnd2]
    }

    private fun getQuestion() {

        val lang = Locale.getDefault().displayLanguage

        var filename = "english - catalan.txt"

        if (lang.toLowerCase() == "español") {

            filename = "english - spanish.txt"

        }

        val fileString = context!!.assets.open(filename).bufferedReader().use {
            it.readText()
        }

        val fileLines = fileString.split("\n")

        val rnd = (0 until (fileLines.size)).random()
        val sol = (1 until (fileLines[rnd].split("$").size)).random()
        answer = (0..3).random()


        question_label.text = fileLines[rnd].split("$")[0]

        when (answer) {
            0 -> {

                a_btn.text = fileLines[rnd].split("$")[sol]
                b_btn.text = getRandomWord(fileLines, rnd)
                c_btn.text = getRandomWord(fileLines, rnd)
                d_btn.text = getRandomWord(fileLines, rnd)

            }
            1 -> {

                a_btn.text = getRandomWord(fileLines, rnd)
                b_btn.text = fileLines[rnd].split("$")[sol]
                c_btn.text = getRandomWord(fileLines, rnd)
                d_btn.text = getRandomWord(fileLines, rnd)

            }
            2 -> {

                a_btn.text = getRandomWord(fileLines, rnd)
                b_btn.text = getRandomWord(fileLines, rnd)
                c_btn.text = fileLines[rnd].split("$")[sol]
                d_btn.text = getRandomWord(fileLines, rnd)

            }
            3 -> {

                a_btn.text = getRandomWord(fileLines, rnd)
                b_btn.text = getRandomWord(fileLines, rnd)
                c_btn.text = getRandomWord(fileLines, rnd)
                d_btn.text = fileLines[rnd].split("$")[sol]

            }
        }

    }

    private fun showSolution() {

        when (answer) {
            0 -> {

                a_btn.background.setTint(Color.GREEN)

            }
            1 -> {

                b_btn.background.setTint(Color.GREEN)

            }
            2 -> {

                c_btn.background.setTint(Color.GREEN)

            }
            3 -> {

                d_btn.background.setTint(Color.GREEN)

            }
        }

    }

    private fun grayButtons() {

        a_btn.isClickable = false
        b_btn.isClickable = false
        c_btn.isClickable = false
        d_btn.isClickable = false

        a_btn.background.setTint(Color.GRAY)
        b_btn.background.setTint(Color.GRAY)
        c_btn.background.setTint(Color.GRAY)
        d_btn.background.setTint(Color.GRAY)

    }

    data class User(
        var username: String? = "",
        var email: String? = ""
    )

    private fun configureView(view: View) {

        answered = false

        view.setAllEnabled(false)

        loading_label = view.findViewById(R.id.label_loading3)

        val shake = AnimationUtils.loadAnimation(this.context, R.anim.shake_button)
        val size = AnimationUtils.loadAnimation(this.context, R.anim.change_size)
        val size_slow = AnimationUtils.loadAnimation(this.context, R.anim.change_size_slow)

        next_btn = view.findViewById(R.id.btn_next)
        question_label = view.findViewById(R.id.label_question)
        a_btn = view.findViewById(R.id.btn_a)
        b_btn = view.findViewById(R.id.btn_b)
        c_btn = view.findViewById(R.id.btn_c)
        d_btn = view.findViewById(R.id.btn_d)

        solution_label = view.findViewById(R.id.label_solution)
        point_label = view.findViewById(R.id.label_point)
        title_emojis_label = view.findViewById(R.id.label_title_emojis)

        getQuestion()

        next_btn.setOnClickListener {

            a_btn.clearAnimation()
            b_btn.clearAnimation()
            c_btn.clearAnimation()
            d_btn.clearAnimation()

            title_emojis_label.clearAnimation()

            solution_label.text = ""

            point_label.text = score.toString() + " " + getString(R.string.points_string)
            point_label.setTextColor(getColor(this.context!!, R.color.colorAccent))

            answered = false
            a_btn.isClickable = true
            b_btn.isClickable = true
            c_btn.isClickable = true
            d_btn.isClickable = true
            a_btn.background.setTint(getColor(this.context!!, R.color.colorPrimaryDark))
            b_btn.background.setTint(getColor(this.context!!, R.color.colorPrimaryDark))
            c_btn.background.setTint(getColor(this.context!!, R.color.colorPrimaryDark))
            d_btn.background.setTint(getColor(this.context!!, R.color.colorPrimaryDark))

            title_emojis_label.setText(R.string.question_emojis_string)
            getQuestion()

        }

        a_btn.setOnClickListener {

            if (!answered) {

                answered = true

                grayButtons()
                showSolution()

                if (answer == 0) {

                    score += 1

                    solution_label.text = getString(R.string.correct_answer_string)
                    solution_label.setTextColor(Color.GREEN)

                    point_label.text = "+" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.GREEN)

                    title_emojis_label.setText(R.string.correct_answer_emojis_string)
                    title_emojis_label.startAnimation(size_slow)

                } else {

                    score -= 1

                    if (score < 0) { score = 0 }

                    solution_label.text = getString(R.string.incorrect_answer_string)
                    solution_label.setTextColor(Color.RED)

                    point_label.text = "-" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.RED)

                    a_btn.startAnimation(shake)
                    a_btn.background.setTint(Color.RED)

                    title_emojis_label.setText(R.string.wrong_answer_emojis_string)
                    title_emojis_label.startAnimation(size)

                }

            }

            database.child("users").child(auth.currentUser!!.uid).child("score").setValue(score)

        }

        b_btn.setOnClickListener{

            if (!answered) {

                answered = true

                grayButtons()
                showSolution()

                if (answer == 1) {

                    score += 1

                    point_label.text = "+" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.GREEN)

                    solution_label.text = getString(R.string.correct_answer_string)
                    solution_label.setTextColor(Color.GREEN)

                    title_emojis_label.setText(R.string.correct_answer_emojis_string)
                    title_emojis_label.startAnimation(size_slow)

                } else {

                    score -= 1

                    if (score < 0) { score = 0 }

                    solution_label.text = getString(R.string.incorrect_answer_string)
                    solution_label.setTextColor(Color.RED)

                    point_label.text = "-" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.RED)

                    b_btn.startAnimation(shake)
                    b_btn.background.setTint(Color.RED)

                    title_emojis_label.setText(R.string.wrong_answer_emojis_string)
                    title_emojis_label.startAnimation(size)

                }

            }

            database.child("users").child(auth.currentUser!!.uid).child("score").setValue(score)

        }

        c_btn.setOnClickListener {

            if (!answered) {

                answered = true

                grayButtons()
                showSolution()

                if (answer == 2) {

                    score += 1

                    point_label.text = "+" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.GREEN)

                    solution_label.text = getString(R.string.correct_answer_string)
                    solution_label.setTextColor(Color.GREEN)

                    title_emojis_label.setText(R.string.correct_answer_emojis_string)
                    title_emojis_label.startAnimation(size_slow)

                } else {

                    score -= 1

                    if (score < 0) { score = 0 }

                    solution_label.text = getString(R.string.incorrect_answer_string)
                    solution_label.setTextColor(Color.RED)

                    point_label.text = "-" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.RED)

                    c_btn.startAnimation(shake)
                    c_btn.background.setTint(Color.RED)

                    title_emojis_label.setText(R.string.wrong_answer_emojis_string)
                    title_emojis_label.startAnimation(size)

                }

            }

            database.child("users").child(auth.currentUser!!.uid).child("score").setValue(score)

        }

        d_btn.setOnClickListener {

            if (!answered) {

                answered = true

                grayButtons()
                showSolution()

                if (answer == 3) {

                    score += 1

                    point_label.text = "+" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.GREEN)

                    solution_label.text = getString(R.string.correct_answer_string)
                    solution_label.setTextColor(Color.GREEN)

                    title_emojis_label.setText(R.string.correct_answer_emojis_string)
                    title_emojis_label.startAnimation(size_slow)

                } else {

                    score -= 1

                    if (score < 0) { score = 0 }

                    solution_label.text = getString(R.string.incorrect_answer_string)
                    solution_label.setTextColor(Color.RED)

                    point_label.text = "-" + getString(R.string.one_point_string)
                    point_label.setTextColor(Color.RED)

                    d_btn.startAnimation(shake)
                    d_btn.background.setTint(Color.RED)

                    title_emojis_label.setText(R.string.wrong_answer_emojis_string)
                    title_emojis_label.startAnimation(size)

                }

            }

            database.child("users").child(auth.currentUser!!.uid).child("score").setValue(score)

        }

    }

}
