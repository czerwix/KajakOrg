package com.mobeedev.kajakorg.designsystem.checklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.theme.KajakOrgTypography
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.ui.model.ChecklistValueItem
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistEditItem(
    item: ChecklistValueItem,
    index: Int,
    onCheckListener: (Int, Boolean) -> Unit,
    onTextChange: (Int, String) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    isCustomColorSelected: Boolean = false,
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
) {
    var note by remember { mutableStateOf(TextFieldValue(item.value)) }
    KajakTheme {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val contentColor = remember {
                if (isCustomColorSelected) Color.White else Color.Black
            }
            //checkbox
            KajakCheckbox(
                checked = item.isDone,
                onCheckedChange = {
                    onCheckListener.invoke(index, it)
                },
                checkedColor = contentColor,
                uncheckedColor = contentColor,
                checkmarkColor = contentColor,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(24.dp),
                cornerRadius = 2.dp
            )
            //input field
            TextField( //todo add hint
                value = note,
                onValueChange = {
                    onTextChange.invoke(index, it.text)
                    note = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = contentColor,
                    unfocusedTextColor = contentColor,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                textStyle = if (item.isDone) {
                    KajakOrgTypography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
                } else {
                    KajakOrgTypography.bodyMedium
                },
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 8.dp)
                    .weight(2f),
                placeholder = {
                    Text(stringResource(id = R.string.checklist_empty_checkList))
                }
            )
            //remove button
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp, top = 16.dp)
                    .size(24.dp)
                    .clickable { onDeleteClicked(index) },
                painter = painterResource(id = R.drawable.round_close_24),
                contentDescription = null,
                tint = contentColor
            )
        }
    }
}

@Preview
@Composable
fun PreviewChecklistEditItem() {
    KajakTheme {
        Surface {
            ChecklistEditItem(item = ChecklistValueItem(
                UUID.randomUUID(),
                true,
                "Item1"
            ), 0, { i, t -> }, { i, t -> }, {})
        }
    }
}
