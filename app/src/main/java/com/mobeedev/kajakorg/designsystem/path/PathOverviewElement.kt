package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayEnd
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayStart
import com.mobeedev.kajakorg.designsystem.theme.White
import com.mobeedev.kajakorg.ui.model.PathOveriewItem

@Composable
fun PathOverViewElement(item: PathOveriewItem, onClick: (Int) -> Unit) {
    KajakTheme {
        Box(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
            .clickable { onClick(item.id) }
        ) {

            Image(
                painter = painterResource(id = pathIdToPicture[item.id]!!),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(
                                PathOverviewOverlayStart,
                                PathOverviewOverlayEnd,
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = size.height - (size.height / 4) //todo extract to separate const
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                Text(
                    text = item.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 4.dp, end = 2.dp)
                )
                Text(
                    text = item.description,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp, top = 4.dp)
                )

                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.conversion_path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(28.dp),
                        colorFilter = ColorFilter.tint(White)
                    )
                    Text(
                        text = stringResource(id = R.string.path_length_km, item.length),
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 4.dp)
                    )
                    if (item.difficulty.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.path_difficulty, item.difficulty),
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 4.dp)
                                .weight(1f)
                        )
                    }
                    if (item.difficulty.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.path_nuisance, item.nuisance),
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 4.dp)
                                .weight(1f)
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewPathOverViewElement() {
    KajakTheme {
        Surface {
            PathOverViewElement(
                PathOveriewItem( //extract to PreviewParameter. do teh same for othe rmodels
                    id = 1,
                    name = "Bystrzyca kłodzka",
                    versionCode = 1,
                    length = 200.0,
                    numberOfMarkers = 99,
                    numberOfSections = 60,
                    difficulty = "D1,D2,D3vbvbvcvbvfdsfdsfdsfdsfdsfdsfcbvcbvcb",
                    nuisance = "N1,N2,N3,N4,Ndsfdsfdsfdsfdsfdfdsfdsfdsfsd",
                    description = "Dłuuuuuuuuuusdusndusnadnsad sauidh sauidnsan dsajdi sd sadiosa ds dsoa disnadi said sadi sadio said sadio sadisa dsa doas dosa dosa doas dosa doas d"
                ),
                onClick = {}
            )
        }
    }
}