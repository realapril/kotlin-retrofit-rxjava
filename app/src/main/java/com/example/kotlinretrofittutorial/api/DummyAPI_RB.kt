package com.example.kotlinretrofittutorial.api

import com.example.kotlinretrofittutorial.models.EmployeeReq
import com.example.kotlinretrofittutorial.models.EmployeeRes
import com.example.kotlinretrofittutorial.models.EmployeeResList
import io.reactivex.Maybe
import retrofit2.Response


class DummyAPI_RB(var url: String){

    //Instance리턴============================================================================
    private fun setRetrofitClient_auth(): DummyAPI {
        RetrofitClient.setFullURL(url)
        return RetrofitClient.INS_API("DummyAPI") as DummyAPI
    }

    //Maybe리턴===============================================================================
    fun buildUseCaseMaybe(): Maybe<Response<EmployeeResList>> {
        return setRetrofitClient_auth().getEmployees()
    }

    fun buildUseCaseMaybe(id: Int): Maybe<Response<EmployeeRes>> {
        return setRetrofitClient_auth().searchEmployee(id)
    }

    fun buildUseCaseMaybe(employeeReq: EmployeeReq):  Maybe<Response<EmployeeRes>> {
        return setRetrofitClient_auth().createEmployee(employeeReq)
    }
}