package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.data.model.detail.EventType
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.EventDescription
import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PathEventWithMapSimpler(
    item: Event,
    onClick: (Int) -> Unit,
    googleMapVisibilityState: GoogleMapsStatusItem = GoogleMapsStatusItem.EnableMap,
    modifier: Modifier
) {
    KajakTheme {
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
                .clickable { onClick(item.id) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                //Title and atKm
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.event_title, item.townName),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Row(modifier = Modifier.padding(start = 4.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.outline_place_24),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(color = Color.Black),
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Text(
                            text = item.atKilometer.toString() + String.space + stringResource(id = R.string.at_kilometer),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .wrapContentSize()
                        )
                    }
                }
                if (googleMapVisibilityState == GoogleMapsStatusItem.EnableMap) {
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
                    var cameraState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(item.position, 18f)
                    }
                    //Google Map
                    GoogleMap(
                        cameraPositionState = cameraState,
                        properties = properties,
                        uiSettings = uiSettings,
                        googleMapOptionsFactory = { GoogleMapOptions().liteMode(true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
//                    .clip(RoundedCornerShape(8.dp))
                    ) {
                        Marker(state = MarkerState(position = item.position))
                    }
                }

                //Event descriptions
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    item.eventDescription.forEach { event ->
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 6.dp)
                        ) {
                            FlowRow(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.event_type) +
                                            String.space +
                                            event.eventType,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.wrapContentSize()
                                )

                                Image(//todo icon of eventType
                                    painter = painterResource(id = event.eventType.getIconResId()),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(start = 4.dp)
                                )
                            }
                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPathEventWithMapSimpler() {
    KajakTheme {
        PathEventWithMapSimpler(
            item = Event(
                0,
                townName = "Bolimów",
                position = LatLng(52.08174002, 20.17178617),
                label = "",
                sortOrder = -1,
                atKilometer = 14.5,
                eventDescription = listOf(
                    EventDescription(
                        eventType = EventType.Niebezpieczeństwo,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    ),
                    EventDescription(
                        eventType = EventType.Most,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    )
                )
            ),
            onClick = {},
            GoogleMapsStatusItem.EnableMap,
            Modifier
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPathEventWithMapSimplerLong() {
    KajakTheme {
        PathEventWithMapSimpler(
            item = Event(
                0,
                townName = "Bolimów gdzies tam w polu płynie rzeka niewielka",
                position = LatLng(52.08174002, 20.17178617),
                label = "",
                sortOrder = -1,
                atKilometer = 14.5,
                eventDescription = listOf(
                    EventDescription(
                        eventType = EventType.Niebezpieczeństwo,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    ),
                    EventDescription(
                        eventType = EventType.Most,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    )
                )
            ),
            onClick = {},
            GoogleMapsStatusItem.EnableMap,
            Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPathEventWithMapMapDIsable() {
    KajakTheme {
        PathEventWithMapSimpler(
            item = Event(
                0,
                townName = "Bolimów gdzies tam w polu płynie rzeka niewielka",
                position = LatLng(52.08174002, 20.17178617),
                label = "",
                sortOrder = -1,
                atKilometer = 14.5,
                eventDescription = listOf(
                    EventDescription(
                        eventType = EventType.Niebezpieczeństwo,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    ),
                    EventDescription(
                        eventType = EventType.Most,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    )
                )
            ),
            onClick = {},
            GoogleMapsStatusItem.DisableMap,
            Modifier
        )
    }
}