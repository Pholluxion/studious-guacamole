package com.gopark.core.user

import com.gopark.core.model.SimpleValue
import com.gopark.core.role.RoleResource
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component


@Component
class UserAssembler : SimpleRepresentationModelAssembler<UserDTO> {

    override fun addLinks(entityModel: EntityModel<UserDTO>) {
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserResource::class.java).getUser(entityModel.content!!.id!!)).withSelfRel())
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserResource::class.java).getAllUsers(null)).withRel(IanaLinkRelations.COLLECTION))
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoleResource::class.java).getRole(entityModel.content!!.role!!)).withRel("role"))
    }

    override fun addLinks(collectionModel: CollectionModel<EntityModel<UserDTO>>) {
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserResource::class.java).getAllUsers(null)).withSelfRel())
    }

    fun toSimpleModel(id: Long): EntityModel<SimpleValue<Long>> {
        val simpleModel = SimpleValue.entityModelOf(id)
        simpleModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserResource::class.java).getUser(id)).withSelfRel())
        return simpleModel
    }

}
