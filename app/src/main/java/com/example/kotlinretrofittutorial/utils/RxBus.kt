package com.example.kotlinretrofittutorial.utils

import io.reactivex.subjects.PublishSubject
import java.util.*

object RxBus {

    val instance = RxBus
    private val subjectTable = Hashtable<String, PublishSubject<Any>>()

    fun sendEvent(any: Any, key: String = "RxBus") {
        subjectTable[key]?.onNext(any)
    }

    fun receiveEvent(key: String = "RxBus"): PublishSubject<Any> {
        synchronized(this) {
            if (subjectTable.containsKey(key).not()) {
                subjectTable[key] = PublishSubject.create()
            }
            return subjectTable[key]!!
        }
    }
}