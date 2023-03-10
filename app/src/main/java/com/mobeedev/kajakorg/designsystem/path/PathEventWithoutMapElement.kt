package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.data.model.detail.EventType
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayEnd
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.EventDescription

 val pathEventWithoutMapElementHeight = 300.dp
@Composable
fun PathEventWithoutMapElement(item: Event) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .height(pathEventWithoutMapElementHeight)
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = stringResource(id = R.string.event_title, item.townName),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.wrapContentSize()
            )
            Image(
                painter = painterResource(id = R.drawable.outline_place_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.Black),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = item.atKilometer.toString() + String.space + stringResource(id = R.string.at_kilometer),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.wrapContentSize()
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(item.eventDescription) { event ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.event_type) +
                                        String.space +
                                        event.eventType,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.wrapContentSize()
                            )

                            Image(//todo icon of eventType
                                painter = painterResource(id = R.drawable.outline_place_24),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = Color.Black),
                                modifier = Modifier.size(24.dp)
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


@Preview(showBackground = true)
@Composable
fun PreviewPathEventWithoutElement() {
    KajakTheme {
        PathEventWithoutMapElement(
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
                    ),
                    EventDescription(
                        eventType = EventType.Most,
                        sortOrder = -1,
                        description = "Łąka naprzeciw domu sołtysa. Wygodne miejsce wodowania. Łąka z pewnością jest czyjąś własnością, jednak właściciel nie objawił się."
                    )
                )
            )
        )
    }
}