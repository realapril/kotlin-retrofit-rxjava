package com.example.kotlinretrofittutorial.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.kotlinretrofittutorial.R
import com.example.kotlinretrofittutorial.api.RetrofitClient
import com.example.kotlinretrofittutorial.models.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException

class MainActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //search one employee
        btn_getOne.setOnClickListener {
            searchOne(editTextKeyword.text.toString().toInt())
        }

        //search all employees
        btn_getAll.setOnClickListener {
            searchAll()
        }

        //create new employee
        btn_create.setOnClickListener {
            val employeeData = EmployeeReq(edit_name.text.toString(), edit_salary.text.toString().toInt(), edit_age.text.toString().toInt())
            createNew(employeeData)
        }

        button_dynamic.setOnClickListener{
            val intent = Intent(this@MainActivity, DynamicActivity::class.java)
            startActivity(intent)
        }

    }

    private fun searchOne(id : Int) {
        disposable = RetrofitClient.Instance_Dummy.searchEmployee(id)
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.isSuccessful) {
                                val res: EmployeeRes = result.body()!!
                                showMsg( "SUCCESS RESULT : " + res.toString())
                                Log.i("TEST: ", "success " + result.code())
                            } else {
                                Log.i("TEST: ", "failed " + result.code())
                            }
                        },
                        { error -> //showError(error.message)
                            if(error is HttpException) {
                                Log.i("TEST Error: ", "${error.code()} exception.response.code : ${error.response()?.code()}")
                            }
                        }
                )
    }

    private fun searchAll() {
        disposable = RetrofitClient.Instance_Dummy.getEmployees()
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.isSuccessful) {
                                val res: EmployeeRes = result.body()!!
                                showMsg( "SUCCESS RESULT : " + res.toString())
                                Log.i("TEST: ", "success " + result.code())
                            } else {
                                Log.i("TEST: ", "failed " + result.code())
                            }
                        },
                        { error -> //showError(error.message)
                            if(error is HttpException) {
                                Log.i("TEST Error: ", "${error.code()} exception.response.code : ${error.response()?.code()}")
                            }
                        }
                )
    }

    private fun createNew(employeeData : EmployeeReq) {
        disposable = RetrofitClient.Instance_Dummy.createEmployee(employeeData)
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.isSuccessful) {
                                val res: EmployeeRes2 = result.body()!!
                                showMsg( "SUCCESS RESULT : " + res.toString())
                                Log.i("TEST: ", "success " + result.code())
                            } else {
                                Log.i("TEST: ", "failed " + result.code())
                            }
                        },
                        { error -> //showError(error.message)
                            if(error is HttpException) {
                                Log.i("TEST Error: ", "${error.code()} exception.response.code : ${error.response()?.code()}")
                            }
                        }
                )
    }

    fun showMsg(msg: String) {
        //1
        AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(msg)
                .setPositiveButton("ok",
                        DialogInterface.OnClickListener { dialog, id ->
                        }).show()
    }

}
