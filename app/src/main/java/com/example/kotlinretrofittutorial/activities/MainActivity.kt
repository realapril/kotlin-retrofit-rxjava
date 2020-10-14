package com.example.kotlinretrofittutorial.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.kotlinretrofittutorial.R
import com.example.kotlinretrofittutorial.api.DummyAPI_RB
import com.example.kotlinretrofittutorial.api.RetrofitClient
import com.example.kotlinretrofittutorial.models.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException

class MainActivity : BasicActivity() {

    val BaseString = "http://dummy.restapiexample.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //search one employee
        btn_getOne.setOnClickListener {
            searchOne(editTextKeyword.text.toString().toInt())
        }

        //search all employees
        btn_getAll.setOnClickListener {
            getAll()
        }

        //create new employee
        btn_create.setOnClickListener {
            val employeeData = EmployeeReq(edit_name.text.toString(), edit_salary.text.toString(), edit_age.text.toString())
            createNew(employeeData)
        }

        button_dynamic.setOnClickListener {
            val intent = Intent(this@MainActivity, DynamicActivity::class.java)
            startActivity(intent)
        }

    }

    private fun searchOne(id: Int) {
        DummyAPI_RB(BaseString).buildUseCaseMaybe(id)
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.i("TEST: ", "success " + result.toString())
                            val res: EmployeeRes = result.body()!!
                            showMsg("SUCCESS RESULT : " + res.toString())

                        }
                        ,
                        { error -> //showError(error.message)

                            Log.i("TEST Error: ", "${(error as HttpException).code()}")
                            Log.i("TEST Error: ", "$error")
                            showMsg("Error RESULT : ${error.message}" )
                        },
                        {
                            showMsg("No Data RESULT" )
                        }
                ).apply { disposables.add(this) }
    }

    private fun getAll() {
        DummyAPI_RB(BaseString).buildUseCaseMaybe()
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            val resList: EmployeeResList = result.body()!!
                            val Elist: List<Employee> = resList.data
                            var Nlist: MutableList<String> = mutableListOf()
                            for (i in 1 until Elist.size){
                                Nlist.add(Elist[i].employee_name?:"No Name $i")
                            }
                            showMsg("SUCCESS RESULT : \nData Size: ${Elist.size} \nSample Names:  ${Nlist}" )

                        },
                        { error ->
                            Log.i("TEST Error: ", "${(error as HttpException).code()}")
                            Log.i("TEST Error: ", "$error")
                            showMsg("Error RESULT : ${error.message}" )
                        }
                ).apply { disposables.add(this) }
    }

    private fun createNew(employeeData: EmployeeReq) {
        DummyAPI_RB(BaseString).buildUseCaseMaybe(employeeData)
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.isSuccessful) {
                                val res: EmployeeRes = result.body()!!
                                showMsg("SUCCESS RESULT : " + res.toString())
                                Log.i("TEST: ", "success " + result.toString())
                            } else {
                                Log.i("TEST: ", "failed " + result.code())
                            }
                        },
                        { error -> //showError(error.message)
                            Log.i("TEST Error: ", "${(error as HttpException).code()}")
                            Log.i("TEST Error: ", "$error")
                            showMsg("Error RESULT : ${error.message}" )
                        }
                ).apply { disposables.add(this) }
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
