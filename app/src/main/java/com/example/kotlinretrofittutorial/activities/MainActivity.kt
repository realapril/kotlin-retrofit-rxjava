package com.example.kotlinretrofittutorial.activities


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.kotlinretrofittutorial.R
import com.example.kotlinretrofittutorial.api.RetrofitClient
import com.example.kotlinretrofittutorial.api.WikiApiService
import com.example.kotlinretrofittutorial.models.Models
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class MainActivity : BasicActivity() {

    var textViewResult: TextView? = null
//    var disposable: Disposable? = null //Redundant

    private fun beginSearchDetailVer(srsearch: String) {
        disposable = RetrofitClient.Instance_Wiki.hitCountWithResponseCode("query", "json", "search", srsearch)
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.isSuccessful) {
                                val res: Models.Result = result.body()
                                        ?: Models.Result(Models.Query(Models.SearchInfo(-1)))
                                result.body()?.let { showResult(res.query.searchinfo.totalhits) }
                                Log.i("TEST: ", "success " + result.code())
                            } else {
                                Log.i("TEST: ", "failed " + result.code())
                            }
                        },
                        { error -> //showError(error.message)
                            if(error is HttpException) {
                                Log.i("TEST Error: ", "${error.code()} exception.response.code : ${error.response().code()}")
                            }
                        }
                )
    }

    private fun showResult(totalhits: Int) {
        Log.d("Result hits: ", totalhits.toString())
        textViewResult?.setText(totalhits.toString())
    }

    private fun showError(message: String?) {
        Log.e("Error Msg:", message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonSearch = findViewById<Button>(R.id.buttonSearch)
        var editTextKeyword = findViewById<EditText>(R.id.editTextKeyword)
        textViewResult = findViewById<TextView>(R.id.textViewRes)

        buttonSearch.setOnClickListener {
            var searchKey = editTextKeyword.text.toString()


            beginSearchDetailVer(searchKey)
        }

    }

    override fun onPause() {
        super.onPause()
//        disposable?.dispose() //redundant
    }
}
