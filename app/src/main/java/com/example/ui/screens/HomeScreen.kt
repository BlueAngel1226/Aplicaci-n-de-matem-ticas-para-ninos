package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ExerciseMode
import com.example.model.MathSection
import com.example.ui.components.LiquidGlassPillButton
import com.example.ui.theme.FlatBackgroundNeutral
import com.example.ui.theme.FlatCoral
import com.example.ui.theme.FlatCoralSoft
import com.example.ui.theme.FlatIndigo
import com.example.ui.theme.FlatIndigoSoft
import com.example.ui.theme.FlatInkPrimary
import com.example.ui.theme.FlatInkSecondary
import com.example.ui.theme.FlatMint
import com.example.ui.theme.FlatSunYellow
import com.example.ui.theme.FlatSunYellowSoft
import com.example.ui.theme.FlatSurfaceSubtle
import com.example.ui.theme.FlatSurfaceWhite
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.NunitoFontFamily
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Componente HomeScreen con estilo Flat Design 2.0:
 * - Contenedor principal con color de fondo '#F8FAFC'.
 * - Dos tarjetas principales (Card) con bordes redondeados de 20.dp y sombra de elevación de 4.dp.
 * - Diseño interno con iconos vectoriales, gráficos planos y textos claros para
 *   'Sumas y Restas' y 'Fracciones y Gráficas', funcionando como botones de navegación.
 */
@Composable
fun HomeScreen(
    starsEarned: Int,
    onSelectSection: (MathSection) -> Unit,
    onQuickStartAddSub: (ExerciseMode) -> Unit,
    onQuickStartFractions: (ExerciseMode) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)) // Contenedor principal con fondo #F8FAFC
            .safeDrawingPadding()
    ) {
        val isExpandedScreen = maxWidth >= 680.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera superior Flat Design 2.0
            FlatHeaderBar(
                starsEarned = starsEarned,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 840.dp)
            )

            // Subtítulo guía
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 840.dp),
                shape = RoundedCornerShape(16.dp),
                color = FlatBackgroundNeutral,
                shadowElevation = 2.dp
            ) {
                Text(
                    text = stringResource(R.string.home_choose_module),
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = FlatInkPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            // DOS TARJETAS PRINCIPALES (Cards con 20.dp de borde redondeado y 4.dp de elevación)
            if (isExpandedScreen) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 840.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    FlatModuleNavigationCard(
                        badgeText = stringResource(R.string.module_grade_2_badge),
                        rangeOrOpsTag = "1 al 99 • + y −",
                        title = "Sumas y Restas",
                        description = stringResource(R.string.module_grade_2_desc),
                        vectorIcon = Icons.Default.Calculate,
                        primaryAccent = FlatIndigo,
                        softAccentBg = FlatIndigoSoft,
                        cardTestTag = "card_grade_2_sumas_restas",
                        systemButtonTag = "home_grade2_system_btn",
                        creatorButtonTag = "home_grade2_creator_btn",
                        illustrationContent = {
                            FlatSumasRestasIllustration(modifier = Modifier.size(100.dp))
                        },
                        onCardClick = { onSelectSection(MathSection.GRADE_2_ADD_SUB) },
                        onSystemModeClick = { onQuickStartAddSub(ExerciseMode.SYSTEM_RANDOM) },
                        onCreatorModeClick = { onQuickStartAddSub(ExerciseMode.CREATOR_MANUAL) },
                        modifier = Modifier.weight(1f)
                    )

                    FlatModuleNavigationCard(
                        badgeText = stringResource(R.string.module_grade_4_badge),
                        rangeOrOpsTag = "Pastel y Barras • +, −, ×, ÷",
                        title = "Fracciones y Gráficas",
                        description = stringResource(R.string.module_grade_4_desc),
                        vectorIcon = Icons.Default.PieChart,
                        primaryAccent = FlatCoral,
                        softAccentBg = FlatCoralSoft,
                        cardTestTag = "card_grade_4_fracciones",
                        systemButtonTag = "home_grade4_system_btn",
                        creatorButtonTag = "home_grade4_creator_btn",
                        illustrationContent = {
                            FlatFraccionesIllustration(modifier = Modifier.size(100.dp))
                        },
                        onCardClick = { onSelectSection(MathSection.GRADE_4_FRACTIONS) },
                        onSystemModeClick = { onQuickStartFractions(ExerciseMode.SYSTEM_RANDOM) },
                        onCreatorModeClick = { onQuickStartFractions(ExerciseMode.CREATOR_MANUAL) },
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tarjeta 1: 'Sumas y Restas' (Grado Segundo)
                    FlatModuleNavigationCard(
                        badgeText = stringResource(R.string.module_grade_2_badge),
                        rangeOrOpsTag = "1 al 99 • + y −",
                        title = "Sumas y Restas",
                        description = stringResource(R.string.module_grade_2_desc),
                        vectorIcon = Icons.Default.Calculate,
                        primaryAccent = FlatIndigo,
                        softAccentBg = FlatIndigoSoft,
                        cardTestTag = "card_grade_2_sumas_restas",
                        systemButtonTag = "home_grade2_system_btn",
                        creatorButtonTag = "home_grade2_creator_btn",
                        illustrationContent = {
                            FlatSumasRestasIllustration(modifier = Modifier.size(100.dp))
                        },
                        onCardClick = { onSelectSection(MathSection.GRADE_2_ADD_SUB) },
                        onSystemModeClick = { onQuickStartAddSub(ExerciseMode.SYSTEM_RANDOM) },
                        onCreatorModeClick = { onQuickStartAddSub(ExerciseMode.CREATOR_MANUAL) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Tarjeta 2: 'Fracciones y Gráficas' (Grado Cuarto)
                    FlatModuleNavigationCard(
                        badgeText = stringResource(R.string.module_grade_4_badge),
                        rangeOrOpsTag = "Pastel y Barras • +, −, ×, ÷",
                        title = "Fracciones y Gráficas",
                        description = stringResource(R.string.module_grade_4_desc),
                        vectorIcon = Icons.Default.PieChart,
                        primaryAccent = FlatCoral,
                        softAccentBg = FlatCoralSoft,
                        cardTestTag = "card_grade_4_fracciones",
                        systemButtonTag = "home_grade4_system_btn",
                        creatorButtonTag = "home_grade4_creator_btn",
                        illustrationContent = {
                            FlatFraccionesIllustration(modifier = Modifier.size(100.dp))
                        },
                        onCardClick = { onSelectSection(MathSection.GRADE_4_FRACTIONS) },
                        onSystemModeClick = { onQuickStartFractions(ExerciseMode.SYSTEM_RANDOM) },
                        onCreatorModeClick = { onQuickStartFractions(ExerciseMode.CREATOR_MANUAL) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FlatHeaderBar(
    starsEarned: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("home_header_panel"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FlatSurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.app_name),
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = FlatIndigo
                )
                Text(
                    text = stringResource(R.string.home_subtitle),
                    fontFamily = NunitoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = FlatInkSecondary,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .testTag("home_stars_badge")
                    .clip(RoundedCornerShape(16.dp))
                    .background(FlatSunYellowSoft)
                    .border(1.5.dp, FlatSunYellow, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrellas ganadas",
                        tint = FlatSunYellow,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$starsEarned",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = FlatInkPrimary
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta principal (Card) de navegación estilo Flat Design 2.0:
 * - Bordes redondeados de 20.dp (RoundedCornerShape(20.dp))
 * - Sombra de elevación de 4.dp (CardDefaults.cardElevation(defaultElevation = 4.dp))
 * - Icono vectorial plano, ilustración vectorial y textos de alto contraste
 */
@Composable
private fun FlatModuleNavigationCard(
    badgeText: String,
    rangeOrOpsTag: String,
    title: String,
    description: String,
    vectorIcon: ImageVector,
    primaryAccent: Color,
    softAccentBg: Color,
    cardTestTag: String,
    systemButtonTag: String,
    creatorButtonTag: String,
    illustrationContent: @Composable () -> Unit,
    onCardClick: () -> Unit,
    onSystemModeClick: () -> Unit,
    onCreatorModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Micro-interacción Flat 2.0: depresión física al tocar la tarjeta
    val pressOffsetY by animateDpAsState(
        targetValue = if (isPressed) 2.5.dp else 0.dp,
        animationSpec = tween(durationMillis = 90),
        label = "card_press_offset"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isPressed) softAccentBg.copy(alpha = 0.55f) else FlatSurfaceWhite,
        animationSpec = tween(durationMillis = 100),
        label = "card_bg_transition"
    )

    val cardShape = RoundedCornerShape(20.dp)

    Card(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onCardClick()
        },
        modifier = modifier
            .testTag(cardTestTag)
            .heightIn(min = 230.dp)
            .graphicsLayer { translationY = pressOffsetY.toPx() }
            .border(
                BorderStroke(1.5.dp, if (isPressed) primaryAccent else FlatSurfaceSubtle),
                shape = cardShape
            ),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp,
            focusedElevation = 4.dp,
            hoveredElevation = 5.dp
        ),
        interactionSource = interactionSource
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    // Franja inferior de acento Flat 2.0
                    val barHeight = 5.dp.toPx()
                    drawRect(
                        color = primaryAccent,
                        topLeft = Offset(0f, size.height - barHeight),
                        size = Size(size.width, barHeight)
                    )
                }
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Fila principal: Icono vectorial + Insignias + Título claro + Descripción + Gráfico vectorial
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono vectorial en insignia circular plana
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(primaryAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = vectorIcon,
                                contentDescription = title,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        FlatChipBadge(
                            text = badgeText,
                            backgroundColor = primaryAccent,
                            textColor = Color.White
                        )
                        FlatChipBadge(
                            text = rangeOrOpsTag,
                            backgroundColor = softAccentBg,
                            textColor = primaryAccent
                        )
                    }

                    Text(
                        text = title,
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        lineHeight = 31.sp,
                        color = FlatInkPrimary
                    )

                    Text(
                        text = description,
                        fontFamily = NunitoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = FlatInkSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                illustrationContent()
            }

            // Botón principal de navegación integrado dentro de la tarjeta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(primaryAccent)
                    .padding(horizontal = 16.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ir a $title",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            // Botones de acceso directo a Modo Sistema y Modo Creador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidGlassPillButton(
                    onClick = onSystemModeClick,
                    accentColor = primaryAccent,
                    filledGlass = false,
                    testTag = systemButtonTag,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 9.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = primaryAccent,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Modo Sistema",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = FlatInkPrimary
                        )
                    }
                }

                LiquidGlassPillButton(
                    onClick = onCreatorModeClick,
                    accentColor = FlatCoral,
                    filledGlass = false,
                    testTag = creatorButtonTag,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 9.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = FlatCoral,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Modo Creador",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = FlatInkPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlatChipBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = textColor
        )
    }
}

@Composable
private fun FlatSumasRestasIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawCircle(
            color = FlatIndigoSoft,
            radius = min(w, h) * 0.48f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        val plusBoxSize = Size(w * 0.45f, h * 0.45f)
        val plusTopLeft = Offset(w * 0.10f, h * 0.10f)
        drawRoundRect(
            color = FlatIndigo,
            topLeft = plusTopLeft,
            size = plusBoxSize,
            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
        )
        val plusCx = plusTopLeft.x + plusBoxSize.width / 2f
        val plusCy = plusTopLeft.y + plusBoxSize.height / 2f
        val arm = plusBoxSize.width * 0.24f
        drawLine(
            color = Color.White,
            start = Offset(plusCx - arm, plusCy),
            end = Offset(plusCx + arm, plusCy),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.White,
            start = Offset(plusCx, plusCy - arm),
            end = Offset(plusCx, plusCy + arm),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )

        val minusBoxSize = Size(w * 0.45f, h * 0.45f)
        val minusTopLeft = Offset(w * 0.45f, h * 0.45f)
        drawRoundRect(
            color = FlatMint,
            topLeft = minusTopLeft,
            size = minusBoxSize,
            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
        )
        val minusCx = minusTopLeft.x + minusBoxSize.width / 2f
        val minusCy = minusTopLeft.y + minusBoxSize.height / 2f
        drawLine(
            color = Color.White,
            start = Offset(minusCx - arm, minusCy),
            end = Offset(minusCx + arm, minusCy),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun FlatFraccionesIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawCircle(
            color = FlatCoralSoft,
            radius = min(w, h) * 0.48f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        val pieCenter = Offset(w * 0.50f, h * 0.38f)
        val pieRadius = w * 0.26f
        val pieTopLeft = Offset(pieCenter.x - pieRadius, pieCenter.y - pieRadius)
        val pieSize = Size(pieRadius * 2f, pieRadius * 2f)

        drawCircle(
            color = FlatSurfaceWhite,
            radius = pieRadius,
            center = pieCenter
        )
        drawArc(
            color = FlatCoral,
            startAngle = -90f,
            sweepAngle = 270f,
            useCenter = true,
            topLeft = pieTopLeft,
            size = pieSize
        )
        for (i in 0 until 4) {
            val angleRad = Math.toRadians((-90 + i * 90).toDouble())
            drawLine(
                color = FlatSurfaceWhite,
                start = pieCenter,
                end = Offset(
                    pieCenter.x + (pieRadius * cos(angleRad)).toFloat(),
                    pieCenter.y + (pieRadius * sin(angleRad)).toFloat()
                ),
                strokeWidth = 2.dp.toPx()
            )
        }
        drawCircle(
            color = FlatCoral,
            radius = pieRadius,
            center = pieCenter,
            style = Stroke(width = 2.dp.toPx())
        )

        val barWidth = w * 0.74f
        val barHeight = h * 0.16f
        val barLeft = (w - barWidth) / 2f
        val barTop = h * 0.72f
        val corner = CornerRadius(6.dp.toPx(), 6.dp.toPx())

        drawRoundRect(
            color = FlatSurfaceWhite,
            topLeft = Offset(barLeft, barTop),
            size = Size(barWidth, barHeight),
            cornerRadius = corner
        )
        val segW = barWidth / 3f
        for (s in 0 until 2) {
            drawRect(
                color = if (s == 0) FlatIndigo else FlatMint,
                topLeft = Offset(barLeft + s * segW + 1f, barTop + 1f),
                size = Size(segW - 2f, barHeight - 2f)
            )
        }
        for (s in 1..2) {
            val x = barLeft + s * segW
            drawLine(
                color = FlatSurfaceWhite,
                start = Offset(x, barTop),
                end = Offset(x, barTop + barHeight),
                strokeWidth = 2.dp.toPx()
            )
        }
        drawRoundRect(
            color = FlatIndigo,
            topLeft = Offset(barLeft, barTop),
            size = Size(barWidth, barHeight),
            cornerRadius = corner,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
