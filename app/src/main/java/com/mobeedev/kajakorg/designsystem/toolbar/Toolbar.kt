package com.mobeedev.kajakorg.designsystem.toolbar

//import androidx.compose.material.icons.rounded.PrivacyTip
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.space
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayEnd
import com.mobeedev.kajakorg.designsystem.theme.PathOverviewOverlayStart
import com.mobeedev.kajakorg.designsystem.theme.Teal700
import com.mobeedev.kajakorg.ui.model.PathOveriewItem
import kotlin.math.roundToInt

private val ContentPadding = 16.dp
private val TitleStartPadding = 8.dp
private val Elevation = 4.dp
private val ButtonSize = 34.dp
private const val Alpha = 0.75f

private val ExpandedPadding = 1.dp
private val ExpandedSectionsPadding = 4.dp
private val ExpandedBottomPadding = 16.dp
private val CollapsedPadding = 3.dp

val MinToolbarHeight = 60.dp
val MaxToolbarHeight = 360.dp

@Preview
@Composable
fun CollapsingToolbarCollapsedPreview() {
    KajakTheme {
        CollapsingToolbar(
            backgroundImageResId = R.drawable.kajak1,
            progress = 0f,
            onBackArrowButtonClicked = {},
            onMapButtonClicked = {},
            onStarButtonClicked = {},
            onSettingsButtonClicked = {},
            onDescriptionClicked = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(MinToolbarHeight),
            path = PathOveriewItem(
                id = 1,
                name = "Bystrzyca kłodzka",
                versionCode = 1,
                length = 200.0,
                numberOfMarkers = 99,
                numberOfSections = 60,
                difficulty = "D1,D2,D3vbvbvcvbvfdsfdsfdsfdsfdsfdsfcbvcbvcb",
                nuisance = "N1,N2,N3,N4,Ndsfdsfdsfdsfdsfdfdsfdsfdsfsd",
                description = "Dłuuuuuuuuuusdusndusnadnsad sauidh sauidnsan dsajdi sd sadiosa ds dsoa disnadi said sadi sadio said sadio sadisa dsa doas dosa dosa doas dosa doas d"
            ), {}
        )
    }
}

@Preview
@Composable
fun CollapsingToolbarHalfwayPreview() {
    KajakTheme {
        CollapsingToolbar(
            backgroundImageResId = R.drawable.kajak1,
            progress = 0.5f,
            onBackArrowButtonClicked = {},
            onMapButtonClicked = {},
            onStarButtonClicked = {},
            onSettingsButtonClicked = {},
            onDescriptionClicked = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            path = PathOveriewItem(
                id = 1,
                name = "Bystrzyca kłodzka",
                versionCode = 1,
                length = 200.0,
                numberOfMarkers = 99,
                numberOfSections = 60,
                difficulty = "D1,D2,D3vbvbvcvbvfdsfdsfdsfdsfdsfdsfcbvcbvcb",
                nuisance = "N1,N2,N3,N4,Ndsfdsfdsfdsfdsfdfdsfdsfdsfsd",
                description = "Dłuuuuuuuuuusdusndusnadnsad sauidh sauidnsan dsajdi sd sadiosa ds dsoa disnadi said sadi sadio said sadio sadisa dsa doas dosa dosa doas dosa doas d"
            ), {}
        )
    }
}

@Preview
@Composable
fun CollapsingToolbarExpandedPreview() {
    KajakTheme {
        CollapsingToolbar(
            backgroundImageResId = R.drawable.kajak1,
            progress = 1f,
            onBackArrowButtonClicked = {},
            onMapButtonClicked = {},
            onStarButtonClicked = {},
            onSettingsButtonClicked = {},
            onDescriptionClicked = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(MaxToolbarHeight),
            path = PathOveriewItem(
                id = 1,
                name = "Bystrzyca kłodzka",
                versionCode = 1,
                length = 200.0,
                numberOfMarkers = 99,
                numberOfSections = 60,
                difficulty = "D1,D2,D3vbvbvcvbvfdsfdsfdsfdsfdsfdsfcbvcbvcb",
                nuisance = "N1,N2,N3,N4,Ndsfdsfdsfdsfdsfdfdsfdsfdsfsd",
                description = "Dłuuuuuuuuuusdusndusnadnsad sauidh sauidnsan dsajdi sd sadiosa ds dsoa disnadi said sadi sadio said sadio sadisa dsa doas dosa dosa doas dosa doas d"
            ), {}
        )
    }
}

@Composable
fun CollapsingToolbar(
    @DrawableRes backgroundImageResId: Int,
    progress: Float,
    onBackArrowButtonClicked: () -> Unit,
    onMapButtonClicked: () -> Unit,
    onStarButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onDescriptionClicked: () -> Unit,
    modifier: Modifier = Modifier,
    path: PathOveriewItem,
    settingsButtonContent: @Composable () -> Unit
) {
    val logoPadding = lerp(CollapsedPadding, ExpandedPadding, progress)
    val bottomTitlePadding = lerp(CollapsedPadding, ExpandedBottomPadding, progress)
    val context = LocalContext.current
    val backgroundColor = remember {
        Palette.from(BitmapFactory.decodeResource(context.resources, backgroundImageResId))
            .generate().vibrantSwatch?.let { Color(it.rgb) } ?: Teal700
    }
    Surface(
        color = backgroundColor,
        shadowElevation = Elevation,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = backgroundImageResId),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = progress * Alpha
                    }
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                PathOverviewOverlayEnd,
                                PathOverviewOverlayStart
                            ),
                            startY = size.height / 3,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    },
                alignment = BiasAlignment(0f, 1f - ((1f - progress) * 0.75f))
            )
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = ContentPadding)
                    .fillMaxSize()
            ) {
                CollapsingToolbarLayout(progress = progress) {
                    //path details
                    Text(
                        text = if (progress >= 0.148112f) path.name else path.name.take(10),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 28.sp,
                        modifier = Modifier
                            .padding(bottom = bottomTitlePadding)
                            .wrapContentHeight()
                            .wrapContentWidth()
                    )
                    Text(
                        text = path.length.toString() + String.space + stringResource(id = R.string.at_kilometer),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(logoPadding)
                            .wrapContentSize()
                            .graphicsLayer { alpha = (-(0.25f - progress) * 4).coerceIn(0f, 1f) },
                    )
                    Text(
                        text = stringResource(id = R.string.number_of_sections) + String.space + path.numberOfSections,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(logoPadding)
                            .wrapContentSize()
                            .graphicsLayer { alpha = (-(0.25f - progress) * 4).coerceIn(0f, 1f) }
                    )
                    Text(
                        text = path.description,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 10,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(logoPadding)
                            .graphicsLayer { alpha = (-(0.5f - progress) * 4).coerceIn(0f, 1f) }
                            .clickable { onDescriptionClicked() }
                    )
                    //BackButton
                    IconButton(
                        onClick = onBackArrowButtonClicked,
                        modifier = Modifier
                            .size(ButtonSize)
                            .background(
                                color = LocalContentColor.current.copy(alpha = 0.0f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    //utility buttons
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(ContentPadding)
                    ) {
                        IconButton(
                            onClick = onMapButtonClicked,
                            modifier = Modifier
                                .size(ButtonSize)
                                .background(
                                    color = LocalContentColor.current.copy(alpha = 0.0f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(R.drawable.outline_map_24),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = onStarButtonClicked,
                            modifier = Modifier
                                .size(ButtonSize)
                                .background(
                                    color = LocalContentColor.current.copy(alpha = 0.0f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(R.drawable.outline_star_24),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Box(modifier = Modifier.wrapContentSize()) {
                            IconButton(
                                onClick = onSettingsButtonClicked,
                                modifier = Modifier
                                    .size(ButtonSize)
                                    .background(
                                        color = LocalContentColor.current.copy(alpha = 0.0f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    imageVector = Icons.Rounded.Settings,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            settingsButtonContent()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollapsingToolbarLayout(
    progress: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 6)
        // [0]: PathName
        // [1]: PathLength
        // [2]: PathNumberOfSections
        // [3]: PathDescription
        // [4]: BackArrow
        // [5]: Buttons Row

        val placeables = measurables.map {
            it.measure(constraints)
        }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {

            val collapsedHorizontalGuideline = (constraints.maxHeight * 0.5f).roundToInt()


            val riverName = placeables[0]
            val pathLength = placeables[1]
            val numberOfSections = placeables[2]
            val pathDescription = placeables[3]
            val backArrow = placeables[4]
            val buttons = placeables[5]

            riverName.placeRelative(
                x = androidx.compose.ui.util.lerp(
                    start = backArrow.width + TitleStartPadding.toPx().toInt(),
                    stop = 0,
                    fraction = progress
                ),
                y = androidx.compose.ui.util.lerp(
                    start = (constraints.maxHeight - riverName.height) / 2,
                    stop = constraints.maxHeight - riverName.height,
                    fraction = progress
                )
            )
            pathLength.placeRelative(
                x = androidx.compose.ui.util.lerp(
                    start = riverName.width,
                    stop = 0,
                    fraction = progress
                ),
                y = androidx.compose.ui.util.lerp(
                    start = collapsedHorizontalGuideline - pathLength.height / 2,
                    stop = constraints.maxHeight - riverName.height - pathLength.height,
                    fraction = progress
                )
            )
            numberOfSections.placeRelative(
                x = androidx.compose.ui.util.lerp(
                    start = riverName.width + pathLength.width,
                    stop = pathLength.width + ExpandedSectionsPadding.toPx().toInt(),
                    fraction = progress
                ),
                y = androidx.compose.ui.util.lerp(
                    start = collapsedHorizontalGuideline - numberOfSections.height / 2,
                    stop = constraints.maxHeight - riverName.height - numberOfSections.height,
                    fraction = progress
                )
            )
            pathDescription.placeRelative(
                x = 0,
                y = androidx.compose.ui.util.lerp(
                    start = -buttons.height,
                    stop = constraints.maxHeight - riverName.height - numberOfSections.height -
                            pathDescription.height - ExpandedSectionsPadding.toPx().toInt(),
                    fraction = progress
                )
            )
            backArrow.placeRelative(
                x = 0,
                y = (MinToolbarHeight.roundToPx() - backArrow.height) / 2,
            )
            buttons.placeRelative(
                x = constraints.maxWidth - buttons.width,
                y = (MinToolbarHeight.roundToPx() - buttons.height) / 2
            )
        }
    }
}
