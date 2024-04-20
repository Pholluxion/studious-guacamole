package com.gopark.core.payment

import com.gopark.core.fee.FeeResource
import com.gopark.core.model.SimpleValue
import com.gopark.core.spot.SpotResource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class PaymentAssembler : SimpleRepresentationModelAssembler<PaymentDTO> {

    override fun addLinks(entityModel: EntityModel<PaymentDTO>) {
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PaymentResource::class.java).getPayment(entityModel.content!!.paymentId!!)).withSelfRel())
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PaymentResource::class.java).getAllPayments(null)).withRel(IanaLinkRelations.COLLECTION))
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SpotResource::class.java).getSpot(entityModel.content!!.spot!!)).withRel("spot"))
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeeResource::class.java).getFee(entityModel.content!!.fee!!)).withRel("fee"))
    }

    override fun addLinks(collectionModel: CollectionModel<EntityModel<PaymentDTO>>) {
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PaymentResource::class.java).getAllPayments(null)).withSelfRel())
    }

    fun toSimpleModel(paymentId: Int): EntityModel<SimpleValue<Int>> {
        val simpleModel = SimpleValue.entityModelOf(paymentId)
        simpleModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PaymentResource::class.java).getPayment(paymentId)).withSelfRel())
        return simpleModel
    }

}
