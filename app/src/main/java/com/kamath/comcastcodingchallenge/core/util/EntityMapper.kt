package com.kamath.comcastcodingchallenge.core.util

interface EntityMapper<Entity, Domain> {
    fun toDomain(entity: Entity): Domain?
    fun toEntity(domain: Domain): Entity
}