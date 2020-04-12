package com.luisog30.abcdcat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var start_btn: Button
    private lateinit var phone_textbox: EditText
    private lateinit var resend_btn: Button
    private lateinit var verify_btn: Button
    private lateinit var info_btn: Button

    private lateinit var verify_textbox: EditText
    private lateinit var status_label: TextView
    private lateinit var write_code_label: TextView
    
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        configureView(view)

        auth = FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                verificationInProgress = false

                updateUI(STATE_VERIFY_SUCCESS, credential)

                signInWithPhoneAuthCredential(credential)

            }

            override fun onVerificationFailed(e: FirebaseException) {

                verificationInProgress = false

                if (e is FirebaseAuthInvalidCredentialsException) {

                    phone_textbox.error = getString(R.string.wrong_telephone_string)

                } else if (e is FirebaseTooManyRequestsException) {
                    Snackbar.make(view, getString(R.string.quota_exceded_string),
                        Snackbar.LENGTH_LONG).show()
                }

                updateUI(STATE_VERIFY_FAILED)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                storedVerificationId = verificationId
                resendToken = token

                updateUI(STATE_CODE_SENT)

            }
        }

        return view

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun configureView(view: View) {

        start_btn = view.findViewById(R.id.btn_send_code)
        verify_btn = view.findViewById(R.id.btn_verify)
        resend_btn = view.findViewById(R.id.btn_resend)
        phone_textbox = view.findViewById(R.id.textbox_phone)
        verify_textbox = view.findViewById(R.id.textbox_verification)
        write_code_label = view.findViewById(R.id.label_write_code)
        info_btn = view.findViewById(R.id.btn_info)

        status_label = view.findViewById(R.id.label_status)

        var prefix_list = mutableMapOf("AC" to "+247")

        prefix_list.put("AC", "+247");
        prefix_list.put("AD", "+376");
        prefix_list.put("AE", "+971");
        prefix_list.put("AF", "+93");
        prefix_list.put("AG", "+1-268");
        prefix_list.put("AI", "+1-264");
        prefix_list.put("AL", "+355");
        prefix_list.put("AM", "+374");
        prefix_list.put("AN", "+599");
        prefix_list.put("AO", "+244");
        prefix_list.put("AR", "+54");
        prefix_list.put("AS", "+1-684");
        prefix_list.put("AT", "+43");
        prefix_list.put("AU", "+61");
        prefix_list.put("AW", "+297");
        prefix_list.put("AX", "+358-18");
        prefix_list.put("AZ", "+374-97");
        prefix_list.put("AZ", "+994");
        prefix_list.put("BA", "+387");
        prefix_list.put("BB", "+1-246");
        prefix_list.put("BD", "+880");
        prefix_list.put("BE", "+32");
        prefix_list.put("BF", "+226");
        prefix_list.put("BG", "+359");
        prefix_list.put("BH", "+973");
        prefix_list.put("BI", "+257");
        prefix_list.put("BJ", "+229");
        prefix_list.put("BM", "+1-441");
        prefix_list.put("BN", "+673");
        prefix_list.put("BO", "+591");
        prefix_list.put("BR", "+55");
        prefix_list.put("BS", "+1-242");
        prefix_list.put("BT", "+975");
        prefix_list.put("BW", "+267");
        prefix_list.put("BY", "+375");
        prefix_list.put("BZ", "+501");
        prefix_list.put("CA", "+1");
        prefix_list.put("CC", "+61");
        prefix_list.put("CD", "+243");
        prefix_list.put("CF", "+236");
        prefix_list.put("CG", "+242");
        prefix_list.put("CH", "+41");
        prefix_list.put("CI", "+225");
        prefix_list.put("CK", "+682");
        prefix_list.put("CL", "+56");
        prefix_list.put("CM", "+237");
        prefix_list.put("CN", "+86");
        prefix_list.put("CO", "+57");
        prefix_list.put("CR", "+506");
        prefix_list.put("CS", "+381");
        prefix_list.put("CU", "+53");
        prefix_list.put("CV", "+238");
        prefix_list.put("CX", "+61");
        prefix_list.put("CY", "+90-392");
        prefix_list.put("CY", "+357");
        prefix_list.put("CZ", "+420");
        prefix_list.put("DE", "+49");
        prefix_list.put("DJ", "+253");
        prefix_list.put("DK", "+45");
        prefix_list.put("DM", "+1-767");
        prefix_list.put("DO", "+1-809"); // and 1-829?
        prefix_list.put("DZ", "+213");
        prefix_list.put("EC", "+593");
        prefix_list.put("EE", "+372");
        prefix_list.put("EG", "+20");
        prefix_list.put("EH", "+212");
        prefix_list.put("ER", "+291");
        prefix_list.put("ES", "+34");
        prefix_list.put("ET", "+251");
        prefix_list.put("FI", "+358");
        prefix_list.put("FJ", "+679");
        prefix_list.put("FK", "+500");
        prefix_list.put("FM", "+691");
        prefix_list.put("FO", "+298");
        prefix_list.put("FR", "+33");
        prefix_list.put("GA", "+241");
        prefix_list.put("GB", "+44");
        prefix_list.put("GD", "+1-473");
        prefix_list.put("GE", "+995");
        prefix_list.put("GF", "+594");
        prefix_list.put("GG", "+44");
        prefix_list.put("GH", "+233");
        prefix_list.put("GI", "+350");
        prefix_list.put("GL", "+299");
        prefix_list.put("GM", "+220");
        prefix_list.put("GN", "+224");
        prefix_list.put("GP", "+590");
        prefix_list.put("GQ", "+240");
        prefix_list.put("GR", "+30");
        prefix_list.put("GT", "+502");
        prefix_list.put("GU", "+1-671");
        prefix_list.put("GW", "+245");
        prefix_list.put("GY", "+592");
        prefix_list.put("HK", "+852");
        prefix_list.put("HN", "+504");
        prefix_list.put("HR", "+385");
        prefix_list.put("HT", "+509");
        prefix_list.put("HU", "+36");
        prefix_list.put("ID", "+62");
        prefix_list.put("IE", "+353");
        prefix_list.put("IL", "+972");
        prefix_list.put("IM", "+44");
        prefix_list.put("IN", "+91");
        prefix_list.put("IO", "+246");
        prefix_list.put("IQ", "+964");
        prefix_list.put("IR", "+98");
        prefix_list.put("IS", "+354");
        prefix_list.put("IT", "+39");
        prefix_list.put("JE", "+44");
        prefix_list.put("JM", "+1-876");
        prefix_list.put("JO", "+962");
        prefix_list.put("JP", "+81");
        prefix_list.put("KE", "+254");
        prefix_list.put("KG", "+996");
        prefix_list.put("KH", "+855");
        prefix_list.put("KI", "+686");
        prefix_list.put("KM", "+269");
        prefix_list.put("KN", "+1-869");
        prefix_list.put("KP", "+850");
        prefix_list.put("KR", "+82");
        prefix_list.put("KW", "+965");
        prefix_list.put("KY", "+1-345");
        prefix_list.put("KZ", "+7");
        prefix_list.put("LA", "+856");
        prefix_list.put("LB", "+961");
        prefix_list.put("LC", "+1-758");
        prefix_list.put("LI", "+423");
        prefix_list.put("LK", "+94");
        prefix_list.put("LR", "+231");
        prefix_list.put("LS", "+266");
        prefix_list.put("LT", "+370");
        prefix_list.put("LU", "+352");
        prefix_list.put("LV", "+371");
        prefix_list.put("LY", "+218");
        prefix_list.put("MA", "+212");
        prefix_list.put("MC", "+377");
        prefix_list.put("MD", "+373-533");
        prefix_list.put("MD", "+373");
        prefix_list.put("ME", "+382");
        prefix_list.put("MG", "+261");
        prefix_list.put("MH", "+692");
        prefix_list.put("MK", "+389");
        prefix_list.put("ML", "+223");
        prefix_list.put("MM", "+95");
        prefix_list.put("MN", "+976");
        prefix_list.put("MO", "+853");
        prefix_list.put("MP", "+1-670");
        prefix_list.put("MQ", "+596");
        prefix_list.put("MR", "+222");
        prefix_list.put("MS", "+1-664");
        prefix_list.put("MT", "+356");
        prefix_list.put("MU", "+230");
        prefix_list.put("MV", "+960");
        prefix_list.put("MW", "+265");
        prefix_list.put("MX", "+52");
        prefix_list.put("MY", "+60");
        prefix_list.put("MZ", "+258");
        prefix_list.put("NA", "+264");
        prefix_list.put("NC", "+687");
        prefix_list.put("NE", "+227");
        prefix_list.put("NF", "+672");
        prefix_list.put("NG", "+234");
        prefix_list.put("NI", "+505");
        prefix_list.put("NL", "+31");
        prefix_list.put("NO", "+47");
        prefix_list.put("NP", "+977");
        prefix_list.put("NR", "+674");
        prefix_list.put("NU", "+683");
        prefix_list.put("NZ", "+64");
        prefix_list.put("OM", "+968");
        prefix_list.put("PA", "+507");
        prefix_list.put("PE", "+51");
        prefix_list.put("PF", "+689");
        prefix_list.put("PG", "+675");
        prefix_list.put("PH", "+63");
        prefix_list.put("PK", "+92");
        prefix_list.put("PL", "+48");
        prefix_list.put("PM", "+508");
        prefix_list.put("PR", "+1-787");
        prefix_list.put("PS", "+970");
        prefix_list.put("PT", "+351");
        prefix_list.put("PW", "+680");
        prefix_list.put("PY", "+595");
        prefix_list.put("QA", "+974");
        prefix_list.put("RE", "+262");
        prefix_list.put("RO", "+40");
        prefix_list.put("RS", "+381");
        prefix_list.put("RU", "+7");
        prefix_list.put("RW", "+250");
        prefix_list.put("SA", "+966");
        prefix_list.put("SB", "+677");
        prefix_list.put("SC", "+248");
        prefix_list.put("SD", "+249");
        prefix_list.put("SE", "+46");
        prefix_list.put("SG", "+65");
        prefix_list.put("SH", "+290");
        prefix_list.put("SI", "+386");
        prefix_list.put("SJ", "+47");
        prefix_list.put("SK", "+421");
        prefix_list.put("SL", "+232");
        prefix_list.put("SM", "+378");
        prefix_list.put("SN", "+221");
        prefix_list.put("SO", "+252");
        prefix_list.put("SO", "+252");
        prefix_list.put("SR", "+597");
        prefix_list.put("ST", "+239");
        prefix_list.put("SV", "+503");
        prefix_list.put("SY", "+963");
        prefix_list.put("SZ", "+268");
        prefix_list.put("TA", "+290");
        prefix_list.put("TC", "+1-649");
        prefix_list.put("TD", "+235");
        prefix_list.put("TG", "+228");
        prefix_list.put("TH", "+66");
        prefix_list.put("TJ", "+992");
        prefix_list.put("TK", "+690");
        prefix_list.put("TL", "+670");
        prefix_list.put("TM", "+993");
        prefix_list.put("TN", "+216");
        prefix_list.put("TO", "+676");
        prefix_list.put("TR", "+90");
        prefix_list.put("TT", "+1-868");
        prefix_list.put("TV", "+688");
        prefix_list.put("TW", "+886");
        prefix_list.put("TZ", "+255");
        prefix_list.put("UA", "+380");
        prefix_list.put("UG", "+256");
        prefix_list.put("US", "+1");
        prefix_list.put("UY", "+598");
        prefix_list.put("UZ", "+998");
        prefix_list.put("VA", "+379");
        prefix_list.put("VC", "+1-784");
        prefix_list.put("VE", "+58");
        prefix_list.put("VG", "+1-284");
        prefix_list.put("VI", "+1-340");
        prefix_list.put("VN", "+84");
        prefix_list.put("VU", "+678");
        prefix_list.put("WF", "+681");
        prefix_list.put("WS", "+685");
        prefix_list.put("YE", "+967");
        prefix_list.put("YT", "+262");
        prefix_list.put("ZA", "+27");
        prefix_list.put("ZM", "+260");
        prefix_list.put("ZW", "+263")

        val tele = activity!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phone_textbox.setText(prefix_list.get(tele.networkCountryIso.toUpperCase()))

        start_btn.setOnClickListener {

            it.hideKeyboard()

            phone_textbox.clearFocus()
            verify_textbox.clearFocus()

            if (validatePhoneNumber()) {

                var found: Boolean = false

                prefix_list.forEach { _, s2 ->

                    if (phone_textbox.text.startsWith(s2)) {
                        found = true
                    }

                }

                if (!found) {

                    phone_textbox.setText(prefix_list.get(tele.networkCountryIso.toUpperCase()) + phone_textbox.text.toString())

                }

                startPhoneNumberVerification(phone_textbox.text.toString())

            }

        }

        verify_btn.setOnClickListener {

            it.hideKeyboard()

            phone_textbox.clearFocus()
            verify_textbox.clearFocus()

            val code = verify_textbox.text.toString()

            if (TextUtils.isEmpty(code)) {
                verify_textbox.error = getString(R.string.invalid_code_string)

            } else {

                verifyPhoneNumberWithCode(storedVerificationId, code)

            }

        }

        resend_btn.setOnClickListener{

            it.hideKeyboard()

            phone_textbox.clearFocus()
            verify_textbox.clearFocus()

            resendVerificationCode(phone_textbox.text.toString(), resendToken)

        }

        info_btn.setOnClickListener {

            it.hideKeyboard()

            phone_textbox.clearFocus()
            verify_textbox.clearFocus()

            val builder =
                AlertDialog.Builder(this.requireContext())
            builder.setTitle(getString(R.string.info_string))

            builder.setMessage(getString(R.string.info_long_string))
            builder.setPositiveButton("Ok", null)

            val dialog = builder.create()
            dialog.show()

        }

        view.setOnClickListener {

            it.hideKeyboard()

            phone_textbox.clearFocus()
            verify_textbox.clearFocus()

        }

    }

    override fun onStart() {

        super.onStart()

        val currentUser = auth.currentUser
        updateUI(currentUser)

        if (verificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phone_textbox.text.toString())
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress)

    }

    private fun startPhoneNumberVerification(phoneNumber: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            120,
            TimeUnit.SECONDS,
            this.activity!!,
            callbacks)

        verificationInProgress = true

    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)

        signInWithPhoneAuthCredential(credential)

    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            120,
            TimeUnit.SECONDS,
            this.activity!!,
            callbacks,
            token)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener (this.activity!!) { task ->

                if (task.isSuccessful) {

                    val user = task.result?.user
                    updateUI(STATE_SIGNIN_SUCCESS, user)

                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                        verify_textbox.error = getString(R.string.invalid_code_string)

                    }

                    updateUI(STATE_SIGNIN_FAILED)

                }

            }

    }

    private fun signOut() {

        Toast.makeText(this.context, getString(R.string.signed_out_string), Toast.LENGTH_LONG).show()

        auth.signOut()
        updateUI(STATE_INITIALIZED)

    }

    private fun updateUI(user: FirebaseUser?) {

        if (user != null) {

            updateUI(STATE_SIGNIN_SUCCESS, user)

        } else {

            updateUI(STATE_INITIALIZED)

        }

    }

    private fun updateUI(uiState: Int, cred: PhoneAuthCredential) {

        updateUI(uiState, null, cred)

    }

    private fun View.hideKeyboard() {

        val inputMethodManager = context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)

    }

    private fun updateUI(
        uiState: Int,
        user: FirebaseUser? = auth.currentUser,
        cred: PhoneAuthCredential? = null
    ) {

        when (uiState) {

            STATE_INITIALIZED -> {

                enableViews(start_btn, phone_textbox, status_label)
                disableViews(verify_btn, resend_btn, verify_textbox, write_code_label)

            }

            STATE_CODE_SENT -> {

                enableViews(verify_btn, resend_btn, verify_textbox, write_code_label)
                disableViews(start_btn, status_label, phone_textbox)

                Toast.makeText(this.context, getString(R.string.code_sent_string), Toast.LENGTH_SHORT).show()

            }

            STATE_VERIFY_FAILED -> {

                Toast.makeText(this.context, getString(R.string.verification_failed_string), Toast.LENGTH_LONG).show()

            }

            STATE_VERIFY_SUCCESS -> {

                disableObject(start_btn, verify_btn, resend_btn, phone_textbox,
                    verify_textbox)

                Toast.makeText(this.context, getString(R.string.verification_succesful_string), Toast.LENGTH_SHORT).show()

                if (cred != null) {

                    if (cred.smsCode != null) {

                        verify_textbox.setText(cred.smsCode)

                    } else {

                        //verify_textbox.setText("")

                    }
                }
            }

            STATE_SIGNIN_FAILED ->

                Toast.makeText(this.context, getString(R.string.signed_in_failed_string), Toast.LENGTH_LONG).show()

            STATE_SIGNIN_SUCCESS -> {
            }

        }

        if (user != null) {

            enableViews(phone_textbox, verify_textbox)
            phone_textbox.text = null
            verify_textbox.text = null

            Snackbar.make(this.view!!, getString(R.string.signed_in_string), Snackbar.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_mainFragment_to_categoriesFragment)

        }

    }

    private fun validatePhoneNumber(): Boolean {

        val phoneNumber = phone_textbox.text.toString()

        if (TextUtils.isEmpty(phoneNumber)) {

            phone_textbox.error = getString(R.string.wrong_telephone_string)
            return false

        }

        return true
    }

    private fun enableViews(vararg views: View) {

        for (v in views) {

            v.visibility = View.VISIBLE

        }

    }

    private fun disableViews(vararg views: View) {

        for (v in views) {

            v.visibility = View.GONE

        }

    }

    private fun disableObject(vararg views: View) {

        for (v in views) {

            v.isEnabled = false

        }

    }

    companion object {

        private const val TAG = "PhoneAuthActivity"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val STATE_INITIALIZED = 1
        private const val STATE_VERIFY_FAILED = 3
        private const val STATE_VERIFY_SUCCESS = 4
        private const val STATE_CODE_SENT = 2
        private const val STATE_SIGNIN_FAILED = 5
        private const val STATE_SIGNIN_SUCCESS = 6

    }

}
