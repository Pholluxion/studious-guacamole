package com.gopark.core.util

import java.util.ArrayList


class ReferencedWarning {

    var key: String? = null

    private var params: ArrayList<Any> = ArrayList()

    fun addParam(param: Any?) {
        params.add(param!!)
    }

    fun toMessage(): String {
        var message = key!!
        if (params.isNotEmpty()) {
            message += "," + params.joinToString(",")
        }
        return message
    }

}
