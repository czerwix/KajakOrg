package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayMiddle
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayStart
import com.mobeedev.kajakorg.designsystem.theme.White
import com.mobeedev.kajakorg.ui.model.PathOveriewItem

val PathOverviewElementHeight = 140.dp

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
                    .height(PathOverviewElementHeight)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(
                                PathOverviewOverlayStart,
                                PathOverviewOverlayMiddle,
                                PathOverviewOverlayEnd
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
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 2.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.conversion_path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(20.dp),
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
                }
                var descriptionMaxLines = 2
                if (item.difficulty.isBlank()) descriptionMaxLines += 1
                if (item.nuisance.isBlank()) descriptionMaxLines += 1
                Text(
                    text = item.description,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = descriptionMaxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp, top = 4.dp)
                )


                if (item.difficulty.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.path_difficulty, item.difficulty),
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                        )
                    }
                }
                if (item.nuisance.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.path_nuisance, item.nuisance),
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
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
                ), onClick = {}
            )
        }
    }
}