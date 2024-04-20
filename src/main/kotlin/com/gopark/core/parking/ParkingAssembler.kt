package com.gopark.core.parking

import com.gopark.core.model.SimpleValue
import com.gopark.core.user.UserResource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class ParkingAssembler : SimpleRepresentationModelAssembler<ParkingDTO> {

    override fun addLinks(entityModel: EntityModel<ParkingDTO>) {
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ParkingResource::class.java).getParking(entityModel.content!!.parkingId!!)).withSelfRel())
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ParkingResource::class.java).getAllParkings(null)).withRel(IanaLinkRelations.COLLECTION))
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserResource::class.java).getUser(entityModel.content!!.owner!!)).withRel("owner"))
    }

    override fun addLinks(collectionModel: CollectionModel<EntityModel<ParkingDTO>>) {
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ParkingResource::class.java).getAllParkings(null)).withSelfRel())
    }

    fun toSimpleModel(parkingId: Int): EntityModel<SimpleValue<Int>> {
        val simpleModel = SimpleValue.entityModelOf(parkingId)
        simpleModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ParkingResource::class.java).getParking(parkingId)).withSelfRel())
        return simpleModel
    }

}
