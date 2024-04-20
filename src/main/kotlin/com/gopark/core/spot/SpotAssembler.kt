package com.gopark.core.spot

import com.gopark.core.model.SimpleValue
import com.gopark.core.parking.ParkingResource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class SpotAssembler : SimpleRepresentationModelAssembler<SpotDTO> {

    override fun addLinks(entityModel: EntityModel<SpotDTO>) {
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SpotResource::class.java).getSpot(entityModel.content!!.spotId!!)).withSelfRel())
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SpotResource::class.java).getAllSpots(null)).withRel(IanaLinkRelations.COLLECTION))
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ParkingResource::class.java).getParking(entityModel.content!!.parking!!)).withRel("parking"))
    }

    override fun addLinks(collectionModel: CollectionModel<EntityModel<SpotDTO>>) {
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SpotResource::class.java).getAllSpots(null)).withSelfRel())
    }

    fun toSimpleModel(spotId: Int): EntityModel<SimpleValue<Int>> {
        val simpleModel = SimpleValue.entityModelOf(spotId)
        simpleModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SpotResource::class.java).getSpot(spotId)).withSelfRel())
        return simpleModel
    }

}
