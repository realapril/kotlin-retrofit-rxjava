package com.example.kotlinretrofittutorial.activities

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable


abstract class BasicActivity : AppCompatActivity() {

    protected val disposables by lazy {
        CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}