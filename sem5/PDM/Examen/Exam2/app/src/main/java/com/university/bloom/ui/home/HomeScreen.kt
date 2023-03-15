package com.university.bloom.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.university.bloom.R
import com.university.bloom.model.Item
import com.university.bloom.theming.BloomTheme
import com.university.bloom.ui_foundation.PrimaryButton
import com.university.bloom.utils.displayToast

@Composable
fun HomeDestination(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState: HomeUiState by viewModel.uiState.collectAsState()
    var selectedId: Int by remember { mutableStateOf(-1) }

    val context = LocalContext.current
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            displayToast(context, uiState.error?.message)
            viewModel.onErrorConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(text = stringResource(R.string.home_title), style = MaterialTheme.typography.h1)
                Spacer(modifier = Modifier.weight(1f))
                if (uiState.shouldRetry) {
                    PrimaryButton(text = stringResource(R.string.home_retry)) {
                        viewModel.loadItems()
                    }
                }
                if (uiState.loading) {
                    CircularProgressIndicator()
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.items.forEach { item ->
                item {
                    ListItem(
                        item = item,
                        expanded = item.id == selectedId,
                        username = viewModel.username,
                        onActionButtonClick = {
                            if (item.status == "free") {
                                viewModel.onTakeClick(it)
                            } else if (item.status == "taken") {
                                viewModel.onReleaseClick(it)
                            }
                        },
                        onClick = {
                            selectedId = if (selectedId == item.id) {
                                -1
                            } else {
                                item.id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ListItem(
    item: Item,
    expanded: Boolean,
    username: String,
    onActionButtonClick: (Int) -> Unit,
    onClick: () -> Unit
) = Card(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick),
    shape = MaterialTheme.shapes.large,
    backgroundColor = MaterialTheme.colors.surface
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(modifier = Modifier.padding(vertical = 16.dp), text = "${item.number} ${item.status}", style = MaterialTheme.typography.h2)
        if (expanded) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Id: ${item.id}")
                    Text(text = "TakenBy: ${item.takenBy}")
                }
                if (item.status == "free") {
                    PrimaryButton(text = stringResource(R.string.take_string), onClick = { onActionButtonClick(item.id) })
                } else if (item.status == "taken" && item.takenBy == username) {
                    PrimaryButton(text = stringResource(R.string.release_string), onClick = { onActionButtonClick(item.id) })
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview_HomeDestination() {
    BloomTheme {
        HomeDestination()
    }
}