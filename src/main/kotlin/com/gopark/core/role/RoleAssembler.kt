package com.gopark.core.role

import com.gopark.core.model.SimpleValue
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class RoleAssembler : SimpleRepresentationModelAssembler<RoleDTO> {

    override fun addLinks(entityModel: EntityModel<RoleDTO>) {
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleResource::class.java).getRole(entityModel.content!!.id!!)).withSelfRel())
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleResource::class.java).getAllRoles()).withRel(IanaLinkRelations.COLLECTION))
    }

    override fun addLinks(collectionModel: CollectionModel<EntityModel<RoleDTO>>) {
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleResource::class.java).getAllRoles()).withSelfRel())
    }

    fun toSimpleModel(id: Long): EntityModel<SimpleValue<Long>> {
        val simpleModel = SimpleValue.entityModelOf(id)
        simpleModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleResource::class.java).getRole(id)).withSelfRel())
        return simpleModel
    }

}
