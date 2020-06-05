package com.example.kotlinretrofittutorial.activities


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.kotlinretrofittutorial.R
import com.example.kotlinretrofittutorial.api.WikiApiService
import com.example.kotlinretrofittutorial.models.Models
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    var textViewResult : TextView? = null
    val wikiApiServe by lazy {
        WikiApiService.create()
    }

    var disposable: Disposable? = null

    private fun beginSearch(srsearch: String) {
        disposable =
                wikiApiServe.hitCountCheck("query", "json", "search", srsearch)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> showResult(result.query.searchinfo.totalhits) },
                                { error -> showError(error.message) }
                        )
    }

    private fun beginSearchDetailVer(srsearch: String) {
        disposable =
                wikiApiServe.hitCountWithResponseCode("query", "json", "search", srsearch)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result ->
                                    if(result.isSuccessful){
                                        val res:Models.Result = result.body()?:Models.Result(Models.Query(Models.SearchInfo(-1)))
                                        result.body()?.let {  showResult(res.query.searchinfo.totalhits) }
                                        Log.i("TEST: ", "success "+result.code())
                                    }else{
                                        Log.i("TEST: ", "failed "+result.code())
                                    }
                                },
                                { error -> showError(error.message) }
                        )
    }

    private  fun showResult(totalhits: Int){
        Log.d("Result hits: ", totalhits.toString())
        textViewResult?.setText(totalhits.toString())
    }

    private fun showError(message: String?){
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

            //beginSearch(searchKey)
            beginSearchDetailVer(searchKey)
        }

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
