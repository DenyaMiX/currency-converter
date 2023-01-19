package com.federicocotogno.currencyconversionapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.federicocotogno.currencyconversionapi.fragments.ConvertorFragment
import com.federicocotogno.currencyconversionapi.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException
import java.net.URL
import kotlin.Exception

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val convertorFragment = ConvertorFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        bottom_nav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_list -> replaceFragment(convertorFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}