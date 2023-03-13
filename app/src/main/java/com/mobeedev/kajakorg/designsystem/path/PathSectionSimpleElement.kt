package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.Section

@Composable
fun PathSectionSimpleElement(item: Section) {
    KajakTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.section_compact, item.name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp)
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 8.dp)
            )
            if (item.events.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.section_length,
                            item.events.first().atKilometer,
                            item.events.last().atKilometer,
                            item.events.first().atKilometer - item.events.last().atKilometer
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.wrapContentSize()
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 4.dp)
            ) {//todo fix this rows alignment with icons maybe? i don't like this look
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.events_on_section) + String.space + item.events.size.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Text(
                        text = stringResource(id = R.string.nuisance) + String.space + item.nuisance,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    if (item.difficulty.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.difficulty) + String.space + item.difficulty,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 2.dp)
                        )
                    }
                    if (item.picturesque.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.picturesque) + String.space + item.picturesque,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 2.dp)
                        )
                    }
                }
                if (item.cleanliness.isNotBlank()) {
                    Text(
                        text = stringResource(id = R.string.cleanliness) + String.space + item.cleanliness,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPathSectionSimpleElement() {
    KajakTheme {
        PathSectionSimpleElement(
            item = Section(
                1,
                "rzeka",
                "Rawka w Bolimowskim Parku Krajobrazowym",
                "U4",
                "ZWC",
                "***",
                "KL2",
                2,
                "Bardzo dużo drzew, szybki nurt. Rawka kręci, a wokoło piękna okolica.",
                mutableListOf(
                    Event(1, "", LatLng(0.0, 0.0), 18.0, "", 1, mutableListOf()),
                    Event(2, "", LatLng(0.0, 0.0), 14.0, "", 2, mutableListOf())
                )
            )
        )
    }
}