package com.mobeedev.kajakorg.designsystem.checklist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChecklistSeparatorElement(
    index: Int,
    onDeleteClicked: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    KajakTheme {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp, bottom = 8.dp)
                .combinedClickable(onLongClick = {
                    onDeleteClicked.invoke(index)
                }, onClick = {
                    onDeleteClicked.invoke(index)
                })
        ) {
            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 2.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewChecklistSeparatorElement() {
    KajakTheme {
        Surface {
            ChecklistSeparatorElement(1, { })
        }
    }
}
