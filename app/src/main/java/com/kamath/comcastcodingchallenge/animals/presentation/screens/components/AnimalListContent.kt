package com.kamath.comcastcodingchallenge.animals.presentation.screens.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal

@Composable
fun AnimalListContent(
    animals: List<Animal>,
    horizontalAlignment: Alignment.Horizontal
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animals) { each ->
                AnimalCard(
                    animal = each,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(IntrinsicSize.Max)

                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = horizontalAlignment,
        ) {
            items(animals) { each ->
                AnimalCard(
                    animal = each,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
