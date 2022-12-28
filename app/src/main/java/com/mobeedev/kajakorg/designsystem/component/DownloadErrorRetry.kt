package com.mobeedev.kajakorg.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme

@Composable
fun DownloadErrorRetry(
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    KajakTheme {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                Row(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.sync_problem_icon),
                        colorFilter = ColorFilter.tint(Color.Red),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(70.dp)
                            .width(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Text(
                        text = stringResource(R.string.could_not_load_path_data),
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                }
                Button(
                    onClick = { onRetryClicked() },
                    modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Red
                    )
                ) {
                    Text(
                        text = stringResource(R.string.retry_data_load),
                        color = Color.Blue,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun PreviewDownloadErrorRetry() {
    KajakTheme {
        Surface {
            DownloadErrorRetry(
                onRetryClicked = {}
            )
        }
    }
}