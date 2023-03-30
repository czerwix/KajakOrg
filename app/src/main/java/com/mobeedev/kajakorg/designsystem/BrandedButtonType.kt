package com.mobeedev.kajakorg.designsystem


import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R

/**
 * A sealed classes hierarchy that contains different visual properties associated with each branded
 * button.
 * @param iconResId the resource id for the brand icon.
 * @param iconHeight the height of the icon.
 * @param backgroundColor the backgroundColor of the button. This color should contain the color
 * that a particular brand specifies in it's design specification.
 * @param textColor the color to be used for the text displayed within the button.
 * @param contentPadding the content padding to be applied to the button.
 */
sealed class BrandedButtonType(
    @DrawableRes val iconResId: Int,
    val iconHeight: Dp,
    val backgroundColor: Color,
    val textColor: Color,
    val contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        top = 8.dp,
        end = 16.dp,
        bottom = 8.dp,
    )
) {

    object Twitter {
        object DarkTwitterButton : BrandedButtonType(
            iconResId = R.drawable.twitter_logo_white,
            iconHeight = 20.dp,
            backgroundColor = Color(0xFF121212),
            textColor = Color.White
        )

        object LightTwitterButton : BrandedButtonType(
            iconResId = R.drawable.twitter_logo_white,
            iconHeight = 20.dp,
            backgroundColor = Color(0xFF1D9BF0),
            textColor = Color.White
        )
    }

    object Facebook {
        object DarkFacebookButton : BrandedButtonType(
            iconResId = R.drawable.facebook_logo_white,
            iconHeight = 25.dp,
            backgroundColor = Color(0xFF1877f2),
            textColor = Color.White
        )

    }

    object Instagram {
        object DarkInstagramButton : BrandedButtonType(
            iconResId = R.drawable.instagram_logo_gradient,
            iconHeight = 20.dp,
            backgroundColor = Color(0xFF333333),
            textColor = Color.White
        )

    }

    object Github {
        object LightGithubButton : BrandedButtonType(
            iconResId = R.drawable.github_logo_black,
            iconHeight = 40.dp,
            backgroundColor = Color.White,
            textColor = Color.Black,
            contentPadding = PaddingValues(horizontal = 16.dp)
        )

        object DarkGithubButton : BrandedButtonType(
            iconResId = R.drawable.github_logo_white,
            iconHeight = 40.dp,
            backgroundColor = Color(0xFF333333),
            textColor = Color.White,
            contentPadding = PaddingValues(horizontal = 16.dp)
        )
    }
}