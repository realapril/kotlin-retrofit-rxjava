package com.example.kotlinretrofittutorial.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kotlinretrofittutorial.R
import com.example.kotlinretrofittutorial.api.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dynamic.*


class DynamicActivity : BasicActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)

        button_req.setOnClickListener {
            val baseUrl = edit_url.text.toString()
            RetrofitClient.setFullURL(baseUrl)
            RetrofitClient.Instance_Dynamic.getAnyInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                Log.i("TEST: ", "success " + result)
                                showMsg(result.toString())
                            },
                            { error ->
                                Log.e("TEST: ", error.toString())
                                showMsg(error.toString())
                            }
                    ).apply { disposables.add(this) }

            edit_url.setText("https://")
        }
    }

    fun showMsg(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("AlertDialog Title")
        builder.setMessage(msg)
        builder.show();
    }
}
