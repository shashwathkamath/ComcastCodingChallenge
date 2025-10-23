package com.kamath.comcastcodingchallenge.animals.domain.model

sealed interface Animal {
    val name: String
    val phylum: String
    val scientificName: String

    fun searchableContent(): List<String>
}

data class Dog(
    override val name: String,
    override val phylum: String,
    override val scientificName: String,
    val slogan: String,
    val lifespan: String
) : Animal {
    override fun searchableContent(): List<String> =
        listOf(name, phylum, scientificName, slogan, lifespan)
}

data class Bird(
    override val name: String,
    override val phylum: String,
    override val scientificName: String,
    val wingspan: String,
    val habitat: String
) : Animal {
    override fun searchableContent(): List<String> =
        listOf(name, phylum, scientificName, wingspan, habitat)
}

data class Bug(
    override val name: String,
    override val phylum: String,
    override val scientificName: String,
    val prey: String,
    val predators: String
) : Animal {
    override fun searchableContent(): List<String> =
        listOf(name, phylum, scientificName, prey, predators)
}