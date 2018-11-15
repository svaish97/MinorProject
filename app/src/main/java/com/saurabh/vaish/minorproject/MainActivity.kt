package com.saurabh.vaish.minorproject

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.saurabh.vaish.minorproject.Fragments.LeafClassifier
import com.saurabh.vaish.minorproject.Fragments.MSP
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragmentManager=supportFragmentManager
                val leafDisease=fragmentManager.beginTransaction()
                leafDisease.replace(R.id.fragment_container, com.saurabh.vaish.minorproject.Fragments.leafDisease())
                leafDisease.commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val fragmentManager=supportFragmentManager
                val leafClassify=fragmentManager.beginTransaction()
                leafClassify.replace(R.id.fragment_container, LeafClassifier())
                leafClassify.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {

                val fragmentManager=supportFragmentManager
                val msp=fragmentManager.beginTransaction()
                msp.replace(R.id.fragment_container, MSP())
                msp.commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as Toolbar?)
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                100)

        val fragmentManager=supportFragmentManager
        val leafDisease=fragmentManager.beginTransaction()
        leafDisease.replace(R.id.fragment_container, com.saurabh.vaish.minorproject.Fragments.leafDisease())
        leafDisease.commit()



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            menuInflater.inflate(R.menu.signout,menu)

        }
        else
        {
            menuInflater.inflate(R.menu.signin, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        //For Sign In menu
        if(FirebaseAuth.getInstance().currentUser!=null){
         FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, MainActivity::class.java))
            finish()
        }
        else {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
            return true
    }

}
