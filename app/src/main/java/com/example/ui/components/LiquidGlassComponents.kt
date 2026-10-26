package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EvaluationStatus
import com.example.model.ExerciseMode
import com.example.ui.theme.FeedbackGreenBorder
import com.example.ui.theme.FeedbackGreenBright
import com.example.ui.theme.FeedbackGreenGlass
import com.example.ui.theme.FeedbackGreenInk
import com.example.ui.theme.FeedbackRedBorder
import com.example.ui.theme.FeedbackRedBright
import com.example.ui.theme.FeedbackRedGlass
import com.example.ui.theme.FeedbackRedInk
import com.example.ui.theme.FlatBackgroundIvory
import com.example.ui.theme.FlatBackgroundNeutral
import com.example.ui.theme.FlatBorderLight
import com.example.ui.theme.FlatCoral
import com.example.ui.theme.FlatCoralDark
import com.example.ui.theme.FlatCoralSoft
import com.example.ui.theme.FlatIndigo
import com.example.ui.theme.FlatIndigoDark
import com.example.ui.theme.FlatIndigoSoft
import com.example.ui.theme.FlatInkMuted
import com.example.ui.theme.FlatInkPrimary
import com.example.ui.theme.FlatInkSecondary
import com.example.ui.theme.FlatMint
import com.example.ui.theme.FlatMintDark
import com.example.ui.theme.FlatSurfaceSubtle
import com.example.ui.theme.FlatSurfaceWhite
import com.example.ui.theme.FredokaFontFamily

/**
 * 1. FLAT DESIGN 2.0 CLEAN BACKGROUND:
 * Restful, uncluttered Ivory White (#F8FAFC) & Neutral Gray (#F1F5F9) surface
 * for maximum concentration and visual clarity for primary school children.
 */
@Composable
fun LiquidGlassBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FlatBackgroundIvory),
        content = content
    )
}

/**
 * 1. FLAT DESIGN 2.0 ELEVATED CARD / PANEL:
 * - Clean solid planes with subtle short-range physical elevation (2.dp to 6.dp).
 * - Rounded friendly geometry (16.dp to 20.dp).
 * - Supports Semáforo 2.0 state (Emerald Green #10B981 with bounce / Coral Red #EF4444 with lateral shake).
 */
@Composable
fun LiquidGlassPanel(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    tintColor: Color = FlatSurfaceWhite,
    borderTint: Color? = null,
    borderWidth: Dp = 1.5.dp,
    shadowElevation: Dp = 4.dp,
    evaluationStatus: EvaluationStatus = EvaluationStatus.IDLE,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val shakeOffset = remember { Animatable(0f) }
    val bounceScale = remember { Animatable(1f) }

    LaunchedEffect(evaluationStatus) {
        when (evaluationStatus) {
            EvaluationStatus.CORRECT -> {
                shakeOffset.snapTo(0f)
                bounceScale.animateTo(
                    targetValue = 1.03f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                )
                bounceScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                )
            }
            EvaluationStatus.INCORRECT -> {
                bounceScale.snapTo(1f)
                val shakes = listOf(-10f, 10f, -8f, 8f, -5f, 5f, 0f)
                for (dx in shakes) {
                    shakeOffset.animateTo(dx, animationSpec = tween(durationMillis = 34))
                }
            }
            EvaluationStatus.IDLE -> {
                shakeOffset.snapTo(0f)
                bounceScale.snapTo(1f)
            }
        }
    }

    val safeCorner = cornerRadius.coerceIn(16.dp, 20.dp)
    val shape = RoundedCornerShape(safeCorner)

    val surfaceColor by animateColorAsState(
        targetValue = when (evaluationStatus) {
            EvaluationStatus.CORRECT -> FeedbackGreenGlass
            EvaluationStatus.INCORRECT -> FeedbackRedGlass
            EvaluationStatus.IDLE -> tintColor
        },
        animationSpec = tween(180),
        label = "flat_panel_bg"
    )

    val strokeColor by animateColorAsState(
        targetValue = when (evaluationStatus) {
            EvaluationStatus.CORRECT -> FeedbackGreenBright
            EvaluationStatus.INCORRECT -> FeedbackRedBright
            EvaluationStatus.IDLE -> borderTint ?: FlatSurfaceSubtle
        },
        animationSpec = tween(180),
        label = "flat_panel_border"
    )

    val effectiveShadow = shadowElevation.coerceIn(2.dp, 6.dp)

    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = shakeOffset.value
                scaleX = bounceScale.value
                scaleY = bounceScale.value
            }
            .shadow(
                elevation = effectiveShadow,
                shape = shape,
                ambientColor = Color(0x180F172A),
                spotColor = Color(0x220F172A)
            )
            .clip(shape)
            .background(surfaceColor)
            .border(
                width = if (evaluationStatus != EvaluationStatus.IDLE) 2.5.dp else borderWidth,
                color = strokeColor,
                shape = shape
            )
            .padding(contentPadding),
        content = content
    )
}

/**
 * 1. FLAT DESIGN 2.0 BUTTON WITH ELEVATION DEPRESSION MICRO-INTERACTION:
 * Reacts to touch with crisp color transition and physical elevation depression
 * (the button lowers when pressed and recovers its shadow when released).
 */
@Composable
fun LiquidGlassPillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accentColor: Color = FlatIndigo,
    contentColor: Color = FlatInkPrimary,
    filledGlass: Boolean = false,
    testTag: String? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Elevation depression micro-interaction: lowers physically on press
    val pressOffsetY by animateDpAsState(
        targetValue = if (isPressed && enabled) 2.5.dp else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "btn_press_offset"
    )

    val dynamicElevation by animateDpAsState(
        targetValue = if (!enabled) 0.dp else if (isPressed) 1.dp else 4.dp,
        animationSpec = tween(durationMillis = 80),
        label = "btn_elevation"
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            !enabled -> FlatBackgroundNeutral
            filledGlass -> if (isPressed) darkenColor(accentColor, 0.88f) else accentColor
            else -> if (isPressed) FlatBackgroundNeutral else FlatSurfaceWhite
        },
        animationSpec = tween(durationMillis = 100),
        label = "btn_bg_color"
    )

    val borderColor = when {
        !enabled -> FlatSurfaceSubtle
        filledGlass -> darkenColor(accentColor, 0.80f)
        else -> FlatBorderLight
    }

    val shape = RoundedCornerShape(16.dp)
    val finalModifier = if (testTag != null) modifier.testTag(testTag) else modifier

    Box(
        modifier = finalModifier
            .minimumInteractiveComponentSize()
            .graphicsLayer {
                translationY = pressOffsetY.toPx()
            }
            .shadow(
                elevation = dynamicElevation,
                shape = shape,
                ambientColor = Color(0x1F0F172A),
                spotColor = Color(0x290F172A)
            )
            .clip(shape)
            .background(bgColor)
            .drawBehind {
                // Subtle Flat 2.0 tactile bottom lip that compresses when pressed
                if (!isPressed && enabled) {
                    val lipHeight = 3.dp.toPx()
                    drawRoundRect(
                        color = borderColor,
                        topLeft = Offset(0f, size.height - lipHeight),
                        size = Size(size.width, lipHeight),
                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                    )
                }
            }
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalContentColor(
            color = if (!enabled) FlatInkMuted else if (filledGlass) Color.White else contentColor,
            content = content
        )
    }
}

private fun darkenColor(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}

@Composable
private fun CompositionLocalContentColor(
    color: Color,
    content: @Composable () -> Unit
) {
    androidx.compose.runtime.CompositionLocalProvider(
        androidx.compose.material3.LocalContentColor provides color,
        content = content
    )
}

/**
 * 2. SEGMENTED MODE TOGGLE (Selector Segmentado Flat 2.0):
 * Allows switching cleanly between Modo Sistema (Aleatorio) and Modo Creador (Manual).
 */
@Composable
fun FlatSegmentedModeToggle(
    currentMode: ExerciseMode,
    onSelectMode: (ExerciseMode) -> Unit,
    systemTag: String,
    creatorTag: String,
    activeColor: Color = FlatIndigo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(FlatBackgroundNeutral)
            .border(1.5.dp, FlatSurfaceSubtle, RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isSystem = currentMode == ExerciseMode.SYSTEM_RANDOM
        LiquidGlassPillButton(
            onClick = { onSelectMode(ExerciseMode.SYSTEM_RANDOM) },
            accentColor = activeColor,
            filledGlass = isSystem,
            testTag = systemTag,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
        ) {
            Text(
                text = "Modo Sistema",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isSystem) Color.White else FlatInkSecondary
            )
        }

        val isCreator = currentMode == ExerciseMode.CREATOR_MANUAL
        LiquidGlassPillButton(
            onClick = { onSelectMode(ExerciseMode.CREATOR_MANUAL) },
            accentColor = FlatCoral,
            filledGlass = isCreator,
            testTag = creatorTag,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
        ) {
            Text(
                text = "Modo Creador",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isCreator) Color.White else FlatInkSecondary
            )
        }
    }
}

/**
 * 3 & 4. FLAT 2.0 INPUT BOX & SEMÁFORO 2.0 RESULT BOX:
 * - Rounded 16.dp geometry with clean flat border.
 * - Respuesta Correcta: Changes immediately to Flat Emerald Green (#10B981) with a bounce animation and check icon (✔).
 * - Respuesta Incorrecta: Changes immediately to Flat Coral Red (#EF4444) with a short lateral vibration.
 */
@Composable
fun LiquidGlassNumberSlot(
    valueText: String,
    placeholder: String,
    isFocused: Boolean,
    isEnabled: Boolean = true,
    evaluationStatus: EvaluationStatus = EvaluationStatus.IDLE,
    accentColor: Color = FlatIndigo,
    fontSize: TextUnit = 30.sp,
    minWidth: Dp = 74.dp,
    height: Dp = 60.dp,
    testTag: String,
    contentDescriptionLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val shakeOffset = remember { Animatable(0f) }
    val bounceScale = remember { Animatable(1f) }

    LaunchedEffect(evaluationStatus) {
        when (evaluationStatus) {
            EvaluationStatus.CORRECT -> {
                shakeOffset.snapTo(0f)
                bounceScale.animateTo(
                    targetValue = 1.14f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                )
                bounceScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                )
            }
            EvaluationStatus.INCORRECT -> {
                bounceScale.snapTo(1f)
                val shakes = listOf(-10f, 10f, -8f, 8f, -5f, 5f, 0f)
                for (dx in shakes) {
                    shakeOffset.animateTo(dx, animationSpec = tween(durationMillis = 32))
                }
            }
            EvaluationStatus.IDLE -> {
                shakeOffset.snapTo(0f)
                bounceScale.snapTo(1f)
            }
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = when {
            evaluationStatus == EvaluationStatus.CORRECT -> FeedbackGreenBright
            evaluationStatus == EvaluationStatus.INCORRECT -> FeedbackRedBright
            !isEnabled -> FlatBackgroundNeutral
            isFocused -> FlatIndigoSoft
            else -> FlatBackgroundIvory
        },
        animationSpec = tween(160),
        label = "slot_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            evaluationStatus == EvaluationStatus.CORRECT -> FeedbackGreenBorder
            evaluationStatus == EvaluationStatus.INCORRECT -> FeedbackRedBorder
            !isEnabled -> FlatSurfaceSubtle
            isFocused -> accentColor
            else -> FlatBorderLight
        },
        animationSpec = tween(160),
        label = "slot_border"
    )

    val textColor = when {
        evaluationStatus == EvaluationStatus.CORRECT -> Color.White
        evaluationStatus == EvaluationStatus.INCORRECT -> Color.White
        !isEnabled -> FlatInkMuted
        valueText.isEmpty() -> FlatInkMuted
        else -> FlatInkPrimary
    }

    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = shakeOffset.value
                scaleX = bounceScale.value
                scaleY = bounceScale.value
            }
            .testTag(testTag)
            .semantics { contentDescription = contentDescriptionLabel }
            .minimumInteractiveComponentSize()
            .widthIn(min = minWidth)
            .height(height)
            .shadow(
                elevation = if (isFocused || evaluationStatus != EvaluationStatus.IDLE) 4.dp else 2.dp,
                shape = shape,
                ambientColor = Color(0x180F172A),
                spotColor = Color(0x220F172A)
            )
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = if (isFocused || evaluationStatus != EvaluationStatus.IDLE) 2.5.dp else 1.5.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(
                enabled = isEnabled,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onClick()
                }
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val display = if (valueText.isEmpty()) placeholder else valueText
            Text(
                text = display,
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                color = textColor,
                textAlign = TextAlign.Center
            )
            if (evaluationStatus == EvaluationStatus.CORRECT) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Correcto",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * 4. SEMÁFORO 2.0 FEEDBACK BANNER:
 * Displays clear pedagogical feedback in Flat Emerald Green (#10B981) with check icon (✔)
 * or Flat Coral Red (#EF4444) with 1-tap clear/retry button.
 */
@Composable
fun EvaluationFeedbackBanner(
    evaluationStatus: EvaluationStatus,
    message: String,
    onRetryOrClear: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    LiquidGlassPanel(
        modifier = modifier
            .fillMaxWidth()
            .testTag("evaluation_feedback_banner"),
        cornerRadius = 18.dp,
        tintColor = FlatSurfaceWhite,
        evaluationStatus = evaluationStatus,
        shadowElevation = 3.dp,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val badgeBg = when (evaluationStatus) {
                EvaluationStatus.CORRECT -> FeedbackGreenBright
                EvaluationStatus.INCORRECT -> FeedbackRedBright
                EvaluationStatus.IDLE -> FlatIndigo
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                when (evaluationStatus) {
                    EvaluationStatus.CORRECT -> Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Correcto",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    EvaluationStatus.INCORRECT -> Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Incorrecto",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    EvaluationStatus.IDLE -> Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Instrucción",
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            val textColor = when (evaluationStatus) {
                EvaluationStatus.CORRECT -> FeedbackGreenInk
                EvaluationStatus.INCORRECT -> FeedbackRedInk
                EvaluationStatus.IDLE -> FlatInkPrimary
            }

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                ),
                modifier = Modifier.weight(1f)
            )

            if (evaluationStatus == EvaluationStatus.INCORRECT && onRetryOrClear != null) {
                Spacer(modifier = Modifier.width(8.dp))
                LiquidGlassPillButton(
                    onClick = onRetryOrClear,
                    accentColor = FeedbackRedBright,
                    filledGlass = true,
                    testTag = "retry_clear_button",
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Borrar",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * 5. TECLADO TÁCTIL FLAT 2.0 (Integrated Numeric Keyboard in the lower third):
 * - Square/rectangular keys with 16.dp rounded corners and subtle 2.dp-3.dp elevation shadow.
 * - Digits 0 to 9 in neutral base color (#FFFFFF over #F1F5F9 tray).
 * - Differentiated Backspace/Delete key in flat Coral.
 * - Direct navigation jump key (Num ⇅ Den) for Fractions + elevated "Comprobar" action button.
 */
@Composable
fun LiquidGlassKeyboard(
    onDigitClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit,
    onVerifyClick: () -> Unit,
    showFractionToggle: Boolean = false,
    isEditingNumerator: Boolean = true,
    onToggleNumeratorDenominator: (() -> Unit)? = null,
    showFieldStepArrows: Boolean = false,
    onStepPreviousField: (() -> Unit)? = null,
    onStepNextField: (() -> Unit)? = null,
    accentColor: Color = FlatIndigo,
    modifier: Modifier = Modifier
) {
    LiquidGlassPanel(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("liquid_glass_keyboard"),
        cornerRadius = 20.dp,
        tintColor = FlatBackgroundNeutral,
        borderTint = FlatSurfaceSubtle,
        shadowElevation = 5.dp,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showFractionToggle || showFieldStepArrows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showFractionToggle && onToggleNumeratorDenominator != null) {
                        LiquidGlassPillButton(
                            onClick = onToggleNumeratorDenominator,
                            accentColor = FlatIndigo,
                            filledGlass = true,
                            testTag = "numpad_key_toggle_num_den",
                            modifier = Modifier.weight(1.6f),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapVert,
                                    contentDescription = "Alternar numerador y denominador",
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEditingNumerator) {
                                        "Numerador Activo (Saltar a Den ⬇)"
                                    } else {
                                        "Denominador Activo (Saltar a Num ⬆)"
                                    },
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    if (showFieldStepArrows && onStepPreviousField != null && onStepNextField != null) {
                        LiquidGlassPillButton(
                            onClick = onStepPreviousField,
                            accentColor = accentColor,
                            testTag = "numpad_prev_field",
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Casilla anterior",
                                tint = FlatInkPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        LiquidGlassPillButton(
                            onClick = onStepNextField,
                            accentColor = accentColor,
                            testTag = "numpad_next_field",
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Siguiente casilla",
                                tint = FlatInkPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Row 1: Flat 2.0 Neutral Keys 1..5
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                for (digit in 1..5) {
                    FlatNumericKey(
                        digit = digit,
                        onClick = { onDigitClick(digit) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 2: Flat 2.0 Neutral Keys 6..9, 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                for (digit in listOf(6, 7, 8, 9, 0)) {
                    FlatNumericKey(
                        digit = digit,
                        onClick = { onDigitClick(digit) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 3: Differentiated Backspace Key + Elevated "Comprobar" Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LiquidGlassPillButton(
                    onClick = onBackspaceClick,
                    accentColor = FlatCoral,
                    filledGlass = true,
                    testTag = "numpad_key_backspace",
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Borrar dígito",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Borrar",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }

                LiquidGlassPillButton(
                    onClick = onVerifyClick,
                    accentColor = FlatMint,
                    filledGlass = true,
                    testTag = "verify_answer_button",
                    modifier = Modifier.weight(1.65f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Comprobar",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Neutral Flat 2.0 Rectangular Numeric Key with 16.dp rounded corners and elevation depression on tap.
 */
@Composable
private fun FlatNumericKey(
    digit: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassPillButton(
        onClick = onClick,
        accentColor = FlatIndigo,
        contentColor = FlatInkPrimary,
        filledGlass = false,
        testTag = "numpad_key_$digit",
        modifier = modifier.heightIn(min = 48.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        Text(
            text = digit.toString(),
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = FlatInkPrimary
        )
    }
}
