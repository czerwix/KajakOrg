package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.domain.model.detail.Section

@Composable
fun PathSectionElement(item: Section, onClick: (Int) -> Unit) {
    KajakTheme {
        Box(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onClick(item.id) }
//            .padding(start = 4.dp)
        ) {
//            Divider(
//                color = RiverBLue,
//                modifier = Modifier
//                    .width(4.dp)
//                    .fillMaxHeight()
//            )
//            Image(
//                painter = painterResource(id = R.drawable.kajak_image),
//                contentDescription = null,
//                modifier = Modifier
//                    .width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
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
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .wrapContentSize()
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 16.dp, top = 4.dp)
                )
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {//todo fix this rows alignment with icons maybe? i don't like this look
                    Text(
                        text = stringResource(id = R.string.events_on_section) +
                                String.space +
                                item.events.size.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                    )
                    Text(
                        text = stringResource(id = R.string.nuisance) +
                                String.space +
                                item.nuisance,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 8.dp)
                    )
                    if (item.difficulty.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.difficulty) +
                                    String.space +
                                    item.difficulty,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 8.dp)
                        )
                    }
                }
                Row {
                    if (item.picturesque.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.picturesque) +
                                    String.space +
                                    item.picturesque,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                        )
                    }
                    if (item.cleanliness.isNotBlank()) {
                        Text(
                            text = stringResource(id = R.string.cleanliness) +
                                    String.space +
                                    item.cleanliness,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
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
                mutableListOf()
            ),
            onClick = {}
        )
    }
}