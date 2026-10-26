package com.example

import android.graphics.Color as AndroidColor
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppScreen
import com.example.model.MathSection
import com.example.ui.components.LiquidGlassBackground
import com.example.ui.screens.AdditionSubtractionScreen
import com.example.ui.screens.FractionsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SectionSubMenuScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MathViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Requirement 5: Disable the native Android virtual keyboard completely
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                AndroidColor.TRANSPARENT,
                AndroidColor.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                AndroidColor.TRANSPARENT,
                AndroidColor.TRANSPARENT
            )
        )
        setContent {
            MyApplicationTheme {
                CristalMateApp()
            }
        }
    }
}

@Composable
fun CristalMateApp(
    viewModel: MathViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val starsEarned by viewModel.starsEarned.collectAsStateWithLifecycle()
    val addSubState by viewModel.addSubState.collectAsStateWithLifecycle()
    val fractionState by viewModel.fractionState.collectAsStateWithLifecycle()

    LiquidGlassBackground(modifier = Modifier.fillMaxSize()) {
        Crossfade(
            targetState = currentScreen,
            animationSpec = tween(durationMillis = 200),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                is AppScreen.Home -> {
                    HomeScreen(
                        starsEarned = starsEarned,
                        onSelectSection = { section -> viewModel.navigateToSubMenu(section) },
                        onQuickStartAddSub = { mode -> viewModel.openAdditionSubtraction(mode) },
                        onQuickStartFractions = { mode -> viewModel.openFractions(mode) }
                    )
                }

                is AppScreen.SubMenu -> {
                    SectionSubMenuScreen(
                        section = screen.section,
                        onSelectMode = { mode ->
                            when (screen.section) {
                                MathSection.GRADE_2_ADD_SUB -> viewModel.openAdditionSubtraction(mode)
                                MathSection.GRADE_4_FRACTIONS -> viewModel.openFractions(mode)
                            }
                        },
                        onBack = { viewModel.navigateToHome() }
                    )
                }

                is AppScreen.AdditionSubtraction -> {
                    AdditionSubtractionScreen(
                        state = addSubState,
                        starsEarned = starsEarned,
                        onBack = { viewModel.navigateBack() },
                        onSwitchMode = { mode -> viewModel.setAddSubMode(mode) },
                        onSelectField = { field -> viewModel.selectAddSubField(field) },
                        onSelectOperator = { op -> viewModel.selectAddSubOperator(op) },
                        onSwapOperands = { viewModel.swapAddSubOperands() },
                        onLockCreator = { viewModel.lockAddSubCreatorAndFocusResult() },
                        onDigitPressed = { digit -> viewModel.onAddSubDigitPressed(digit) },
                        onBackspacePressed = { viewModel.onAddSubBackspace() },
                        onClearResult = { viewModel.clearAddSubResult() },
                        onVerifyAnswer = { viewModel.verifyAddSubAnswer() },
                        onNextExercise = { viewModel.generateSystemAddSubExercise() },
                        onStepField = { forward -> viewModel.stepAddSubField(forward) }
                    )
                }

                is AppScreen.Fractions -> {
                    FractionsScreen(
                        state = fractionState,
                        starsEarned = starsEarned,
                        onBack = { viewModel.navigateBack() },
                        onSwitchMode = { mode -> viewModel.setFractionMode(mode) },
                        onClearCreator = { viewModel.clearFractionCreator() },
                        onSelectField = { field -> viewModel.selectFractionField(field) },
                        onSelectOperator = { op -> viewModel.selectFractionOperator(op) },
                        onSelectChartType = { chartType -> viewModel.selectFractionChartType(chartType) },
                        onLockCreator = { viewModel.lockFractionCreatorAndFocusResult() },
                        onToggleNumeratorDenominator = { viewModel.toggleNumeratorDenominator() },
                        onStepField = { forward -> viewModel.stepFractionField(forward) },
                        onDigitPressed = { digit -> viewModel.onFractionDigitPressed(digit) },
                        onBackspacePressed = { viewModel.onFractionBackspace() },
                        onClearResult = { viewModel.clearFractionResult() },
                        onVerifyAnswer = { viewModel.verifyFractionAnswer() },
                        onNextExercise = { viewModel.generateSystemFractionExercise() }
                    )
                }
            }
        }
    }
}
