package com.example.kotlinretrofittutorial.api

import android.util.Log
import com.example.kotlinretrofittutorial.models.EmployeeReq
import com.example.kotlinretrofittutorial.models.EmployeeRes
import com.example.kotlinretrofittutorial.models.EmployeeResList
import com.example.kotlinretrofittutorial.utils.RxBus
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response


class DummyAPI_RB(var url: String) {

    //Instance ============================================================================
    private fun setRetrofitClient_auth(): DummyAPI {
        RetrofitClient.setFullURL(url)
        return RetrofitClient.INS_API("DummyAPI") as DummyAPI
    }

    //Maybe ===============================================================================
    private fun buildUseCaseMaybe(): Maybe<Response<EmployeeResList>> {
        return setRetrofitClient_auth().getEmployees()
    }

    private fun buildUseCaseMaybe(id: Int): Maybe<Response<EmployeeRes>> {
        return setRetrofitClient_auth().searchEmployee(id)
    }

    private fun buildUseCaseMaybe(employeeReq: EmployeeReq): Maybe<Response<EmployeeRes>> {
        return setRetrofitClient_auth().createEmployee(employeeReq)
    }


    //Result Event =========================================================================
    //Get Employee
    fun buildDisposable(): Disposable {
        return buildUseCaseMaybe()
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            val res = result.body()!!
                            Log.e("성공", res.toString())
                            RxBus.instance.sendEvent(res, "List")
                        },
                        { error ->
                            Log.e("실패", error.toString())
                            RxBus.instance.sendEvent(error, "ServerError")
                        }
                )
    }

    //Search Employee
    fun buildDisposable(id: Int): Disposable {
        return buildUseCaseMaybe(id)
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            val res = result.body()!!
                            Log.e("성공", res.toString())
                            RxBus.instance.sendEvent(res, "Search")
                        },
                        { error ->
                            Log.e("실패", error.toString())
                            RxBus.instance.sendEvent(error, "ServerError")
                        }
                )
    }

    //Create Employee
    fun buildDisposable(employeeReq: EmployeeReq): Disposable {
        return buildUseCaseMaybe(employeeReq)
                .subscribeOn(Schedulers.io())
                .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            val res = result.body()!!
                            Log.e("성공", res.toString())
                            RxBus.instance.sendEvent(res, "Create")
                        },
                        { error ->
                            Log.e("실패", error.toString())
                            RxBus.instance.sendEvent(error, "ServerError")
                        }
                )
    }
}