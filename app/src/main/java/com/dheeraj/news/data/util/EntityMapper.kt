package com.dheeraj.news.data.util

interface EntityMapper<Model, Entity> {

    fun mapFromEntity(entity: Entity) : Model
}