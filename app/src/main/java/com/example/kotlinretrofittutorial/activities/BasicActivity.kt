package com.example.kotlinretrofittutorial.activities

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.Disposable


abstract class BasicActivity : AppCompatActivity() {
    var disposable: Disposable? = null


    override fun onPause() {
        super.onPause()
        if (disposable != null) disposable!!.dispose()
    }
}