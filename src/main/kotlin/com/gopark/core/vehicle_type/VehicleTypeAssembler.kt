package com.gopark.core.vehicle_type

import com.gopark.core.model.SimpleValue
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class VehicleTypeAssembler : SimpleRepresentationModelAssembler<VehicleTypeDTO> {

    override fun addLinks(entityModel: EntityModel<VehicleTypeDTO>) {
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleTypeResource::class.java).getVehicleType(entityModel.content!!.vehicleTypeId!!)).withSelfRel())
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleTypeResource::class.java).getAllVehicleTypes()).withRel(IanaLinkRelations.COLLECTION))
    }

    override fun addLinks(collectionModel: CollectionModel<EntityModel<VehicleTypeDTO>>) {
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleTypeResource::class.java).getAllVehicleTypes()).withSelfRel())
    }

    fun toSimpleModel(vehicleTypeId: Int): EntityModel<SimpleValue<Int>> {
        val simpleModel = SimpleValue.entityModelOf(vehicleTypeId)
        simpleModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleTypeResource::class.java).getVehicleType(vehicleTypeId)).withSelfRel())
        return simpleModel
    }

}
