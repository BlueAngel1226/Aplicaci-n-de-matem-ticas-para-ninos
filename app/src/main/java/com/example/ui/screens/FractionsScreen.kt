package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.EvaluationStatus
import com.example.model.ExerciseMode
import com.example.model.FractionChartType
import com.example.model.FractionField
import com.example.model.FractionOperator
import com.example.model.FractionUiState
import com.example.ui.components.DynamicFractionChart
import com.example.ui.components.EvaluationFeedbackBanner
import com.example.ui.components.FlatSegmentedModeToggle
import com.example.ui.components.LiquidGlassKeyboard
import com.example.ui.components.LiquidGlassNumberSlot
import com.example.ui.components.LiquidGlassPanel
import com.example.ui.components.LiquidGlassPillButton
import com.example.ui.theme.FeedbackGreenBright
import com.example.ui.theme.FeedbackRedBright
import com.example.ui.theme.FlatCoral
import com.example.ui.theme.FlatIndigo
import com.example.ui.theme.FlatInkPrimary
import com.example.ui.theme.FlatInkSecondary
import com.example.ui.theme.FlatMint
import com.example.ui.theme.FlatSkyBlue
import com.example.ui.theme.FlatSunYellow
import com.example.ui.theme.FlatSurfaceWhite
import com.example.ui.theme.FredokaFontFamily

@Composable
fun FractionsScreen(
    state: FractionUiState,
    starsEarned: Int,
    onBack: () -> Unit,
    onSwitchMode: (ExerciseMode) -> Unit,
    onClearCreator: () -> Unit,
    onSelectField: (FractionField) -> Unit,
    onSelectOperator: (FractionOperator) -> Unit,
    onSelectChartType: (FractionChartType) -> Unit,
    onLockCreator: () -> Unit,
    onToggleNumeratorDenominator: () -> Unit,
    onStepField: (Boolean) -> Unit,
    onDigitPressed: (Int) -> Unit,
    onBackspacePressed: () -> Unit,
    onClearResult: () -> Unit,
    onVerifyAnswer: () -> Unit,
    onNextExercise: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .widthIn(max = 600.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Flat 2.0 Header & Segmented Mode Selector
            LiquidGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                tintColor = FlatSurfaceWhite,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LiquidGlassPillButton(
                                onClick = onBack,
                                accentColor = FlatCoral,
                                testTag = "fractions_back_button",
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.cd_back_button),
                                    tint = FlatInkPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Grado Cuarto • +, −, ×, ÷",
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = FlatCoral
                                )
                                Text(
                                    text = stringResource(R.string.module_grade_4_title),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontSize = 18.sp,
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

                    // Segmented Mode Toggle + Next/Clear Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FlatSegmentedModeToggle(
                            currentMode = state.mode,
                            onSelectMode = onSwitchMode,
                            systemTag = "fraction_mode_system",
                            creatorTag = "fraction_mode_creator",
                            activeColor = FlatIndigo,
                            modifier = Modifier.weight(1f)
                        )

                        LiquidGlassPillButton(
                            onClick = {
                                if (state.mode == ExerciseMode.SYSTEM_RANDOM) {
                                    onNextExercise()
                                } else {
                                    onClearCreator()
                                }
                            },
                            accentColor = FlatIndigo,
                            filledGlass = false,
                            testTag = "fraction_next_button",
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = FlatIndigo,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (state.mode == ExerciseMode.SYSTEM_RANDOM) "Nuevo" else "Vaciar",
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FlatIndigo
                                )
                            }
                        }
                    }
                }
            }

            // Flat Vector Graphical Representation Panel (Pie Chart & Segmented Bar Chart)
            LiquidGlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dynamic_charts_panel"),
                cornerRadius = 20.dp,
                tintColor = FlatSurfaceWhite,
                shadowElevation = 3.dp,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val chartOptions = listOf(
                            FractionChartType.AUTO to "Auto (Pastel/Barras)",
                            FractionChartType.PIE to "Pastel",
                            FractionChartType.BAR to "Barras"
                        )
                        chartOptions.forEach { (type, label) ->
                            val isSelected = state.chartType == type
                            LiquidGlassPillButton(
                                onClick = { onSelectChartType(type) },
                                accentColor = FlatSkyBlue,
                                filledGlass = isSelected,
                                testTag = "chart_type_${type.name}",
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isSelected) Color.White else FlatInkPrimary
                                )
                            }
                        }
                    }

                    // Dynamic Real-Time Flat Vector Charts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DynamicFractionChart(
                            numerator = state.num1,
                            denominator = state.den1,
                            chartType = state.chartType,
                            fillColor = FlatIndigo,
                            label = "Fracción 1",
                            testTag = "fraction_chart_1",
                            chartSize = 76.dp,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = state.operator.symbol,
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = FlatInkPrimary
                        )

                        DynamicFractionChart(
                            numerator = state.num2,
                            denominator = state.den2,
                            chartType = state.chartType,
                            fillColor = FlatCoral,
                            label = "Fracción 2",
                            testTag = "fraction_chart_2",
                            chartSize = 76.dp,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "=",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = FlatInkPrimary
                        )

                        DynamicFractionChart(
                            numerator = state.resultNum,
                            denominator = state.resultDen,
                            chartType = state.chartType,
                            fillColor = when (state.evaluationStatus) {
                                EvaluationStatus.CORRECT -> FeedbackGreenBright
                                EvaluationStatus.INCORRECT -> FeedbackRedBright
                                EvaluationStatus.IDLE -> FlatMint
                            },
                            label = "Resultado",
                            testTag = "fraction_chart_result",
                            chartSize = 76.dp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Classic Fraction Structure Panel with Semáforo 2.0
            LiquidGlassPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fraction_result_container"),
                cornerRadius = 20.dp,
                tintColor = FlatSurfaceWhite,
                evaluationStatus = state.evaluationStatus,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Operator selector buttons (+, -, ×, ÷)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FractionOperator.entries.forEach { op ->
                            val isSelected = state.operator == op
                            LiquidGlassPillButton(
                                onClick = { onSelectOperator(op) },
                                accentColor = FlatIndigo,
                                filledGlass = isSelected,
                                testTag = "fraction_op_${op.name}",
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${op.symbol} ${op.label}",
                                    fontFamily = FredokaFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else FlatInkPrimary
                                )
                            }
                        }
                    }

                    // Classic Fraction Equation Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ClassicFractionColumn(
                            numeratorText = state.num1Input,
                            denominatorText = state.den1Input,
                            isNumeratorFocused = state.activeField == FractionField.NUM_1 && state.mode == ExerciseMode.CREATOR_MANUAL,
                            isDenominatorFocused = state.activeField == FractionField.DEN_1 && state.mode == ExerciseMode.CREATOR_MANUAL,
                            isEnabled = state.mode == ExerciseMode.CREATOR_MANUAL,
                            evaluationStatus = EvaluationStatus.IDLE,
                            accentColor = FlatIndigo,
                            numeratorTag = "fraction_num1_slot",
                            denominatorTag = "fraction_den1_slot",
                            fractionLabel = "Primera fracción",
                            onNumeratorClick = { onSelectField(FractionField.NUM_1) },
                            onDenominatorClick = { onSelectField(FractionField.DEN_1) }
                        )

                        Text(
                            text = state.operator.symbol,
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = FlatInkPrimary
                        )

                        ClassicFractionColumn(
                            numeratorText = state.num2Input,
                            denominatorText = state.den2Input,
                            isNumeratorFocused = state.activeField == FractionField.NUM_2 && state.mode == ExerciseMode.CREATOR_MANUAL,
                            isDenominatorFocused = state.activeField == FractionField.DEN_2 && state.mode == ExerciseMode.CREATOR_MANUAL,
                            isEnabled = state.mode == ExerciseMode.CREATOR_MANUAL,
                            evaluationStatus = EvaluationStatus.IDLE,
                            accentColor = FlatCoral,
                            numeratorTag = "fraction_num2_slot",
                            denominatorTag = "fraction_den2_slot",
                            fractionLabel = "Segunda fracción",
                            onNumeratorClick = { onSelectField(FractionField.NUM_2) },
                            onDenominatorClick = { onSelectField(FractionField.DEN_2) }
                        )

                        Text(
                            text = "=",
                            fontFamily = FredokaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = FlatInkPrimary
                        )

                        ClassicFractionColumn(
                            numeratorText = state.resultNumInput,
                            denominatorText = state.resultDenInput,
                            isNumeratorFocused = state.activeField == FractionField.RESULT_NUM,
                            isDenominatorFocused = state.activeField == FractionField.RESULT_DEN,
                            isEnabled = state.isResultEnabled,
                            evaluationStatus = state.evaluationStatus,
                            accentColor = FlatIndigo,
                            numeratorTag = "fraction_result_num_slot",
                            denominatorTag = "fraction_result_den_slot",
                            fractionLabel = "Fracción resultado",
                            onNumeratorClick = { onSelectField(FractionField.RESULT_NUM) },
                            onDenominatorClick = { onSelectField(FractionField.RESULT_DEN) }
                        )
                    }

                    if (state.mode == ExerciseMode.CREATOR_MANUAL && state.areOperandsValid &&
                        state.activeField != FractionField.RESULT_NUM && state.activeField != FractionField.RESULT_DEN
                    ) {
                        LiquidGlassPillButton(
                            onClick = onLockCreator,
                            accentColor = FlatIndigo,
                            filledGlass = true,
                            testTag = "fraction_creator_lock_button",
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.btn_lock_creator),
                                fontFamily = FredokaFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
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
            showFractionToggle = true,
            isEditingNumerator = state.activeField.isNumerator,
            onToggleNumeratorDenominator = onToggleNumeratorDenominator,
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

@Composable
private fun ClassicFractionColumn(
    numeratorText: String,
    denominatorText: String,
    isNumeratorFocused: Boolean,
    isDenominatorFocused: Boolean,
    isEnabled: Boolean,
    evaluationStatus: EvaluationStatus,
    accentColor: Color,
    numeratorTag: String,
    denominatorTag: String,
    fractionLabel: String,
    onNumeratorClick: () -> Unit,
    onDenominatorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lineColor = when (evaluationStatus) {
        EvaluationStatus.CORRECT -> FeedbackGreenBright
        EvaluationStatus.INCORRECT -> FeedbackRedBright
        EvaluationStatus.IDLE -> FlatInkPrimary
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        LiquidGlassNumberSlot(
            valueText = numeratorText,
            placeholder = "?",
            isFocused = isNumeratorFocused,
            isEnabled = isEnabled,
            evaluationStatus = evaluationStatus,
            accentColor = accentColor,
            fontSize = 24.sp,
            minWidth = 68.dp,
            height = 48.dp,
            testTag = numeratorTag,
            contentDescriptionLabel = "$fractionLabel numerador",
            onClick = onNumeratorClick
        )

        // Solid Horizontal Dividing Bar (Línea divisoria horizontal sólida)
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(50))
                .background(lineColor)
        )

        LiquidGlassNumberSlot(
            valueText = denominatorText,
            placeholder = "?",
            isFocused = isDenominatorFocused,
            isEnabled = isEnabled,
            evaluationStatus = evaluationStatus,
            accentColor = accentColor,
            fontSize = 24.sp,
            minWidth = 68.dp,
            height = 48.dp,
            testTag = denominatorTag,
            contentDescriptionLabel = "$fractionLabel denominador",
            onClick = onDenominatorClick
        )
    }
}
