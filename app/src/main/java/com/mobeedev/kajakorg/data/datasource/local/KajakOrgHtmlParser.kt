package com.mobeedev.kajakorg.data.datasource.local

import android.util.Log
import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.ID_REGEX
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import org.jsoup.nodes.Document


private const val TABLE_SELECTOR = "table"
private const val ROW_SELECTOR = "tr"
private const val NEW_LINE = "//n"

private const val ROW_OPENING_TAG = "<tr>"
private const val ROW_CLOSING_TAG = "</tr>"

private const val CELL_OPENING_TAG = "<td>"
private const val CELL_CLOSING_TAG = "</td>"

private const val PATH_OVERVIEW_ID_POSITION = 0
private const val PATH_OVERVIEW_NAME_POSITION = 1
private const val PATH_OVERVIEW_VERSION_CODE_POSITION = 2
private const val PATH_OVERVIEW_LENGTH_POSITION = 3
private const val PATH_OVERVIEW_NUMBER_OF_MAKERS_POSITION = 4
private const val PATH_OVERVIEW_NUMBER_OF_SECTIONS_POSITION = 5
private const val PATH_OVERVIEW_NUISANCE_POSITION = 6

object KajakOrgHtmlParser {

    fun parsePathHTML(response: Document): List<PathOverviewDto> {
        val pathsRawData =
            response.select(TABLE_SELECTOR).select(ROW_SELECTOR).map { it.toString() }
                .toMutableList()
                .apply {
                    removeFirst()
                }
        val pathsParsed = mutableListOf<PathOverviewDto>()

        pathsRawData.forEach { pathRaw: String ->
            val wipItem = pathRaw.replace(ROW_OPENING_TAG, String.empty)
                .replace(CELL_OPENING_TAG, String.empty)
                .replace(ROW_CLOSING_TAG, String.empty)
                .split(CELL_CLOSING_TAG)
                .map { it.replace(NEW_LINE, String.empty).trim() }

            pathsParsed.add(
                PathOverviewDto(
                    ID_REGEX.find(wipItem[7])?.value?.toInt()
                        ?: error("Path can not have null ID."),
                    wipItem[PATH_OVERVIEW_ID_POSITION],
                    wipItem[PATH_OVERVIEW_NAME_POSITION].toInt(),
                    wipItem[PATH_OVERVIEW_VERSION_CODE_POSITION].toDouble(),
                    wipItem[PATH_OVERVIEW_LENGTH_POSITION].toInt(),
                    wipItem[PATH_OVERVIEW_NUMBER_OF_MAKERS_POSITION].toInt(),
                    wipItem[PATH_OVERVIEW_NUMBER_OF_SECTIONS_POSITION],
                    wipItem[PATH_OVERVIEW_NUISANCE_POSITION]
                )
            )
        }

        Log.d("---PATH OVERVIEW LOADED---","")
        return pathsParsed
    }
}