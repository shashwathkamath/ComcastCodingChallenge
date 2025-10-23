package com.kamath.comcastcodingchallenge.core.util

import com.kamath.comcastcodingchallenge.animals.data.remote.dto.AnimalDto
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.model.Bird
import com.kamath.comcastcodingchallenge.animals.domain.model.Bug
import com.kamath.comcastcodingchallenge.animals.domain.model.Dog

fun AnimalDto.toDomain(): Animal? {
    val scientificName = taxonomy.scientificName ?: Constants.DEFAULT_VALUE
    val phylum = taxonomy.phylum ?: Constants.DEFAULT_VALUE
    val animalClass = taxonomy.animalClass ?: Constants.DEFAULT_VALUE
    val family = taxonomy.family ?: Constants.DEFAULT_VALUE

    return when {
        family.equals(Constants.FAMILY_CANIDAE, ignoreCase = true) -> Dog(
            name = name,
            phylum = phylum,
            scientificName = scientificName,
            slogan = characteristics.slogan ?: "",
            lifespan = characteristics.lifespan ?: ""
        )

        animalClass.equals(Constants.CLASS_AVES, ignoreCase = true) -> Bird(
            name = name,
            phylum = phylum,
            scientificName = scientificName,
            wingspan = characteristics.wingspan ?: "",
            habitat = characteristics.habitat ?: ""
        )

        phylum.equals(
            Constants.PHYLUM_ARTHROPODA,
            ignoreCase = true
        ) -> Bug(
            name = name,
            phylum = phylum,
            scientificName = scientificName,
            prey = characteristics.prey ?: "",
            predators = characteristics.predators ?: ""
        )

        else -> null
    }
}