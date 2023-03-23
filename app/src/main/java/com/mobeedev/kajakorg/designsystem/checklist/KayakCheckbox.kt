package com.mobeedev.kajakorg.designsystem.checklist

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme

@Composable
fun KajakCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    checkedColor: Color = Color.Black,
    uncheckedColor: Color = Color.Black,
    checkmarkColor: Color = Color.Black,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp
) {
    val borderColor = remember {
        if (checked) checkedColor else uncheckedColor
    }
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius))
            .clickable {
                if (enabled) {
                    onCheckedChange?.invoke(checked.not())
                }
            }) {
        if (checked) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "", tint = checkmarkColor)
        }
    }
}

@Preview
@Composable
fun PreviewKajakCheckboxTrue() {
    KajakTheme {
        Surface {
            KajakCheckbox(
                checked = true, modifier = Modifier.size(10.dp), cornerRadius = 2.dp
            )
        }
    }
}

@Preview
@Composable
fun PreviewKajakCheckboxFalse() {
    KajakTheme {
        Surface {
            KajakCheckbox(checked = false, modifier = Modifier.size(30.dp))
        }
    }
}