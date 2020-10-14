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
import com.example.kotlinretrofittutorial.utils.RxBus
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

        //API Result- Get
        val key_get = "List"
        RxBus.instance.receiveEvent(key_get).subscribe {
            Log.e("onComplete", "$key_get = $it")
            val resList = it as EmployeeResList
            val Elist: List<Employee> = resList.data
            var Nlist: MutableList<String> = mutableListOf()
            for (i in 1 until Elist.size) {
                Nlist.add(Elist[i].employee_name ?: "No Name $i")
            }
            showMsg("SUCCESS RESULT : \nData Size: ${Elist.size} \nSample Names:  $Nlist")
        }.apply { disposables.add(this) }

        //API Result- Search
        val key_search = "Search"
        RxBus.instance.receiveEvent(key_search).subscribe {
            Log.e("onComplete", "$key_search = $it")
            val res = it as EmployeeRes
            showMsg("SUCCESS RESULT : " + res.toString())

        }.apply { disposables.add(this) }

        //API Result- Create
        val key_create = "Create"
        RxBus.instance.receiveEvent(key_create).subscribe {
            Log.e("onComplete", "$key_create = $it")
            showMsg("SUCCESS RESULT : $it")

        }.apply { disposables.add(this) }

        // API Result- Error
        val key_error = "ServerError"
        RxBus.instance.receiveEvent(key_error).subscribe {
            val error = it as Throwable
            Log.i("TEST Error: ", "${(error as HttpException).code()}")
            Log.i("TEST Error: ", "$error")
            showMsg("Error RESULT : ${error.message}")
        }.apply { disposables.add(this) }

    }

    private fun searchOne(id: Int) {
        DummyAPI_RB(BaseString).buildDisposable(id).apply { disposables.add(this) }
    }

    private fun getAll() {
        DummyAPI_RB(BaseString).buildDisposable().apply { disposables.add(this) }
    }

    private fun createNew(employeeData: EmployeeReq) {
        DummyAPI_RB(BaseString).buildDisposable(employeeData).apply { disposables.add(this) }
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
