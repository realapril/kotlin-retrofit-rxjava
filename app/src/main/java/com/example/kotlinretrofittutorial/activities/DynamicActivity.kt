package com.example.kotlinretrofittutorial.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinretrofittutorial.R
import com.example.kotlinretrofittutorial.api.RetrofitClient
import com.example.kotlinretrofittutorial.models.Models
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_dynamic.*
import retrofit2.HttpException

class DynamicActivity : BasicActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)

        button_req.setOnClickListener {
            val baseUrl = edit_url.text.toString()
            RetrofitClient.setFullURL(baseUrl)
            disposable = RetrofitClient.Instance_Dynamic().getAnyInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                Log.i("TEST: ", "success " + result)
                                showMsg(result)
                            },
                            { error ->
                                Log.e("TEST: ", error.toString())
                                showMsg(error.toString())
                            }
                    )

            edit_url.setText("https://")
        }
    }

    fun showMsg(msg: String) {
        //1
        var builder = AlertDialog.Builder(this)
        //2
        builder.setTitle("Result")
        builder.setMessage(msg)
                .setPositiveButton("ok",
                        DialogInterface.OnClickListener { dialog, id ->

                        }).show()
    }
}
