package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AddSubField
import com.example.model.AddSubOperator
import com.example.model.AddSubUiState
import com.example.model.ExerciseMode
import com.example.ui.components.EvaluationFeedbackBanner
import com.example.ui.components.FlatSegmentedModeToggle
import com.example.ui.components.LiquidGlassKeyboard
import com.example.ui.components.LiquidGlassNumberSlot
import com.example.ui.components.LiquidGlassPanel
import com.example.ui.components.LiquidGlassPillButton
import com.example.ui.components.TensAndOnesVisualBar
import com.example.ui.theme.FlatCoral
import com.example.ui.theme.FlatIndigo
import com.example.ui.theme.FlatInkPrimary
import com.example.ui.theme.FlatInkSecondary
import com.example.ui.theme.FlatSunYellow
import com.example.ui.theme.FlatSurfaceWhite
import com.example.ui.theme.FredokaFontFamily

@Composable
fun AdditionSubtractionScreen(
    state: AddSubUiState,
    starsEarned: Int,
    onBack: () -> Unit,
    onSwitchMode: (ExerciseMode) -> Unit,
    onSelectField: (AddSubField) -> Unit,
    onSelectOperator: (AddSubOperator) -> Unit,
    onSwapOperands: () -> Unit,
    onLockCreator: () -> Unit,
    onDigitPressed: (Int) -> Unit,
    onBackspacePressed: () -> Unit,
    onClearResult: () -> Unit,
    onVerifyAnswer: () -> Unit,
    onNextExercise: () -> Unit,
    onStepField: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Flat 2.0 Header & Segmented Mode Selector
            LiquidGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                tintColor = FlatSurfaceWhite,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LiquidGlassPillButton(
                                onClick = onBack,
                                accentColor = FlatIndigo,
                                testTag = "add_sub_back_button",
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.cd_back_button),
                                    tint = FlatInkPrimary,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Grado Segundo • Números del 1 al 99",
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FlatIndigo
                                )
                                Text(
                                    text = stringResource(R.string.module_grade_2_title),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontSize = 20.sp,
                                        color = FlatInkPrimary
                                    )
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = FlatSunYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$starsEarned",
                                fontFamily = FredokaFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = FlatInkPrimary
                            )
                        }
                    }

                    // Segmented Toggle + Next/Reset button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FlatSegmentedModeToggle(
                            currentMode = state.mode,
                            onSelectMode = onSwitchMode,
                            systemTag = "mode_pill_system",
                            creatorTag = "mode_pill_creator",
                            activeColor = FlatIndigo,
                            modifier = Modifier.weight(1f)
                        )

                        LiquidGlassPillButton(
                            onClick = {
                                if (state.mode == ExerciseMode.SYSTEM_RANDOM) {
                                    onNextExercise()
                                } else {
                                    onSwitchMode(ExerciseMode.CREATOR_MANUAL)
                                }
                            },
                            accentColor = FlatIndigo,
                            filledGlass = false,
                            testTag = "add_sub_next_button",
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 9.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = FlatIndigo,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (state.mode == ExerciseMode.SYSTEM_RANDOM) "Nuevo" else "Limpiar",
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = FlatIndigo
                                )
                            }
                        }
                    }
                }
            }

            // Visual Tens & Ones Helper Panel
            LiquidGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp,
                tintColor = FlatSurfaceWhite,
                shadowElevation = 2.dp,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                TensAndOnesVisualBar(
                    number1 = state.operand1,
                    number2 = state.operand2,
                    operatorSymbol = state.operator.symbol
                )
            }

            // Main Equation & Result Container with Semáforo 2.0
            LiquidGlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_sub_result_container"),
                cornerRadius = 20.dp,
                tintColor = FlatSurfaceWhite,
                evaluationStatus = state.evaluationStatus,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Operator selector buttons (+ and - only)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Operación:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FlatInkSecondary
                            )
                        )
                        AddSubOperator.entries.forEach { op ->
                            val isSelected = state.operator == op
                            val opColor = if (op == AddSubOperator.ADD) FlatIndigo else FlatCoral
                            LiquidGlassPillButton(
                                onClick = { onSelectOperator(op) },
                                accentColor = opColor,
                                filledGlass = isSelected,
                                testTag = "op_button_${op.name}",
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${op.symbol} ${op.label}",
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else FlatInkPrimary
                                )
                            }
                        }
                    }

                    // Equation Row: [Operand 1]  (+/-)  [Operand 2]  =  [Result]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LiquidGlassNumberSlot(
                            valueText = state.operand1Input,
                            placeholder = "?",
                            isFocused = state.activeField == AddSubField.OPERAND_1 && state.mode == ExerciseMode.CREATOR_MANUAL,
                            isEnabled = state.mode == ExerciseMode.CREATOR_MANUAL,
                            accentColor = FlatIndigo,
                            fontSize = 30.sp,
                            minWidth = 72.dp,
                            height = 60.dp,
                            testTag = "add_sub_op1_slot",
                            contentDescriptionLabel = "Primer número",
                            onClick = { onSelectField(AddSubField.OPERAND_1) }
                        )

                        Text(
                            text = state.operator.symbol,
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            color = if (state.operator == AddSubOperator.ADD) FlatIndigo else FlatCoral
                        )

                        LiquidGlassNumberSlot(
                            valueText = state.operand2Input,
                            placeholder = "?",
                            isFocused = state.activeField == AddSubField.OPERAND_2 && state.mode == ExerciseMode.CREATOR_MANUAL,
                            isEnabled = state.mode == ExerciseMode.CREATOR_MANUAL,
                            accentColor = FlatIndigo,
                            fontSize = 30.sp,
                            minWidth = 72.dp,
                            height = 60.dp,
                            testTag = "add_sub_op2_slot",
                            contentDescriptionLabel = "Segundo número",
                            onClick = { onSelectField(AddSubField.OPERAND_2) }
                        )

                        Text(
                            text = "=",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = FlatInkPrimary
                        )

                        LiquidGlassNumberSlot(
                            valueText = state.resultInput,
                            placeholder = "?",
                            isFocused = state.activeField == AddSubField.RESULT,
                            isEnabled = state.isResultEnabled,
                            evaluationStatus = state.evaluationStatus,
                            accentColor = FlatIndigo,
                            fontSize = 30.sp,
                            minWidth = 86.dp,
                            height = 62.dp,
                            testTag = "add_sub_result_slot",
                            contentDescriptionLabel = "Resultado de la operación",
                            onClick = { onSelectField(AddSubField.RESULT) }
                        )
                    }

                    // Creator Mode helper buttons
                    if (state.mode == ExerciseMode.CREATOR_MANUAL) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (state.violatesSubtractionRule) {
                                LiquidGlassPillButton(
                                    onClick = onSwapOperands,
                                    accentColor = FlatSunYellow,
                                    filledGlass = true,
                                    testTag = "swap_numbers_button",
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.SwapHoriz,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = stringResource(R.string.btn_swap_numbers),
                                            fontFamily = FredokaFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            } else if (state.areOperandsValid && state.activeField != AddSubField.RESULT) {
                                LiquidGlassPillButton(
                                    onClick = onLockCreator,
                                    accentColor = FlatIndigo,
                                    filledGlass = true,
                                    testTag = "creator_lock_button",
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.btn_lock_creator),
                                        fontFamily = FredokaFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Semáforo 2.0 Banner
            EvaluationFeedbackBanner(
                evaluationStatus = state.evaluationStatus,
                message = state.feedbackMessage,
                onRetryOrClear = onClearResult
            )
        }

        // Bottom Integrated Teclado Táctil Flat 2.0
        LiquidGlassKeyboard(
            onDigitClick = onDigitPressed,
            onBackspaceClick = onBackspacePressed,
            onVerifyClick = onVerifyAnswer,
            showFractionToggle = false,
            showFieldStepArrows = state.mode == ExerciseMode.CREATOR_MANUAL,
            onStepPreviousField = { onStepField(false) },
            onStepNextField = { onStepField(true) },
            accentColor = FlatIndigo,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
        )
    }
}
