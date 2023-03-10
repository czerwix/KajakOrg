package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayEnd
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayStart
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.Section
import kotlin.random.Random

@Composable
fun PathSectionElement(item: Section, onClick: (Int) -> Unit) {
    KajakTheme {
        Box(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onClick(item.id) }
        ) {

//            Image(
//                painter = painterResource(id = pathIdToPicture[8]!!),
//                contentDescription = null,
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier
//                    .matchParentSize()
//                    .padding(start = 2.dp, end = 2.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .drawWithCache {
//                        val gradient = Brush.verticalGradient(
//                            colors = listOf(PathOverviewOverlayStart, PathOverviewOverlayEnd),
//                            startY = 0f,
//                            endY = size.height
//                        )
//                        onDrawWithContent {
//                            drawContent()
//                            drawRect(gradient, blendMode = BlendMode.Multiply)
//                        }
//                    }
//            )
            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 10.dp)
            ) {

//                Text(
//                    text = stringResource(id = R.string.section),
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .padding(start = 4.dp, end = 2.dp)
//                )//todo maybe add a section 1 out of N here or kilometer at  or something
                Text(
                    text = stringResource(id = R.string.section, item.name),
                    style = MaterialTheme.typography.titleLarge,
//                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 10.dp)
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.White,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 8.dp)
                )
                if (item.events.isNotEmpty()) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.section_length,
                                item.events.first().atKilometer,
                                item.events.last().atKilometer,
                                item.events.first().atKilometer - item.events.last().atKilometer
                            ),
                            style = MaterialTheme.typography.bodyMedium,
//                            color = Color.White,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 4.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                ) {//todo fix this rows alignment with icons maybe? i don't like this look
                    Text(
                        text = stringResource(id = R.string.events_on_section) +
                                String.space +
                                item.events.size.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
//                        color = Color.White,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.nuisance) +
                                String.space +
                                item.nuisance,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
//                        color = Color.White,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 2.dp)
                    )
                    if (item.difficulty.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.difficulty) +
                                    String.space +
                                    item.difficulty,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
//                            color = Color.White,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 2.dp)
                        )
                    }
                    if (item.picturesque.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.picturesque) +
                                    String.space +
                                    item.picturesque,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
//                            color = Color.White,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 2.dp)
                        )
                    }
                    if (item.cleanliness.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.cleanliness) +
                                    String.space +
                                    item.cleanliness,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
//                            color = Color.White,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}


fun getRandomBackgroundImage(id: Int): Int {//todo refactor/change this
    val pictures = pathIdToPicture.entries.toList()
    return pictures[Random.nextInt(pictures.size)].value
}

@Preview(showBackground = true)
@Composable
fun PreviewPathSectionElement() {
    KajakTheme {
        PathSectionElement(
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
            ),
            onClick = {}
        )
    }
}