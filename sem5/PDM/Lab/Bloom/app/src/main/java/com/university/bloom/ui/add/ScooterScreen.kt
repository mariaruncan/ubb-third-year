package com.university.bloom.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.university.bloom.model.Scooter
import com.university.bloom.ui_foundation.BloomTextField
import com.university.bloom.ui_foundation.Label
import com.university.bloom.ui_foundation.PrimaryButton

@Composable
fun ScooterDestination(
    viewModel: ScooterViewModel = hiltViewModel(),
    onSaveSuccessful: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = uiState.savingSuccessful) {
        if (uiState.savingSuccessful) {
            onSaveSuccessful()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                contentPadding = PaddingValues(horizontal = 16.dp),
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary
            ) {
                Text(text = if (uiState.item == null) "Save page" else "Edit page", style = MaterialTheme.typography.h1)
                Spacer(modifier = Modifier.weight(1f))
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (uiState.item == null) {
                var number: Int by rememberSaveable { mutableStateOf(0) }
                var batteryLevel: Int by rememberSaveable { mutableStateOf(0) }
                var locked: Boolean by rememberSaveable { mutableStateOf(false) }
                var latitude: Double by rememberSaveable { mutableStateOf(46.772784) }
                var longitude: Double by rememberSaveable { mutableStateOf(23.622797) }
                ScooterScreen(
                    showMap = viewModel.connectionState.collectAsState(initial = false).value,
                    id = null,
                    number = number,
                    batteryLevel = batteryLevel,
                    locked = locked,
                    latitude = latitude,
                    longitude = longitude,
                    onNumberChanged = { number = it },
                    onBatteryLevelChanged = { batteryLevel = it },
                    onLockedChanged = { locked = it },
                    onCoordinatesChanged = { lat, lng ->
                        latitude = lat
                        longitude = lng
                    },
                    onSaveItem = { nr, b, l, lat, lng ->
                        viewModel.saveItem(nr, b, l, lat, lng)
                    }
                )
            } else {
                val scooter: Scooter = uiState.item!!
                var number: Int by rememberSaveable { mutableStateOf(scooter.number) }
                var batteryLevel: Int by rememberSaveable { mutableStateOf(scooter.batteryLevel) }
                var locked: Boolean by rememberSaveable { mutableStateOf(scooter.locked) }
                var latitude: Double by rememberSaveable { mutableStateOf(scooter.latitude) }
                var longitude: Double by rememberSaveable { mutableStateOf(scooter.longitude) }
                ScooterScreen(
                    id = scooter._id,
                    number = number,
                    batteryLevel = batteryLevel,
                    locked = locked,
                    latitude = latitude,
                    longitude = longitude,
                    showMap = viewModel.connectionState.collectAsState(initial = false).value,
                    onNumberChanged = { number = it },
                    onBatteryLevelChanged = { batteryLevel = it },
                    onLockedChanged = { locked = it },
                    onCoordinatesChanged = { lat, lng ->
                        latitude = lat
                        longitude = lng
                    },
                    onSaveItem = { nr, b, l, lat, lng ->
                        viewModel.saveItem(nr, b, l, lat, lng)
                    }
                )
            }
        }
    }
}

@Composable
private fun ScooterScreen(
    showMap: Boolean,
    id: String?,
    number: Int,
    batteryLevel: Int,
    locked: Boolean,
    latitude: Double,
    longitude: Double,
    onNumberChanged: (Int) -> Unit,
    onBatteryLevelChanged: (Int) -> Unit,
    onLockedChanged: (Boolean) -> Unit,
    onCoordinatesChanged: (Double, Double) -> Unit,
    onSaveItem: (Int, Int, Boolean, Double, Double) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val modifier = Modifier.fillMaxWidth()
        val modifier2 = Modifier.width(150.dp)
        if (id != null) {
            Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
                Label(text = "Id:")
                Label(text = id)
            }
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Label(text = "Number:")
            BloomTextField(modifier = modifier2, text = number.toString(), onValueChanged = { onNumberChanged(it.toInt()) })
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Label(text = "Battery level:")
            BloomTextField(modifier = modifier2, text = batteryLevel.toString(), onValueChanged = { onBatteryLevelChanged(it.toInt()) })
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Label(text = "Locked:")
            Checkbox(checked = locked, onCheckedChange = onLockedChanged)
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Label(text = "Latitude:")
            Label(text = latitude.toString())
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Label(text = "Longitude:")
            Label(text = longitude.toString())
        }
        PrimaryButton(text = "Save") {
            onSaveItem(number, batteryLevel, locked, latitude, longitude)
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (showMap) {
            MapComponent(latitude = latitude, longitude = longitude, onCoordinatesChanged = onCoordinatesChanged)
        }
    }
}

@Composable
private fun MapComponent(
    latitude: Double,
    longitude: Double,
    onCoordinatesChanged: (Double, Double) -> Unit
) {
    val markerState = rememberMarkerState(position = LatLng(latitude, longitude))
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerState.position, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        cameraPositionState = cameraPositionState,
        onMapClick = {
            markerState.position = it
            onCoordinatesChanged(it.latitude, it.longitude)
        }
    ) {
        Marker(state = markerState)
    }
}