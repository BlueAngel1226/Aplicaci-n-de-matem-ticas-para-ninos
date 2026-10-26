package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FractionChartType
import com.example.ui.theme.FlatBackgroundNeutral
import com.example.ui.theme.FlatBorderLight
import com.example.ui.theme.FlatCoral
import com.example.ui.theme.FlatIndigo
import com.example.ui.theme.FlatInkPrimary
import com.example.ui.theme.FlatInkSecondary
import com.example.ui.theme.FlatSunYellow
import com.example.ui.theme.FlatSurfaceWhite
import com.example.ui.theme.FredokaFontFamily
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * 3B. REPRESENTACIÓN GRÁFICA VECTORIAL PLANA (Flat Design 2.0):
 * - Gráfica de Pastel (Circular): Flat circle divided with precision into equal segments
 *   according to the denominator, with numerator sectors colored in a solid contrasting tone.
 * - Gráfica de Barras: Flat segmented rectangles with solid vivid colors.
 */
@Composable
fun DynamicFractionChart(
    numerator: Int?,
    denominator: Int?,
    chartType: FractionChartType,
    fillColor: Color,
    label: String,
    testTag: String,
    chartSize: Dp = 84.dp,
    modifier: Modifier = Modifier
) {
    val validNum = numerator != null && numerator >= 0
    val validDen = denominator != null && denominator in 1..24

    Column(
        modifier = modifier
            .testTag(testTag)
            .semantics {
                contentDescription = if (validNum && validDen) {
                    "Gráfica de $label: $numerator de $denominator partes"
                } else {
                    "Gráfica de $label pendiente de datos"
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!validNum || !validDen) {
            Box(
                modifier = Modifier.size(chartSize),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = min(size.width, size.height) * 0.42f
                    drawCircle(
                        color = FlatBackgroundNeutral,
                        radius = radius
                    )
                    drawCircle(
                        color = FlatBorderLight,
                        radius = radius,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                Text(
                    text = "?/?",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = FlatInkSecondary
                )
            }
        } else {
            val num = numerator!!
            val den = denominator!!.coerceIn(1, 24)
            val isImproper = num > den
            val effectiveUsePie = when (chartType) {
                FractionChartType.AUTO -> !isImproper
                FractionChartType.PIE -> true
                FractionChartType.BAR -> false
            }

            if (effectiveUsePie) {
                FlatPieChartCanvas(
                    numerator = num,
                    denominator = den,
                    fillColor = fillColor,
                    chartSize = chartSize
                )
            } else {
                FlatSegmentedBarsCanvas(
                    numerator = num,
                    denominator = den,
                    fillColor = fillColor,
                    chartSize = chartSize
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        val typeBadge = if (validNum && validDen) {
            val num = numerator!!
            val den = denominator!!
            val kind = if (num <= den) "Propia" else "Impropia"
            "$num/$den ($kind)"
        } else {
            label
        }

        Text(
            text = typeBadge,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = FlatInkPrimary
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FlatPieChartCanvas(
    numerator: Int,
    denominator: Int,
    fillColor: Color,
    chartSize: Dp
) {
    val wholePiesNeeded = if (numerator == 0) 1 else ceil(numerator.toFloat() / denominator.toFloat()).toInt().coerceIn(1, 4)

    Canvas(modifier = Modifier.size(chartSize)) {
        val w = size.width
        val h = size.height

        if (wholePiesNeeded == 1) {
            drawSingleFlatPie(
                center = Offset(w / 2f, h / 2f),
                radius = min(w, h) * 0.45f,
                filledSlices = numerator.coerceIn(0, denominator),
                totalSlices = denominator,
                fillColor = fillColor
            )
        } else {
            val cols = 2
            val rows = if (wholePiesNeeded > 2) 2 else 1
            val cellW = w / cols
            val cellH = h / rows
            val radius = min(cellW, cellH) * 0.41f
            var remaining = numerator

            for (i in 0 until wholePiesNeeded) {
                val col = i % cols
                val row = i / cols
                val cx = col * cellW + cellW / 2f
                val cy = row * cellH + cellH / 2f
                val sliceCount = remaining.coerceIn(0, denominator)
                remaining = (remaining - denominator).coerceAtLeast(0)

                drawSingleFlatPie(
                    center = Offset(cx, cy),
                    radius = radius,
                    filledSlices = sliceCount,
                    totalSlices = denominator,
                    fillColor = fillColor
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSingleFlatPie(
    center: Offset,
    radius: Float,
    filledSlices: Int,
    totalSlices: Int,
    fillColor: Color
) {
    val topLeft = Offset(center.x - radius, center.y - radius)
    val arcSize = Size(radius * 2f, radius * 2f)

    // Base neutral flat circle
    drawCircle(
        color = FlatBackgroundNeutral,
        radius = radius,
        center = center
    )

    val sweepPerSlice = 360f / totalSlices.toFloat()
    val startBaseAngle = -90f

    // Solid contrasting flat slices for numerator
    for (slice in 0 until totalSlices) {
        val angleStart = startBaseAngle + slice * sweepPerSlice
        val isFilled = slice < filledSlices
        drawArc(
            color = if (isFilled) fillColor else FlatBackgroundNeutral,
            startAngle = angleStart,
            sweepAngle = sweepPerSlice,
            useCenter = true,
            topLeft = topLeft,
            size = arcSize
        )
    }

    // Crisp flat white divider lines
    if (totalSlices > 1) {
        for (slice in 0 until totalSlices) {
            val angleRad = Math.toRadians((startBaseAngle + slice * sweepPerSlice).toDouble())
            val endX = center.x + (radius * cos(angleRad)).toFloat()
            val endY = center.y + (radius * sin(angleRad)).toFloat()
            drawLine(
                color = FlatSurfaceWhite,
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Butt
            )
        }
    }

    // Clean solid perimeter outline
    drawCircle(
        color = fillColor,
        radius = radius,
        center = center,
        style = Stroke(width = 2.dp.toPx())
    )
}

@Composable
private fun FlatSegmentedBarsCanvas(
    numerator: Int,
    denominator: Int,
    fillColor: Color,
    chartSize: Dp
) {
    val barsNeeded = if (numerator == 0) 1 else ceil(numerator.toFloat() / denominator.toFloat()).toInt().coerceIn(1, 4)

    Canvas(modifier = Modifier.size(chartSize)) {
        val w = size.width
        val h = size.height
        val horizontalPad = w * 0.05f
        val barWidth = w - horizontalPad * 2f
        val spacing = 6.dp.toPx()
        val totalBarsHeight = h * 0.78f
        val singleBarHeight = ((totalBarsHeight - spacing * (barsNeeded - 1)) / barsNeeded)
            .coerceIn(12.dp.toPx(), 30.dp.toPx())
        val startY = (h - (singleBarHeight * barsNeeded + spacing * (barsNeeded - 1))) / 2f

        var remainingToFill = numerator

        for (barIdx in 0 until barsNeeded) {
            val topY = startY + barIdx * (singleBarHeight + spacing)
            val filledInThisBar = remainingToFill.coerceIn(0, denominator)
            remainingToFill = (remainingToFill - denominator).coerceAtLeast(0)

            val segWidth = barWidth / denominator.toFloat()
            val corner = CornerRadius(6.dp.toPx(), 6.dp.toPx())

            // Base flat bar
            drawRoundRect(
                color = FlatBackgroundNeutral,
                topLeft = Offset(horizontalPad, topY),
                size = Size(barWidth, singleBarHeight),
                cornerRadius = corner
            )

            // Solid flat segments
            for (seg in 0 until denominator) {
                val segLeft = horizontalPad + seg * segWidth
                val isFilled = seg < filledInThisBar
                if (isFilled) {
                    drawRect(
                        color = fillColor,
                        topLeft = Offset(segLeft + 1f, topY + 1f),
                        size = Size((segWidth - 2f).coerceAtLeast(1f), (singleBarHeight - 2f).coerceAtLeast(1f))
                    )
                }

                // Solid white segment divider
                if (seg > 0) {
                    drawLine(
                        color = FlatSurfaceWhite,
                        start = Offset(segLeft, topY),
                        end = Offset(segLeft, topY + singleBarHeight),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }

            // Clean flat outline
            drawRoundRect(
                color = fillColor,
                topLeft = Offset(horizontalPad, topY),
                size = Size(barWidth, singleBarHeight),
                cornerRadius = corner,
                style = Stroke(width = 2.dp.toPx())
            )
        }
    }
}

/**
 * Flat Design 2.0 Tens & Ones (Decenas y Unidades) representation for 2nd Grade.
 */
@Composable
fun TensAndOnesVisualBar(
    number1: Int?,
    number2: Int?,
    operatorSymbol: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberBlocksPreview(
            number = number1,
            label = "1er Número",
            tensColor = FlatIndigo,
            onesColor = FlatSunYellow,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = operatorSymbol,
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = FlatInkPrimary,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
        NumberBlocksPreview(
            number = number2,
            label = "2do Número",
            tensColor = FlatCoral,
            onesColor = FlatSunYellow,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NumberBlocksPreview(
    number: Int?,
    label: String,
    tensColor: Color,
    onesColor: Color,
    modifier: Modifier = Modifier
) {
    val tens = if (number != null && number in 1..99) number / 10 else 0
    val ones = if (number != null && number in 1..99) number % 10 else 0

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            val w = size.width
            val h = size.height
            if (number == null || number !in 1..99) {
                drawRoundRect(
                    color = FlatBackgroundNeutral,
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
                return@Canvas
            }

            val rodWidth = 5.5.dp.toPx()
            val rodGap = 3.dp.toPx()
            val dotRadius = 3.dp.toPx()
            val totalRodsWidth = tens * (rodWidth + rodGap)
            val startX = ((w - totalRodsWidth - 36.dp.toPx()) / 2f).coerceAtLeast(4f)

            for (t in 0 until tens) {
                val x = startX + t * (rodWidth + rodGap)
                drawRoundRect(
                    color = tensColor,
                    topLeft = Offset(x, 3.dp.toPx()),
                    size = Size(rodWidth, h - 6.dp.toPx()),
                    cornerRadius = CornerRadius(2.5.dp.toPx(), 2.5.dp.toPx())
                )
            }

            val onesStartX = startX + totalRodsWidth + 6.dp.toPx()
            for (u in 0 until ones) {
                val col = u % 5
                val row = u / 5
                val cx = onesStartX + col * (dotRadius * 2.4f) + dotRadius
                val cy = (h * 0.30f) + row * (dotRadius * 2.6f)
                if (cx + dotRadius <= w) {
                    drawCircle(
                        color = onesColor,
                        radius = dotRadius,
                        center = Offset(cx, cy)
                    )
                }
            }
        }
        Text(
            text = if (number != null && number in 1..99) {
                "$tens dec. y $ones unid."
            } else {
                "$label (1-99)"
            },
            fontFamily = FredokaFontFamily,
            fontSize = 11.sp,
            color = FlatInkSecondary
        )
    }
}
