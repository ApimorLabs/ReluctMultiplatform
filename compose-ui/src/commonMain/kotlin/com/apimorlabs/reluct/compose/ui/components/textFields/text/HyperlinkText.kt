package com.apimorlabs.reluct.compose.ui.components.textFields.text

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import kotlinx.collections.immutable.ImmutableList

@Composable
fun HyperlinkText(
    fullText: String,
    hyperLinks: ImmutableList<HighlightTextProps>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fullTextColor: Color = LocalContentColor.current,
    textStyle: TextStyle = LocalTextStyle.current,
    linkTextDecoration: TextDecoration = TextDecoration.None,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    val annotatedString by remember(fullText, hyperLinks) {
        derivedStateOf {
            buildAnnotatedString {
                append(fullText)
                addStyle(
                    style = SpanStyle(
                        color = fullTextColor,
                        fontSize = textStyle.fontSize,
                        fontWeight = textStyle.fontWeight,
                        fontStyle = textStyle.fontStyle,
                        fontSynthesis = textStyle.fontSynthesis,
                        fontFamily = textStyle.fontFamily,
                        letterSpacing = textStyle.letterSpacing,
                        fontFeatureSettings = textStyle.fontFeatureSettings,
                        baselineShift = textStyle.baselineShift,
                        textGeometricTransform = textStyle.textGeometricTransform,
                        localeList = textStyle.localeList,
                        background = textStyle.background,
                        textDecoration = linkTextDecoration,
                        shadow = textStyle.shadow,

                        ),
                    start = 0,
                    end = fullText.lastIndex
                )
                hyperLinks.forEach { link ->
                    link.url?.let { url ->
                        withLink(
                            LinkAnnotation.Url(
                                url = url,
                                styles = TextLinkStyles(
                                    style = SpanStyle(
                                        color = link.color,
                                        fontSize = textStyle.fontSize,
                                        fontWeight = textStyle.fontWeight,
                                        fontStyle = textStyle.fontStyle,
                                        fontSynthesis = textStyle.fontSynthesis,
                                        fontFamily = textStyle.fontFamily,
                                        letterSpacing = textStyle.letterSpacing,
                                        fontFeatureSettings = textStyle.fontFeatureSettings,
                                        baselineShift = textStyle.baselineShift,
                                        textGeometricTransform = textStyle.textGeometricTransform,
                                        localeList = textStyle.localeList,
                                        background = textStyle.background,
                                        textDecoration = linkTextDecoration,
                                        shadow = textStyle.shadow,

                                        )
                                )
                            )
                        ) {
                            append(link.text)
                        }
                    }
                }
                addStyle(
                    style = ParagraphStyle(
                        textAlign = textAlign
                    ),
                    start = 0,
                    end = fullText.length
                )
            }
        }
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow
    )
}
