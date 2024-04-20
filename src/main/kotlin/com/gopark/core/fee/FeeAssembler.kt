package com.gopark.core.fee

import com.gopark.core.model.SimpleValue
import com.gopark.core.vehicle_type.VehicleTypeResource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class FeeAssembler : SimpleRepresentationModelAssembler<FeeDTO> {

    override fun addLinks(entityModel: EntityModel<FeeDTO>) {
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeeResource::class.java).getFee(entityModel.content!!.feeId!!)).withSelfRel())
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeeResource::class.java).getAllFees()).withRel(IanaLinkRelations.COLLECTION))
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleTypeResource::class.java).getVehicleType(entityModel.content!!.vehicleType!!)).withRel("vehicleType"))
    }

    override fun addLinks(collectionModel: CollectionModel<EntityModel<FeeDTO>>) {
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeeResource::class.java).getAllFees()).withSelfRel())
    }

    fun toSimpleModel(feeId: Int): EntityModel<SimpleValue<Int>> {
        val simpleModel = SimpleValue.entityModelOf(feeId)
        simpleModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FeeResource::class.java).getFee(feeId)).withSelfRel())
        return simpleModel
    }

}
