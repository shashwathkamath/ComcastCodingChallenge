package com.kamath.comcastcodingchallenge.animals.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * All items must display the name, phylum, and scientific name.
 * Dog items must display the slogan and lifespan
 * Bird items must display wingspan and habitat
 * Bug items must display prey and predators
 */

data class AnimalDto(
    val name: String,
    val taxonomy: TaxonomyDto,
    val characteristics: CharacteristicsDto
)

data class TaxonomyDto(
    @SerializedName("scientific_name")
    val scientificName: String?,
    @SerializedName("phylum")
    val phylum: String?,
    @SerializedName("family")
    val family: String?,
    @SerializedName("class")
    val animalClass: String?
)

data class CharacteristicsDto(
    val slogan: String?,
    val lifespan: String?,
    val wingspan: String?,
    val habitat: String?,
    val prey: String?,
    val predators: String?,
)
