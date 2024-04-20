package com.gopark.core.fee

import com.gopark.core.util.ReferencedWarning


interface FeeService {

    fun findAll(): List<FeeDTO>

    fun get(feeId: Int): FeeDTO

    fun create(feeDTO: FeeDTO): Int

    fun update(feeId: Int, feeDTO: FeeDTO)

    fun delete(feeId: Int)

    fun getReferencedWarning(feeId: Int): ReferencedWarning?

}
