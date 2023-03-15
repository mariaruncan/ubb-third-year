package com.university.bloom.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.university.bloom.R
import com.university.bloom.model.Scooter
import com.university.bloom.ui_foundation.PrimaryButton
import com.university.bloom.utils.createNotificationChannel
import com.university.bloom.utils.showSimpleNotification

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeDestination(
    viewModel: HomeViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    onAddClick: () -> Unit,
    onLogOutClick: () -> Unit
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.loadItems()
    }

    val context = LocalContext.current
    val channelId = "MyTestChannel"
    val notificationId = 0
    LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }
    val connectionState: Boolean by viewModel.connectionState.collectAsState(initial = false)
    LaunchedEffect(connectionState){
        if(!connectionState) {
            showSimpleNotification(
                context,
                channelId,
                notificationId,
                "Connection failed",
                "Please go online"
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Column {
                    Text(text = "Bloom", style = MaterialTheme.typography.h1)
                    Text(text = "Connected: $connectionState")
                }
                Spacer(modifier = Modifier.weight(1f))
                PrimaryButton(
                    text = "Log out",
                    onClick = {
                        viewModel.logOut()
                        onLogOutClick()
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    modifier = Modifier.background(MaterialTheme.colors.secondary),
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val items by viewModel.items.collectAsStateWithLifecycle(initialValue = emptyList())
            HomeScreen(
                items = items,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun HomeScreen(
    items: List<Scooter>,
    onItemClick: (String) -> Unit
) {
    if (items.isEmpty()) {
        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
    } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(16.dp)) {
            items(items) { item ->
                ScooterItem(scooter = item, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
private fun ScooterItem(
    scooter: Scooter,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(scooter._id) },
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Number ${scooter.number}", style = MaterialTheme.typography.h1)
            Text(text = "id ${scooter._id}", style = MaterialTheme.typography.body1)
            Text(text = "battery level ${scooter.batteryLevel}", style = MaterialTheme.typography.body1)
        }
    }
}