package com.mobeedev.kajakorg.designsystem.checklist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.ui.model.ChecklistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistDescriptionItem(
    item: ChecklistItem,
    onTextChanged: (String) -> Unit,
    isCustomColorSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    var note by remember { mutableStateOf(TextFieldValue(item.description)) }

    KajakTheme {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val contentColor = remember {
                if (isCustomColorSelected) Color.White else Color.Black
            }
            //input field
            TextField( //todo add hint
                value = note,
                onValueChange = {
                    onTextChanged(it.text.trim())
                    note = it
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = contentColor,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp),
                placeholder = {
                    Text(stringResource(id = R.string.checklist_empty_description))
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewChecklistDescriptionItem() {
    KajakTheme {
        Surface {
            ChecklistDescriptionItem(
                item = ChecklistItem(
                    title = "",
                    description = "Very long description Very long description Very long descriptionVery long descriptionVery long description"
                ), { })
        }
    }
}
