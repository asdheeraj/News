package com.dheeraj.news.util

interface EntityMapper<Entity, Model> {

    fun mapToEntity(model: Model) : Entity
}