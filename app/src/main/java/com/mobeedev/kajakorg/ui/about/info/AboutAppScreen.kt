package com.mobeedev.kajakorg.ui.about.info

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.BrandedButton
import com.mobeedev.kajakorg.designsystem.BrandedButtonType
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import java.util.Locale

@Composable
fun AboutAppRoute(
    modifier: Modifier = Modifier,
    navigateToExplain: () -> Unit
) {
    AboutAppScreen(modifier, navigateToExplain)
}

@Composable
fun AboutAppScreen(modifier: Modifier, navigateToExplain: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(id = R.string.about_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 8.dp, start = 16.dp, end = 16.dp
                )
        )
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.about_app),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 32.dp, start = 16.dp, end = 16.dp
                ),
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            BrandedButton(
                brandedButtonType = BrandedButtonType.Github.DarkGithubButton,
                label = stringResource(R.string.join_the_project),
                onClick = {
                    uriHandler.openUri("https://github.com/czerwix/KajakOrg")
                },
            )
        }
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.about_explain_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 32.dp, start = 16.dp, end = 16.dp
                ),
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = { uriHandler.openUri("https://pl.wikipedia.org/wiki/Szlak_kajakowy") },
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_school_24),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(id = R.string.about_explain_button),
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
        }
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.about_codebase_author),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 32.dp, start = 16.dp, end = 16.dp
                ),
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            BrandedButton(
                brandedButtonType = BrandedButtonType.Github.DarkGithubButton,
                label = stringResource(R.string.check_out_my_other_stuff),
                onClick = {
                    uriHandler.openUri("https://github.com/czerwix")
                },
            )
        }
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.about_path_authors),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 32.dp, start = 16.dp, end = 16.dp
                ),
        )
        Text(
            text = stringResource(id = R.string.about_path_authors_list),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 4.dp, start = 16.dp, end = 16.dp
                ),
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            BrandedButton(
                brandedButtonType = BrandedButtonType.Github.DarkGithubButton,
                label = stringResource(R.string.change_language_polish),
                onClick = {
                    changeLanguage(Locale("pl"), context)
                },
            )

            BrandedButton(
                modifier = Modifier.padding(top = 8.dp),
                brandedButtonType = BrandedButtonType.Github.DarkGithubButton,
                label = stringResource(R.string.change_language_english),
                onClick = {
                    changeLanguage(Locale("en"), context)
                },
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
    }
}

fun changeLanguage(locale: Locale, context: Context) {
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.locale = locale
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

@Preview
@Composable
fun previewAboutAppScreen() {
    KajakTheme {
        Surface {
            AboutAppScreen(Modifier, {})
        }
    }
}
