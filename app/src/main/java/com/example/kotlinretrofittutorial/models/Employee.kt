package com.example.kotlinretrofittutorial.models


data class EmployeeResList(val status: String,
                           val data: List<Employee>
)

data class EmployeeRes(val status: String,
                       val data: Employee
)

data class Employee(val id: Int,
                    val employee_name: String?,
                    val employee_salary: Int?,
                    val employee_age: Int?,
                    val profile_image: String?
)

data class EmployeeReq(val name: String,
                    val salary: String,
                    val age: String
)

