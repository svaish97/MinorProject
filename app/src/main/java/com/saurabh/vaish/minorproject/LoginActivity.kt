package com.saurabh.vaish.minorproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    lateinit var phoneNo:String
    lateinit var mcallbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var verificationId:String
    lateinit var mAuth:FirebaseAuth

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar2 as Toolbar?)

        mAuth=FirebaseAuth.getInstance()

        mcallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {

            }

            override fun onVerificationFailed(p0: FirebaseException?) {
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                verificationId=p0!!


            }
        }



        SignIn.setOnClickListener {
            if(phoneNumber.text.toString().isEmpty() )
            {
                Toast.makeText(this,"Please Enter a Valid Phone Number",Toast.LENGTH_SHORT)
                        .show()
            }
            else{
                phoneNo = "+91"+phoneNumber.text.toString()
                otp.visibility=View.VISIBLE
                SignIn.visibility=View.GONE
                Verify.visibility=View.VISIBLE
                getOTP()
            }

        }

        Verify.setOnClickListener {
            if(otp.text.toString().isEmpty())
            {
                Toast.makeText(this,"Please Enter a the OTP send to your phone",
                        Toast.LENGTH_SHORT)
                        .show()
            }
            else{
                verifycode(verificationId,otp.text.toString())
            }
        }



    }

    private fun verifycode(verificationId: String, otp: String) {
        signInWithPhoneCredential(PhoneAuthProvider.getCredential(verificationId,otp))
    }

    private fun signInWithPhoneCredential(credential: PhoneAuthCredential?) {
        mAuth.signInWithCredential(credential!!).addOnCompleteListener{
            if(it.isSuccessful){
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish()
            }
            else
            {
                Toast.makeText(this@LoginActivity,"Something went wrong",Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }


    private fun getOTP() {
       PhoneAuthProvider.getInstance().verifyPhoneNumber(
               phoneNo,60,TimeUnit.SECONDS,this,mcallbacks
       )

    }
}
