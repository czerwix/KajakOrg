package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.data.model.detail.EventType
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayEnd
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.EventDescription

@Composable
fun PathEventElement(
    item: Event,
    onClick: (Int) -> Unit
) {//todo template view of most important info... make pretty when i have time.
    KajakTheme {
        var mapEndPadding = with(LocalDensity.current) { 100.dp.toPx().toInt() }
        var mapBottomPadding: Int =
            with(LocalDensity.current) { determineBottomPadding(item, this) }
        Box(modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .clickable { onClick(item.id) }
        ) {
            var uiSettings by remember {
                mutableStateOf(
                    MapUiSettings(
                        compassEnabled = false,
                        indoorLevelPickerEnabled = false,
                        mapToolbarEnabled = false,
                        myLocationButtonEnabled = false,
                        rotationGesturesEnabled = false,
                        scrollGesturesEnabled = false,
                        scrollGesturesEnabledDuringRotateOrZoom = false,
                        tiltGesturesEnabled = false,
                        zoomControlsEnabled = false
                    )
                )
            }
            var properties by remember {
                mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 4.dp, end = 4.dp)
                    .clip(RoundedCornerShape(8.dp)),
                properties = properties,
                uiSettings = uiSettings,
                googleMapOptionsFactory = { GoogleMapOptions().liteMode(true) }
            ) {
                Marker(state = MarkerState(position = item.position))
                MapEffect() { map ->
                    map.setPadding(0, 0, mapEndPadding, mapBottomPadding)
                    map.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.fromLatLngZoom(
                                item.position,
                                18f
                            )
                        )
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.event_title, item.townName),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 16.dp)
                        .background(
                            PathOverviewOverlayEnd,
                            RoundedCornerShape(4.dp)
                        )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .background(
                            PathOverviewOverlayEnd,
                            RoundedCornerShape(4.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.outline_place_24),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.atKilometer.toString() + String.space + stringResource(id = R.string.at_kilometer),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp, top = 34.dp, end = 16.dp, bottom = 25.dp)
                ) {
                    item.eventDescription.forEach { event ->
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(
                                        PathOverviewOverlayEnd,
                                        RoundedCornerShape(4.dp)
                                    )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.event_type) +
                                            String.space +
                                            event.eventType,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White,
                                    modifier = Modifier
                                        .wrapContentSize()
                                )

                                Image(//todo icon of eventType
                                    painter = painterResource(id = R.drawable.outline_place_24),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(color = Color.White),
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(
                                        PathOverviewOverlayEnd,
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun determineBottomPadding(item: Event, density: Density): Int = with(density) {
    var textRows = 0
    var textLengthTotal = 0
    var textPadding = 0
    item.eventDescription.forEach {
        textLengthTotal += it.description.length
    }
    textRows += textLengthTotal / 50
    if (item.eventDescription.size > 1) {
        textPadding = 26.dp.toPx().toInt() * item.eventDescription.size
    }
    if (textRows == 0) {
        0
    } else if (textRows < 5) {
        (14.sp.toPx() * (textRows * 1.1)).toInt() + textPadding
    } else {
        (14.sp.toPx() * (textRows * 1.8)).toInt() + textPadding
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPathEventElement() {
    KajakTheme {
        PathEventElement(
            item = Event(
                0,
                townName = "Bolimów",
                position = LatLng(52.08174002, 20.17178617),
                label = "",
                sortOrder = -1,
                atKilometer = 14.5,
                eventDescription = listOf(
                    EventDescription(
                        eventType = EventType.Uwaga,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    )
                )
            ),
            onClick = {}
        )
    }
}