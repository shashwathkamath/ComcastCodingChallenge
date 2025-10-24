import com.kamath.comcastcodingchallenge.animals.data.local.AnimalEntity
import com.kamath.comcastcodingchallenge.animals.data.remote.dto.AnimalDto
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.model.Bird
import com.kamath.comcastcodingchallenge.animals.domain.model.Bug
import com.kamath.comcastcodingchallenge.animals.domain.model.Dog
import com.kamath.comcastcodingchallenge.core.util.Constants
import com.kamath.comcastcodingchallenge.core.util.EntityMapper

object AnimalEntityMapper : EntityMapper<AnimalEntity, Animal> {

    override fun toDomain(entity: AnimalEntity): Animal? {
        val scientificName = entity.scientificName ?: Constants.DEFAULT_VALUE
        val phylum = entity.phylum ?: Constants.DEFAULT_VALUE
        val animalClass = entity.animalClass ?: Constants.DEFAULT_VALUE
        val family = entity.family ?: Constants.DEFAULT_VALUE

        return when {
            family.equals(Constants.FAMILY_CANIDAE, ignoreCase = true) -> Dog(
                name = entity.name,
                phylum = phylum,
                scientificName = scientificName,
                slogan = entity.slogan.orEmpty(),
                lifespan = entity.lifespan.orEmpty()
            )
            animalClass.equals(Constants.CLASS_AVES, ignoreCase = true) -> Bird(
                name = entity.name,
                phylum = phylum,
                scientificName = scientificName,
                wingspan = entity.wingspan.orEmpty(),
                habitat = entity.habitat.orEmpty()
            )
            phylum.equals(Constants.PHYLUM_ARTHROPODA, ignoreCase = true) -> Bug(
                name = entity.name,
                phylum = phylum,
                scientificName = scientificName,
                prey = entity.prey.orEmpty(),
                predators = entity.predators.orEmpty()
            )
            else -> null
        }
    }

    override fun toEntity(domain: Animal): AnimalEntity {
        return AnimalEntity(
            name = domain.name,
            scientificName = domain.scientificName,
            phylum = domain.phylum,
            family = when (domain) {
                is Dog -> Constants.FAMILY_CANIDAE
                is Bird -> ""
                is Bug -> ""
            },
            animalClass = when (domain) {
                is Bird -> Constants.CLASS_AVES
                else -> ""
            },
            slogan = (domain as? Dog)?.slogan,
            lifespan = (domain as? Dog)?.lifespan,
            wingspan = (domain as? Bird)?.wingspan,
            habitat = (domain as? Bird)?.habitat,
            prey = (domain as? Bug)?.prey,
            predators = (domain as? Bug)?.predators
        )
    }

    fun AnimalDto.toEntity(): AnimalEntity {
        return AnimalEntity(
            name = this.name,
            scientificName = this.taxonomy.scientificName,
            phylum = this.taxonomy.phylum,
            family = this.taxonomy.family,
            animalClass = this.taxonomy.animalClass,
            slogan = this.characteristics.slogan,
            lifespan = this.characteristics.lifespan,
            wingspan = this.characteristics.wingspan,
            habitat = this.characteristics.habitat,
            prey = this.characteristics.prey,
            predators = this.characteristics.predators,
            lastUpdated = System.currentTimeMillis()
        )
    }
}
