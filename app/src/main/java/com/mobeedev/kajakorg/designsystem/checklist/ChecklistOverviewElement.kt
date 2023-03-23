package com.mobeedev.kajakorg.designsystem.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.designsystem.theme.White
import com.mobeedev.kajakorg.ui.model.ChecklistItem
import com.mobeedev.kajakorg.ui.model.ChecklistValueItem
import java.util.UUID

private const val MAX_CHECKLIST_ELEMENTS = 8

@Composable
fun ChecklistOverviewElement(checklistItem: ChecklistItem, onClick: (UUID) -> Unit) {
    KajakTheme {
        val contentColor = remember {
            if (checklistItem.color.isUnspecified) Color.Black else Color.White
        }
        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(White),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onClick(checklistItem.id) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(if (checklistItem.color.isUnspecified) Color.White else checklistItem.color)
            ) {
                if (checklistItem.title.isNotEmpty()) {
                    Text(
                        text = checklistItem.title,
                        color = contentColor,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
                if (checklistItem.description.isNotEmpty()) {
                    Text(
                        text = checklistItem.description,
                        color = contentColor,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    )
                }
                if (checklistItem.checklist.isNotEmpty()) {
                    //todo add 6 items max from checklist
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(bottom = 8.dp)
                    ) {
                        checklistItem.checklist.subList(
                            0,
                            checklistItem.checklist.size.coerceAtMost(MAX_CHECKLIST_ELEMENTS)
                        ).forEach { item ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 2.dp)
                            ) {
                                KajakCheckbox(
                                    checked = item.isDone,
                                    onCheckedChange = null,
                                    enabled = false,
                                    checkedColor = contentColor,
                                    uncheckedColor = contentColor,
                                    checkmarkColor = contentColor,
                                    modifier = Modifier
                                        .padding(top = 3.dp)
                                        .size(10.dp),
                                    cornerRadius = 2.dp
                                )

                                Text(
                                    text = item.value,
                                    color = contentColor,
                                    style = MaterialTheme.typography.bodySmall
                                        .copy(textDecoration = if (item.isDone) TextDecoration.LineThrough else null),
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(start = 4.dp)
                                )
                            }
                        }

                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewChecklistElementNormal() {
    KajakTheme {
        Surface {
            ChecklistOverviewElement(
                checklistItem = ChecklistItem(
                    title = "Spływ kajakiem po baryczy",
                    description = "Shor description here",
                    checklist = listOf(
                        ChecklistValueItem(true, "Item1"),
                        ChecklistValueItem(true, "Item2"),
                        ChecklistValueItem(false, "Item3"),
                        ChecklistValueItem(
                            true,
                            "Item4 very long Item very long Item very long Item very long Item very long Item  very long Item"
                        ),
                        ChecklistValueItem(false, "Item5"),
                        ChecklistValueItem(true, "Item6"),
                        ChecklistValueItem(false, "Item7"),
                        ChecklistValueItem(false, "Item8")
                    ),
                    color = Color.Unspecified
                )
            ) {}
        }
    }
}

@Preview
@Composable
fun PreviewChecklistElementLong() {
    KajakTheme {
        Surface {
            ChecklistOverviewElement(
                checklistItem = ChecklistItem(
                    title = "Spływ kajakiem po baryczy, Spływ kajakiem po baryczy,kajakiem po baryczy, Spływ kajakiem po baryczy,kajakiem po baryczy, Spływ kajakiem po baryczy, Spływ kajakiem po baryczy",
                    description = "Shor description hereShor description hereShor description hereShor description hereShor description hereShor description hereShor description hereShor description hereShor description hereShor description hereShor description here",
                    checklist = listOf(
                        ChecklistValueItem(true, "Item1"),
                        ChecklistValueItem(true, "Item2"),
                        ChecklistValueItem(false, "Item3"),
                        ChecklistValueItem(true, "Item4"),
                        ChecklistValueItem(false, "Item5"),
                        ChecklistValueItem(true, "Item6"),
                        ChecklistValueItem(false, "Item7"),
                        ChecklistValueItem(false, "Item8"),
                        ChecklistValueItem(false, "Item9 ")
                    ),
                    color = Color.Blue
                )
            ) {}
        }
    }
}




