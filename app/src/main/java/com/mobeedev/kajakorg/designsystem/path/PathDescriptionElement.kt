package com.mobeedev.kajakorg.designsystem.path

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme

@Composable
fun PathDescriptionElement(description: String) {
    KajakTheme {
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPathDescriptionElement() {
    KajakTheme {
        PathDescriptionElement("sdsadsadsa ds ad sad sa ds ad sad sadsadsadsa dsa d sad sa dsa d sad sa d asd sa d asd sa dsa d sa dsa dsa dsa d sa d  sa dsa d sa d sad sa dsa d sa dsa dsa d sada d as d sa d sa d sa dasdsadas das d")
    }
}