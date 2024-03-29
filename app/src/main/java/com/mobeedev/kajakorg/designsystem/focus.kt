package com.mobeedev.kajakorg.designsystem

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Custom scroll modifier extension which ties together focus listening and relocation requesting
 */
@ExperimentalFoundationApi
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.scrollToOnFocus() = composed {
    val scope = rememberCoroutineScope()

    val requester = remember { BringIntoViewRequester() }

    bringIntoViewRequester(requester).onFocusEvent {
        if (it.isFocused) {
            scope.launch {
                delay(250)
                requester.bringIntoView()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.focusOnEntry(ignoreImeVisibility: Boolean = false) = composed {
    val imeInsets = WindowInsets.isImeVisible
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {
        if (ignoreImeVisibility || imeInsets) {
            focusRequester.requestFocus()
        }
    }

    focusRequester(focusRequester = focusRequester)
}