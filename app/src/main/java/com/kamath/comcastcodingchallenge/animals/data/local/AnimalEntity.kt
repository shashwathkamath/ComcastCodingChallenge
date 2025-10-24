package com.kamath.comcastcodingchallenge.animals.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class AnimalEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,

    @ColumnInfo(name = "scientific_name")
    val scientificName: String?,

    @ColumnInfo(name = "phylum")
    val phylum: String?,

    @ColumnInfo(name = "family")
    val family: String?,

    @ColumnInfo(name = "animal_class")
    val animalClass: String?,

    @ColumnInfo(name = "slogan")
    val slogan: String?,

    @ColumnInfo(name = "lifespan")
    val lifespan: String?,

    @ColumnInfo(name = "wingspan")
    val wingspan: String?,

    @ColumnInfo(name = "habitat")
    val habitat: String?,

    @ColumnInfo(name = "prey")
    val prey: String?,

    @ColumnInfo(name = "predators")
    val predators: String?,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)
