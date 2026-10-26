package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.AddSubField
import com.example.model.AddSubOperator
import com.example.model.AddSubUiState
import com.example.model.AppScreen
import com.example.model.EvaluationStatus
import com.example.model.ExerciseMode
import com.example.model.FractionChartType
import com.example.model.FractionField
import com.example.model.FractionOperator
import com.example.model.FractionUiState
import com.example.model.MathLogic
import com.example.model.MathSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class MathViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Home)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _starsEarned = MutableStateFlow(0)
    val starsEarned: StateFlow<Int> = _starsEarned.asStateFlow()

    private val _addSubState = MutableStateFlow(AddSubUiState())
    val addSubState: StateFlow<AddSubUiState> = _addSubState.asStateFlow()

    private val _fractionState = MutableStateFlow(FractionUiState())
    val fractionState: StateFlow<FractionUiState> = _fractionState.asStateFlow()

    init {
        generateSystemAddSubExercise()
        generateSystemFractionExercise()
    }

    // Navigation actions
    fun navigateToHome() {
        _currentScreen.value = AppScreen.Home
    }

    fun navigateToSubMenu(section: MathSection) {
        _currentScreen.value = AppScreen.SubMenu(section)
    }

    fun openAdditionSubtraction(mode: ExerciseMode) {
        setAddSubMode(mode)
        _currentScreen.value = AppScreen.AdditionSubtraction(mode)
    }

    fun openFractions(mode: ExerciseMode) {
        setFractionMode(mode)
        _currentScreen.value = AppScreen.Fractions(mode)
    }

    fun navigateBack() {
        when (val screen = _currentScreen.value) {
            is AppScreen.Home -> Unit
            is AppScreen.SubMenu -> _currentScreen.value = AppScreen.Home
            is AppScreen.AdditionSubtraction -> _currentScreen.value = AppScreen.SubMenu(MathSection.GRADE_2_ADD_SUB)
            is AppScreen.Fractions -> _currentScreen.value = AppScreen.SubMenu(MathSection.GRADE_4_FRACTIONS)
        }
    }

    // =========================================================================
    // MODULE A: 2nd Grade - Addition & Subtraction (1..99, no negative results)
    // =========================================================================

    fun setAddSubMode(mode: ExerciseMode) {
        if (mode == ExerciseMode.SYSTEM_RANDOM) {
            generateSystemAddSubExercise()
        } else {
            _addSubState.value = AddSubUiState(
                mode = ExerciseMode.CREATOR_MANUAL,
                operand1Input = "",
                operand2Input = "",
                operator = AddSubOperator.ADD,
                resultInput = "",
                activeField = AddSubField.OPERAND_1,
                creatorLocked = false,
                evaluationStatus = EvaluationStatus.IDLE,
                feedbackMessage = "Escribe los dos números (del 1 al 99) y elige + o -."
            )
        }
    }

    fun generateSystemAddSubExercise() {
        val op = if (Random.nextBoolean()) AddSubOperator.ADD else AddSubOperator.SUBTRACT
        val first: Int
        val second: Int
        if (op == AddSubOperator.ADD) {
            first = Random.nextInt(1, 100)  // 1..99
            second = Random.nextInt(1, 100) // 1..99
        } else {
            // Mandatory logic rule: minuendo >= sustraendo (both in 1..99)
            val a = Random.nextInt(1, 100)
            val b = Random.nextInt(1, 100)
            first = maxOf(a, b)
            second = minOf(a, b)
        }
        _addSubState.value = AddSubUiState(
            mode = ExerciseMode.SYSTEM_RANDOM,
            operand1Input = first.toString(),
            operand2Input = second.toString(),
            operator = op,
            resultInput = "",
            activeField = AddSubField.RESULT,
            creatorLocked = true,
            evaluationStatus = EvaluationStatus.IDLE,
            feedbackMessage = "¡Resuelve la ${op.label.lowercase()} usando el teclado de cristal!"
        )
    }

    fun setAddSubProblemForTest(op1: Int, op2: Int, operator: AddSubOperator, mode: ExerciseMode = ExerciseMode.SYSTEM_RANDOM) {
        _addSubState.value = AddSubUiState(
            mode = mode,
            operand1Input = op1.toString(),
            operand2Input = op2.toString(),
            operator = operator,
            resultInput = "",
            activeField = AddSubField.RESULT,
            creatorLocked = mode == ExerciseMode.SYSTEM_RANDOM,
            evaluationStatus = EvaluationStatus.IDLE,
            feedbackMessage = ""
        )
    }

    fun selectAddSubField(field: AddSubField) {
        _addSubState.update { state ->
            when (field) {
                AddSubField.OPERAND_1, AddSubField.OPERAND_2 -> {
                    if (state.mode == ExerciseMode.CREATOR_MANUAL) {
                        state.copy(
                            activeField = field,
                            creatorLocked = false,
                            evaluationStatus = EvaluationStatus.IDLE
                        )
                    } else state
                }
                AddSubField.RESULT -> {
                    if (state.isResultEnabled) {
                        state.copy(
                            activeField = AddSubField.RESULT,
                            creatorLocked = true
                        )
                    } else {
                        state.copy(
                            feedbackMessage = if (state.violatesSubtractionRule) {
                                "En la resta, el primer número debe ser mayor o igual al segundo."
                            } else {
                                "Primero completa ambos números (del 1 al 99) antes de escribir el resultado."
                            }
                        )
                    }
                }
            }
        }
    }

    fun selectAddSubOperator(operator: AddSubOperator) {
        _addSubState.update { state ->
            if (state.mode == ExerciseMode.SYSTEM_RANDOM) {
                // In System mode, changing operator generates a valid exercise with that operator
                val a = Random.nextInt(1, 100)
                val b = Random.nextInt(1, 100)
                val first = if (operator == AddSubOperator.SUBTRACT) maxOf(a, b) else a
                val second = if (operator == AddSubOperator.SUBTRACT) minOf(a, b) else b
                state.copy(
                    operator = operator,
                    operand1Input = first.toString(),
                    operand2Input = second.toString(),
                    resultInput = "",
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "¡Resuelve la ${operator.label.lowercase()}!"
                )
            } else {
                val updated = state.copy(
                    operator = operator,
                    resultInput = "",
                    evaluationStatus = EvaluationStatus.IDLE
                )
                val msg = if (updated.violatesSubtractionRule) {
                    "Atención: El primer número (${updated.operand1}) es menor que el segundo (${updated.operand2})."
                } else if (updated.areOperandsValid) {
                    "¡Operación lista! Toca la casilla de resultado o '¡Listo para Resolver!'."
                } else {
                    "Escribe números del 1 al 99 en cada casilla."
                }
                updated.copy(
                    activeField = if (!updated.areOperandsValid && updated.activeField == AddSubField.RESULT) {
                        AddSubField.OPERAND_1
                    } else updated.activeField,
                    feedbackMessage = msg
                )
            }
        }
    }

    fun swapAddSubOperands() {
        _addSubState.update { state ->
            if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
            val swapped = state.copy(
                operand1Input = state.operand2Input,
                operand2Input = state.operand1Input,
                resultInput = "",
                evaluationStatus = EvaluationStatus.IDLE
            )
            swapped.copy(
                activeField = if (swapped.areOperandsValid) AddSubField.RESULT else AddSubField.OPERAND_1,
                creatorLocked = swapped.areOperandsValid,
                feedbackMessage = "¡Números invertidos! Ahora el minuendo es mayor o igual al sustraendo."
            )
        }
    }

    fun lockAddSubCreatorAndFocusResult() {
        _addSubState.update { state ->
            if (state.areOperandsValid) {
                state.copy(
                    activeField = AddSubField.RESULT,
                    creatorLocked = true,
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "¡Ahora ingresa el resultado y presiona Verificar!"
                )
            } else if (state.violatesSubtractionRule) {
                state.copy(
                    feedbackMessage = "El primer número debe ser mayor o igual al segundo en una resta."
                )
            } else {
                state.copy(
                    feedbackMessage = "Ingresa dos números válidos entre 1 y 99."
                )
            }
        }
    }

    fun stepAddSubField(forward: Boolean) {
        _addSubState.update { state ->
            if (state.mode == ExerciseMode.SYSTEM_RANDOM) return@update state
            val order = if (state.isResultEnabled) {
                listOf(AddSubField.OPERAND_1, AddSubField.OPERAND_2, AddSubField.RESULT)
            } else {
                listOf(AddSubField.OPERAND_1, AddSubField.OPERAND_2)
            }
            val idx = order.indexOf(state.activeField).coerceAtLeast(0)
            val nextIdx = if (forward) (idx + 1) % order.size else (idx - 1 + order.size) % order.size
            val target = order[nextIdx]
            state.copy(
                activeField = target,
                creatorLocked = target == AddSubField.RESULT
            )
        }
    }

    fun onAddSubDigitPressed(digit: Int) {
        _addSubState.update { state ->
            when (state.activeField) {
                AddSubField.OPERAND_1 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    val candidate = (state.operand1Input + digit.toString()).trimStart('0')
                    if (candidate.isEmpty() || candidate.length > 2) return@update state // 1..99
                    val next = state.copy(
                        operand1Input = candidate,
                        evaluationStatus = EvaluationStatus.IDLE
                    )
                    next.copy(
                        feedbackMessage = when {
                            next.violatesSubtractionRule -> "En la resta, el primer número debe ser mayor o igual al segundo."
                            next.areOperandsValid -> "¡Listo! Toca el resultado o presiona '¡Listo para Resolver!'."
                            else -> "Ahora ingresa el segundo número (1 al 99)."
                        }
                    )
                }
                AddSubField.OPERAND_2 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    val candidate = (state.operand2Input + digit.toString()).trimStart('0')
                    if (candidate.isEmpty() || candidate.length > 2) return@update state // 1..99
                    val next = state.copy(
                        operand2Input = candidate,
                        evaluationStatus = EvaluationStatus.IDLE
                    )
                    next.copy(
                        feedbackMessage = when {
                            next.violatesSubtractionRule -> "En la resta, el primer número debe ser mayor o igual al segundo."
                            next.areOperandsValid -> "¡Operación válida! Toca la casilla de resultado para resolver."
                            else -> "Completa el primer número (1 al 99)."
                        }
                    )
                }
                AddSubField.RESULT -> {
                    if (!state.isResultEnabled) return@update state
                    val base = if (state.evaluationStatus != EvaluationStatus.IDLE && state.resultInput.length >= 3) "" else state.resultInput
                    val candidate = if (base == "0") digit.toString() else base + digit.toString()
                    if (candidate.length > 3) return@update state
                    state.copy(
                        resultInput = candidate,
                        evaluationStatus = EvaluationStatus.IDLE,
                        feedbackMessage = "Presiona 'Verificar Respuesta' para comprobar."
                    )
                }
            }
        }
    }

    fun onAddSubBackspace() {
        _addSubState.update { state ->
            when (state.activeField) {
                AddSubField.OPERAND_1 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    state.copy(
                        operand1Input = state.operand1Input.dropLast(1),
                        resultInput = "",
                        evaluationStatus = EvaluationStatus.IDLE
                    )
                }
                AddSubField.OPERAND_2 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    state.copy(
                        operand2Input = state.operand2Input.dropLast(1),
                        resultInput = "",
                        evaluationStatus = EvaluationStatus.IDLE
                    )
                }
                AddSubField.RESULT -> {
                    state.copy(
                        resultInput = state.resultInput.dropLast(1),
                        evaluationStatus = EvaluationStatus.IDLE,
                        feedbackMessage = "Corrige tu respuesta y vuelve a verificar."
                    )
                }
            }
        }
    }

    fun clearAddSubResult() {
        _addSubState.update { state ->
            state.copy(
                resultInput = "",
                activeField = AddSubField.RESULT,
                evaluationStatus = EvaluationStatus.IDLE,
                feedbackMessage = "Casilla borrada. ¡Inténtalo de nuevo!"
            )
        }
    }

    fun verifyAddSubAnswer() {
        _addSubState.update { state ->
            val expected = state.expectedAnswer
            val userAns = state.resultValue
            if (expected == null) {
                return@update state.copy(
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = if (state.violatesSubtractionRule) {
                        "Corrige la resta: el primer número debe ser mayor o igual al segundo."
                    } else {
                        "Completa primero los dos números de la operación (del 1 al 99)."
                    }
                )
            }
            if (userAns == null) {
                return@update state.copy(
                    activeField = AddSubField.RESULT,
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "Escribe un número en la casilla de resultado antes de verificar."
                )
            }
            if (userAns == expected) {
                _starsEarned.update { it + 1 }
                state.copy(
                    evaluationStatus = EvaluationStatus.CORRECT,
                    feedbackMessage = "¡Excelente! ${state.operand1} ${state.operator.symbol} ${state.operand2} = $expected."
                )
            } else {
                state.copy(
                    evaluationStatus = EvaluationStatus.INCORRECT,
                    feedbackMessage = "¡Ups! $userAns no es correcto. Puedes borrar y volver a intentarlo."
                )
            }
        }
    }

    // =========================================================================
    // MODULE B: 4th Grade - Fractions & Dynamic Charts
    // =========================================================================

    fun setFractionMode(mode: ExerciseMode) {
        if (mode == ExerciseMode.SYSTEM_RANDOM) {
            generateSystemFractionExercise()
        } else {
            _fractionState.value = FractionUiState(
                mode = ExerciseMode.CREATOR_MANUAL,
                num1Input = "1",
                den1Input = "2",
                num2Input = "1",
                den2Input = "4",
                operator = FractionOperator.ADD,
                resultNumInput = "",
                resultDenInput = "",
                activeField = FractionField.NUM_1,
                creatorLocked = false,
                chartType = _fractionState.value.chartType,
                evaluationStatus = EvaluationStatus.IDLE,
                feedbackMessage = "Modo Creador: Edita los numeradores y denominadores o elige la operación."
            )
        }
    }

    fun clearFractionCreator() {
        _fractionState.update { state ->
            state.copy(
                mode = ExerciseMode.CREATOR_MANUAL,
                num1Input = "",
                den1Input = "",
                num2Input = "",
                den2Input = "",
                resultNumInput = "",
                resultDenInput = "",
                activeField = FractionField.NUM_1,
                creatorLocked = false,
                evaluationStatus = EvaluationStatus.IDLE,
                feedbackMessage = "Escribe los numeradores y denominadores para ver las gráficas en tiempo real."
            )
        }
    }

    fun generateSystemFractionExercise(preferredOperator: FractionOperator? = null) {
        val op = preferredOperator ?: FractionOperator.entries.random()
        val denominators = listOf(2, 3, 4, 5, 6, 8)
        var d1 = denominators.random()
        var d2 = denominators.random()
        var n1 = Random.nextInt(1, d1 + 2) // includes proper & occasional improper fractions
        var n2 = Random.nextInt(1, d2 + 1)

        if (op == FractionOperator.SUBTRACT) {
            // Ensure first fraction >= second fraction so result is non-negative
            if (n1 * d2 < n2 * d1) {
                val tmpN = n1; val tmpD = d1
                n1 = n2; d1 = d2
                n2 = tmpN; d2 = tmpD
            }
            if (n1 * d2 == n2 * d1 && n1 > 1) {
                n1 += 1
            }
        }

        _fractionState.value = FractionUiState(
            mode = ExerciseMode.SYSTEM_RANDOM,
            num1Input = n1.toString(),
            den1Input = d1.toString(),
            num2Input = n2.toString(),
            den2Input = d2.toString(),
            operator = op,
            resultNumInput = "",
            resultDenInput = "",
            activeField = FractionField.RESULT_NUM,
            creatorLocked = true,
            chartType = _fractionState.value.chartType,
            evaluationStatus = EvaluationStatus.IDLE,
            feedbackMessage = "Observa las gráficas, escribe el numerador y usa 'Num ⇅ Den' para el denominador."
        )
    }

    fun setFractionProblemForTest(
        n1: Int,
        d1: Int,
        n2: Int,
        d2: Int,
        operator: FractionOperator,
        mode: ExerciseMode = ExerciseMode.SYSTEM_RANDOM
    ) {
        _fractionState.value = FractionUiState(
            mode = mode,
            num1Input = n1.toString(),
            den1Input = d1.toString(),
            num2Input = n2.toString(),
            den2Input = d2.toString(),
            operator = operator,
            resultNumInput = "",
            resultDenInput = "",
            activeField = FractionField.RESULT_NUM,
            creatorLocked = mode == ExerciseMode.SYSTEM_RANDOM,
            evaluationStatus = EvaluationStatus.IDLE
        )
    }

    fun selectFractionOperator(operator: FractionOperator) {
        _fractionState.update { state ->
            if (state.mode == ExerciseMode.SYSTEM_RANDOM) {
                generateSystemFractionExercise(operator)
                return@update _fractionState.value
            } else {
                val next = state.copy(
                    operator = operator,
                    resultNumInput = "",
                    resultDenInput = "",
                    evaluationStatus = EvaluationStatus.IDLE
                )
                val msg = if (next.violatesSubtractionRule) {
                    "En la resta, la primera fracción debe ser mayor o igual a la segunda."
                } else {
                    "Operación cambiada a ${operator.label} (${operator.symbol})."
                }
                next.copy(feedbackMessage = msg)
            }
        }
    }

    fun selectFractionChartType(chartType: FractionChartType) {
        _fractionState.update { it.copy(chartType = chartType) }
    }

    fun selectFractionField(field: FractionField) {
        _fractionState.update { state ->
            val isResultField = field == FractionField.RESULT_NUM || field == FractionField.RESULT_DEN
            if (!isResultField && state.mode == ExerciseMode.SYSTEM_RANDOM) {
                return@update state
            }
            if (isResultField && !state.isResultEnabled) {
                return@update state.copy(
                    feedbackMessage = if (state.violatesSubtractionRule) {
                        "La primera fracción debe ser mayor o igual a la segunda para restar."
                    } else {
                        "Completa primero ambas fracciones (con denominadores entre 1 y 20)."
                    }
                )
            }
            state.copy(
                activeField = field,
                creatorLocked = isResultField
            )
        }
    }

    fun lockFractionCreatorAndFocusResult() {
        _fractionState.update { state ->
            if (state.areOperandsValid) {
                state.copy(
                    activeField = FractionField.RESULT_NUM,
                    creatorLocked = true,
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "¡Ahora ingresa el numerador y denominador del resultado!"
                )
            } else if (state.violatesSubtractionRule) {
                state.copy(
                    feedbackMessage = "En la resta, la primera fracción debe ser mayor o igual a la segunda."
                )
            } else {
                state.copy(
                    feedbackMessage = "Asegúrate de que ambas fracciones tengan números mayores a 0 (denominador máx. 20)."
                )
            }
        }
    }

    /**
     * Requirement 5: Quick navigation toggle on the custom Liquid Glass keyboard
     * to switch rapidly between Numerator and Denominator in the Fractions module.
     */
    fun toggleNumeratorDenominator() {
        _fractionState.update { state ->
            state.copy(
                activeField = state.activeField.toggleNumeratorDenominator()
            )
        }
    }

    fun stepFractionField(forward: Boolean) {
        _fractionState.update { state ->
            val fields = if (state.mode == ExerciseMode.SYSTEM_RANDOM) {
                listOf(FractionField.RESULT_NUM, FractionField.RESULT_DEN)
            } else if (state.isResultEnabled) {
                listOf(
                    FractionField.NUM_1,
                    FractionField.DEN_1,
                    FractionField.NUM_2,
                    FractionField.DEN_2,
                    FractionField.RESULT_NUM,
                    FractionField.RESULT_DEN
                )
            } else {
                listOf(
                    FractionField.NUM_1,
                    FractionField.DEN_1,
                    FractionField.NUM_2,
                    FractionField.DEN_2
                )
            }
            val currentIndex = fields.indexOf(state.activeField).coerceAtLeast(0)
            val nextIndex = if (forward) {
                (currentIndex + 1) % fields.size
            } else {
                (currentIndex - 1 + fields.size) % fields.size
            }
            val nextField = fields[nextIndex]
            state.copy(
                activeField = nextField,
                creatorLocked = nextField == FractionField.RESULT_NUM || nextField == FractionField.RESULT_DEN
            )
        }
    }

    fun onFractionDigitPressed(digit: Int) {
        _fractionState.update { state ->
            when (state.activeField) {
                FractionField.NUM_1 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    val candidate = (state.num1Input + digit.toString()).trimStart('0')
                    if (candidate.isEmpty() || candidate.length > 2) return@update state
                    state.copy(num1Input = candidate, evaluationStatus = EvaluationStatus.IDLE)
                }
                FractionField.DEN_1 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    val candidate = (state.den1Input + digit.toString()).trimStart('0')
                    val value = candidate.toIntOrNull() ?: return@update state
                    if (value !in 1..20) return@update state.copy(
                        feedbackMessage = "Usa denominadores del 1 al 20 para visualizar mejor la gráfica."
                    )
                    state.copy(den1Input = candidate, evaluationStatus = EvaluationStatus.IDLE)
                }
                FractionField.NUM_2 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    val candidate = (state.num2Input + digit.toString()).trimStart('0')
                    if (candidate.isEmpty() || candidate.length > 2) return@update state
                    state.copy(num2Input = candidate, evaluationStatus = EvaluationStatus.IDLE)
                }
                FractionField.DEN_2 -> {
                    if (state.mode != ExerciseMode.CREATOR_MANUAL) return@update state
                    val candidate = (state.den2Input + digit.toString()).trimStart('0')
                    val value = candidate.toIntOrNull() ?: return@update state
                    if (value !in 1..20) return@update state.copy(
                        feedbackMessage = "Usa denominadores del 1 al 20 para visualizar mejor la gráfica."
                    )
                    state.copy(den2Input = candidate, evaluationStatus = EvaluationStatus.IDLE)
                }
                FractionField.RESULT_NUM -> {
                    if (!state.isResultEnabled) return@update state
                    val candidate = if (state.resultNumInput == "0") digit.toString() else state.resultNumInput + digit.toString()
                    if (candidate.length > 3) return@update state
                    state.copy(
                        resultNumInput = candidate,
                        evaluationStatus = EvaluationStatus.IDLE,
                        feedbackMessage = "Usa 'Num ⇅ Den' en el teclado para pasar al denominador."
                    )
                }
                FractionField.RESULT_DEN -> {
                    if (!state.isResultEnabled) return@update state
                    val candidate = (state.resultDenInput + digit.toString()).trimStart('0')
                    if (candidate.isEmpty() || candidate.length > 3) return@update state
                    state.copy(
                        resultDenInput = candidate,
                        evaluationStatus = EvaluationStatus.IDLE,
                        feedbackMessage = "Presiona 'Verificar Respuesta' para evaluar tu fracción."
                    )
                }
            }
        }
    }

    fun onFractionBackspace() {
        _fractionState.update { state ->
            when (state.activeField) {
                FractionField.NUM_1 -> state.copy(
                    num1Input = state.num1Input.dropLast(1),
                    evaluationStatus = EvaluationStatus.IDLE
                )
                FractionField.DEN_1 -> state.copy(
                    den1Input = state.den1Input.dropLast(1),
                    evaluationStatus = EvaluationStatus.IDLE
                )
                FractionField.NUM_2 -> state.copy(
                    num2Input = state.num2Input.dropLast(1),
                    evaluationStatus = EvaluationStatus.IDLE
                )
                FractionField.DEN_2 -> state.copy(
                    den2Input = state.den2Input.dropLast(1),
                    evaluationStatus = EvaluationStatus.IDLE
                )
                FractionField.RESULT_NUM -> state.copy(
                    resultNumInput = state.resultNumInput.dropLast(1),
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "Corrige el numerador y vuelve a intentar."
                )
                FractionField.RESULT_DEN -> state.copy(
                    resultDenInput = state.resultDenInput.dropLast(1),
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "Corrige el denominador y vuelve a intentar."
                )
            }
        }
    }

    fun clearFractionResult() {
        _fractionState.update { state ->
            state.copy(
                resultNumInput = "",
                resultDenInput = "",
                activeField = FractionField.RESULT_NUM,
                evaluationStatus = EvaluationStatus.IDLE,
                feedbackMessage = "Resultado borrado. ¡Inténtalo de nuevo!"
            )
        }
    }

    fun verifyFractionAnswer() {
        _fractionState.update { state ->
            val expected = state.expectedReducedResult
                ?: return@update state.copy(
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "Completa primero las dos fracciones de la operación."
                )
            val userNum = state.resultNum
            val userDen = state.resultDen
            if (userNum == null || userDen == null || userDen <= 0) {
                return@update state.copy(
                    activeField = if (userNum == null) FractionField.RESULT_NUM else FractionField.RESULT_DEN,
                    evaluationStatus = EvaluationStatus.IDLE,
                    feedbackMessage = "Completa tanto el numerador como el denominador del resultado."
                )
            }

            val isEquivalent = MathLogic.areFractionsEquivalent(
                userNum = userNum,
                userDen = userDen,
                expectedNum = expected.first,
                expectedDen = expected.second
            )

            if (isEquivalent) {
                _starsEarned.update { it + 1 }
                val simplifiedNote = if (userNum != expected.first || userDen != expected.second) {
                    " (Equivalente a la fracción simplificada ${expected.first}/${expected.second})"
                } else {
                    " ¡En su forma más simple!"
                }
                state.copy(
                    evaluationStatus = EvaluationStatus.CORRECT,
                    feedbackMessage = "¡Excelente! $userNum/$userDen es correcto.$simplifiedNote"
                )
            } else {
                state.copy(
                    evaluationStatus = EvaluationStatus.INCORRECT,
                    feedbackMessage = "¡Sigue intentando! $userNum/$userDen no equivale al resultado. Borra y corrige."
                )
            }
        }
    }
}
