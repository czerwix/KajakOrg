package com.mobeedev.kajakorg.designsystem.checklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistAddItem(
    onAddClicked: () -> Unit,
    onAddSeparatorClicked: () -> Unit,
    isCustomColorSelected: Boolean = false,
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
) {
    KajakTheme {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .focusTarget()
        ) {
            val contentColor = remember {
                if (isCustomColorSelected) Color.White else Color.Black
            }
            Row(modifier = Modifier
                .weight(0.8f)
                .clickable { onAddSeparatorClicked() }) {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.round_add_24),
                    contentDescription = null,
                    tint = contentColor
                )

                Text(
                    text = stringResource(id = R.string.add_checklist_separator),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .padding(start = 8.dp, top = 16.dp, bottom = 16.dp)
                )
            }

            Row(modifier = Modifier
                .weight(1f)
                .clickable { onAddClicked() }) {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.round_add_24),
                    contentDescription = null,
                    tint = contentColor
                )

                Text(
                    text = stringResource(id = R.string.add_checklist_item),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewChecklistAddItem() {
    KajakTheme {
        Surface {
            ChecklistAddItem({}, {})
        }
    }
}