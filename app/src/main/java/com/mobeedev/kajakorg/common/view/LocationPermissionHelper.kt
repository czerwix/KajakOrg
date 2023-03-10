package com.mobeedev.kajakorg.common.view

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mobeedev.kajakorg.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun checkLocationPermissions(
    onPermissionGranted: @Composable BoxScope.() -> Unit,
    onPermissionDenied: @Composable BoxScope.() -> Unit
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissionState.allPermissionsGranted) {
        Box(Modifier.fillMaxSize()) {
            onPermissionGranted.invoke(this)
        }
    } else {
        if (locationPermissionState.shouldShowRationale) {
            showMapDetailsPermissionRationale(locationPermissionState)
        } else if (locationPermissionState.revokedPermissions.isNotEmpty()) {
            LaunchedEffect("Permission_Request_Key") {
                locationPermissionState.launchMultiplePermissionRequest()
            }
            Box(Modifier.fillMaxSize()) {
                onPermissionDenied.invoke(this)
            }
        } else {
            showMapDetailsPermissionRequest(locationPermissionState = locationPermissionState)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun showMapDetailsPermissionRationale(locationPermissionState: MultiplePermissionsState) {
    Text(stringResource(id = R.string.location_rationale))
    Button(onClick = { locationPermissionState.launchMultiplePermissionRequest() }) {
        Text(stringResource(id = R.string.location_request))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun showMapDetailsPermissionRequest(locationPermissionState: MultiplePermissionsState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = stringResource(id = R.string.path_map_location_request),
            color = Color.Black,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Button(
            onClick = { locationPermissionState.launchMultiplePermissionRequest() },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.location_request),
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}