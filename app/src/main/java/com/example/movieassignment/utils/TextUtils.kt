package com.example.movieassignment.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import java.util.regex.Pattern

object TextUtils {
    fun buildHighlightedText(
        fullText: String,
        query: String,
        higlightColor: Color
    ): AnnotatedString {
        val annotatedString = AnnotatedString.Builder(fullText)
        val pattern = Regex(Pattern.quote(query), RegexOption.IGNORE_CASE)

        for (match in pattern.findAll(fullText)) {
            annotatedString.addStyle(
                style = SpanStyle(color = higlightColor),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
        return annotatedString.toAnnotatedString()
    }
}