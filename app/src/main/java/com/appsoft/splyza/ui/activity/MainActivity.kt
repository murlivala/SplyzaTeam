package com.appsoft.splyza.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.appsoft.splyza.BuildConfig
import com.appsoft.splyza.R
import com.appsoft.splyza.ui.fragment.LandingFragment

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LandingFragment.newInstance())
                .commit()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!processBackPress()) {
            super.onBackPressed()
        }
    }

    private fun processBackPress():Boolean {
        if (BuildConfig.DEBUG) {
            Log.d(TAG,"onBackPressed ${supportFragmentManager.backStackEntryCount}")
        }
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return true
        } else {
            return false
        }
    }
}