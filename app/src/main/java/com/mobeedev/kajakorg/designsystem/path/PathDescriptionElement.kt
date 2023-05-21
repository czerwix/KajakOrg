package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.dash
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.PathOverviewItem

@Composable
fun PathDescriptionElement(path: PathItem, modifier: Modifier) {
    KajakTheme {
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = pathIdToPicture[path.overview.id]!!),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                )

                Text(
                    text = path.overview.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 16.dp, end = 16.dp, top = 2.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.path_total_lenght) + String.space + String.dash + String.space,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.wrapContentSize()
                    )
                    Text(
                        text = path.overview.length.toString() + String.space +
                                stringResource(id = R.string.at_kilometer),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.wrapContentSize()
                    )

                    Text(
                        text = stringResource(id = R.string.number_of_sections) + String.space,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 4.dp)
                    )
                    Text(
                        text = path.overview.numberOfSections.toString(),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                }
                Text(
                    text = path.overview.description,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPathDescriptionElement() {
    KajakTheme {
        PathDescriptionElement(
            PathItem(
                overview = PathOverviewItem(
                    id = 1,
                    name = "Barycz",
                    versionCode = 1,
                    length = 100.0,
                    numberOfMarkers = 12,
                    numberOfSections = 15,
                    difficulty = "cosik",
                    nuisance = "cosikkkkk",
                    description = "SOME very very long description"

                ),
                pathSectionsEvents = listOf()
            ),
            Modifier
        )
    }
}