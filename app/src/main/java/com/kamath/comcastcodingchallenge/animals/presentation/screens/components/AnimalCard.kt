package com.kamath.comcastcodingchallenge.animals.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.model.Bird
import com.kamath.comcastcodingchallenge.animals.domain.model.Bug
import com.kamath.comcastcodingchallenge.animals.domain.model.Dog

@Composable
fun AnimalCard(
    animal: Animal,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("animalCard_${animal.name}"),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                animal.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                animal.phylum,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                animal.scientificName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(thickness = 1.dp)
            Spacer(modifier.height(8.dp))
            when (animal) {
                is Dog -> {
                    AnimalDetailRow(label = "Slogan", value = animal.slogan)
                    AnimalDetailRow(label = "Lifespan", value = animal.lifespan)
                }

                is Bird -> {
                    AnimalDetailRow(label = "Wingspan", value = animal.wingspan)
                    AnimalDetailRow(label = "Habitat", value = animal.habitat)
                }

                is Bug -> {
                    AnimalDetailRow(label = "Prey", value = animal.prey)
                    AnimalDetailRow(label = "Predators", value = animal.predators)
                }
            }
        }
    }
}

@Composable
private fun AnimalDetailRow(label: String, value: String) {
    if (value.isBlank()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}


@Preview
@Composable
fun AnimalCardPreview() {
    AnimalCard(
        animal = Dog(
            name = "Preview Dog",
            slogan = "A good boy",
            scientificName = "Canis lupus familiaris",
            phylum = "Chordata",
            lifespan = "12 years"
        )
    )
}