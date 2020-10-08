package com.example.kotlinretrofittutorial.models


data class EmployeeRes(val status: String,
                       val data: List<Employee>
)

data class EmployeeRes2(val status: String,
                       val data: List<Employee2>
)

data class Employee(val id: Int,
                    val employee_name: String,
                    val employee_salary: String,
                    val profile_image: String
)

data class Employee2(val id: Int,
                    val name: String,
                    val salary: String,
                    val age: String
)


data class EmployeeReq(val name: String,
                    val salary: Int,
                    val age: Int
)