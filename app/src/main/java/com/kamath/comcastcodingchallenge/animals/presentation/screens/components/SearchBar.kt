package com.kamath.comcastcodingchallenge.animals.presentation.screens.components

import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.testTag

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier.testTag("searchBar"),
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        label = { Text("Search Animals") },
        placeholder = { Text("Dog, Bird, Bug, etc..") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    )
}