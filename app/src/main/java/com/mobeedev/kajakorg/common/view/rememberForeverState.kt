package com.mobeedev.kajakorg.common.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * quick and hacky solution to remember stste forever when application is not killed. switch to saving to DB once i have more time
 */

/**
 * Static field, contains all scroll values
 */
private val LazyColumnSaveMap = mutableMapOf<String, LazyColumKeyParams>()

private data class LazyColumKeyParams(
    val params: String = "",
    val index: Int,
    val scrollOffset: Int
)

/**
 * Save scroll state on all time.
 * @param key value for comparing screen
 * @param params arguments for find different between equals screen
 * @param initialFirstVisibleItemIndex see [LazyListState.firstVisibleItemIndex]
 * @param initialFirstVisibleItemScrollOffset see [LazyListState.firstVisibleItemScrollOffset]
 */
@Composable
fun rememberForeverLazyListState(
    key: String,
    params: String = "",
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0
): LazyListState {
    val scrollState = rememberSaveable(saver = LazyListState.Saver) {
        var savedValue = LazyColumnSaveMap[key]
        if (savedValue?.params != params) savedValue = null
        val savedIndex = savedValue?.index ?: initialFirstVisibleItemIndex
        val savedOffset = savedValue?.scrollOffset ?: initialFirstVisibleItemScrollOffset
        LazyListState(
            savedIndex,
            savedOffset
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            val lastIndex = scrollState.firstVisibleItemIndex
            val lastOffset = scrollState.firstVisibleItemScrollOffset
            LazyColumnSaveMap[key] = LazyColumKeyParams(params, lastIndex, lastOffset)
        }
    }
    return scrollState
}


private val PagerSaveMap = mutableMapOf<String, PagerKeyParams>()

private data class PagerKeyParams(
    val params: String = "",
    val initialPage: Int,
    val initialPageOffSetFraction: Float
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberForeverPagerState(
    key: String,
    params: String = "",
    initialPage: Int = 0,
    initialPageOffSetFraction: Float = 0f
): PagerState {
    val pagerState = rememberSaveable(saver = PagerState.Saver) {
        var savedValue = PagerSaveMap[key]
        if (savedValue?.params != params) savedValue = null
        val savedIndex = savedValue?.initialPage ?: initialPage
        val savedOffset = savedValue?.initialPageOffSetFraction ?: initialPageOffSetFraction
        PagerState(
            savedIndex,
            savedOffset
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            val currentPage = pagerState.currentPage
            val currentOffset = pagerState.currentPageOffsetFraction
            PagerSaveMap[key] = PagerKeyParams(params, currentPage, currentOffset)
        }
    }

    return pagerState
}
