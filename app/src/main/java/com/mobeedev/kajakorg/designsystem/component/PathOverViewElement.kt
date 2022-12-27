package com.mobeedev.kajakorg.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.domain.model.detail.EventDto
import com.mobeedev.kajakorg.domain.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.detail.SectionDto
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.ui.theme.KajakOrgTheme

@Composable
fun PathOverViewElement(item: PathOverview, onClick: () -> Unit) {
    KajakOrgTheme {
        Box(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.kajak1), contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(70.dp)
                    .width(70.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

        }
    }
}

@Preview
@Composable
fun PreviewAlbumElement() {
    KajakOrgTheme {
        Surface {
            PathOverViewElement(
                PathOverview(
                   id = 1,
                    name = "spływ po rzece Wda",
                    versionCode = 1,
                    description = "Dłuuuuuuuuuusdusndusnadnsad sauidh sauidnsan dsajdi sd sadiosa ds dsoa disnadi said sadi sadio said sadio sadisa dsa doas dosa dosa doas dosa doas d",
                    sections = mutableListOf(SectionDto(id = 1),SectionDto(id = 2),SectionDto(id = 3)),
                    events = mutableListOf(EventDto(id = 1), EventDto(id = 2), EventDto(id = 3))
                ),
                onClick = {}
            )
        }
    }
}