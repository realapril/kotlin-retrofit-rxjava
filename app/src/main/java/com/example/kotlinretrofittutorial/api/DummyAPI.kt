package com.example.kotlinretrofittutorial.api

import com.example.kotlinretrofittutorial.models.EmployeeReq
import com.example.kotlinretrofittutorial.models.EmployeeResList
import com.example.kotlinretrofittutorial.models.EmployeeRes
import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DummyAPI {

    @GET("/api/v1/employees")
    fun getEmployees(
    ): Maybe<Response<EmployeeResList>>

    @GET("/api/v1/employee/{id}")
    fun searchEmployee(
        @Path("id") id: Int
    ): Maybe<Response<EmployeeRes>>

    @POST("/api/v1/create")
    fun createEmployee(
       @Body employeeReq: EmployeeReq
    ): Maybe<Response<EmployeeRes>>

}