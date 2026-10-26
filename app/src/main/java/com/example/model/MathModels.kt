package com.example.model

import kotlin.math.abs

enum class MathSection {
    GRADE_2_ADD_SUB,
    GRADE_4_FRACTIONS
}

enum class ExerciseMode {
    SYSTEM_RANDOM,
    CREATOR_MANUAL
}

sealed class AppScreen {
    data object Home : AppScreen()
    data class SubMenu(val section: MathSection) : AppScreen()
    data class AdditionSubtraction(val initialMode: ExerciseMode) : AppScreen()
    data class Fractions(val initialMode: ExerciseMode) : AppScreen()
}

enum class AddSubOperator(val symbol: String, val label: String) {
    ADD("+", "Suma"),
    SUBTRACT("-", "Resta")
}

enum class FractionOperator(val symbol: String, val label: String) {
    ADD("+", "Suma"),
    SUBTRACT("-", "Resta"),
    MULTIPLY("×", "Multiplicación"),
    DIVIDE("÷", "División")
}

enum class EvaluationStatus {
    IDLE,
    CORRECT,
    INCORRECT
}

enum class AddSubField {
    OPERAND_1,
    OPERAND_2,
    RESULT
}

enum class FractionField(val isNumerator: Boolean) {
    NUM_1(true),
    DEN_1(false),
    NUM_2(true),
    DEN_2(false),
    RESULT_NUM(true),
    RESULT_DEN(false);

    fun toggleNumeratorDenominator(): FractionField = when (this) {
        NUM_1 -> DEN_1
        DEN_1 -> NUM_1
        NUM_2 -> DEN_2
        DEN_2 -> NUM_2
        RESULT_NUM -> RESULT_DEN
        RESULT_DEN -> RESULT_NUM
    }
}

enum class FractionChartType {
    AUTO, // Pie chart for proper fractions (num <= den), Bar chart for improper fractions (num > den)
    PIE,  // Circular pie chart
    BAR   // Segmented rectangular bars
}

data class AddSubUiState(
    val mode: ExerciseMode = ExerciseMode.SYSTEM_RANDOM,
    val operand1Input: String = "",
    val operand2Input: String = "",
    val operator: AddSubOperator = AddSubOperator.ADD,
    val resultInput: String = "",
    val activeField: AddSubField = AddSubField.RESULT,
    val creatorLocked: Boolean = false,
    val evaluationStatus: EvaluationStatus = EvaluationStatus.IDLE,
    val feedbackMessage: String = ""
) {
    val operand1: Int? get() = operand1Input.toIntOrNull()
    val operand2: Int? get() = operand2Input.toIntOrNull()
    val resultValue: Int? get() = resultInput.toIntOrNull()

    val isOperand1InRange: Boolean get() = operand1 != null && operand1 in 1..99
    val isOperand2InRange: Boolean get() = operand2 != null && operand2 in 1..99

    val violatesSubtractionRule: Boolean
        get() = operator == AddSubOperator.SUBTRACT &&
            operand1 != null && operand2 != null && operand1!! < operand2!!

    val areOperandsValid: Boolean
        get() = isOperand1InRange && isOperand2InRange && !violatesSubtractionRule

    val isResultEnabled: Boolean
        get() = mode == ExerciseMode.SYSTEM_RANDOM || areOperandsValid

    val expectedAnswer: Int?
        get() {
            if (!areOperandsValid) return null
            val a = operand1 ?: return null
            val b = operand2 ?: return null
            return when (operator) {
                AddSubOperator.ADD -> a + b
                AddSubOperator.SUBTRACT -> a - b
            }
        }
}

data class FractionUiState(
    val mode: ExerciseMode = ExerciseMode.SYSTEM_RANDOM,
    val num1Input: String = "",
    val den1Input: String = "",
    val num2Input: String = "",
    val den2Input: String = "",
    val operator: FractionOperator = FractionOperator.ADD,
    val resultNumInput: String = "",
    val resultDenInput: String = "",
    val activeField: FractionField = FractionField.RESULT_NUM,
    val creatorLocked: Boolean = false,
    val chartType: FractionChartType = FractionChartType.AUTO,
    val evaluationStatus: EvaluationStatus = EvaluationStatus.IDLE,
    val feedbackMessage: String = ""
) {
    val num1: Int? get() = num1Input.toIntOrNull()
    val den1: Int? get() = den1Input.toIntOrNull()
    val num2: Int? get() = num2Input.toIntOrNull()
    val den2: Int? get() = den2Input.toIntOrNull()
    val resultNum: Int? get() = resultNumInput.toIntOrNull()
    val resultDen: Int? get() = resultDenInput.toIntOrNull()

    val isFraction1Valid: Boolean
        get() = num1 != null && den1 != null && num1!! >= 1 && den1!! in 1..20

    val isFraction2Valid: Boolean
        get() = num2 != null && den2 != null && num2!! >= 1 && den2!! in 1..20

    val violatesSubtractionRule: Boolean
        get() {
            if (operator != FractionOperator.SUBTRACT) return false
            val n1 = num1 ?: return false
            val d1 = den1 ?: return false
            val n2 = num2 ?: return false
            val d2 = den2 ?: return false
            if (d1 <= 0 || d2 <= 0) return false
            return n1.toLong() * d2.toLong() < n2.toLong() * d1.toLong()
        }

    val areOperandsValid: Boolean
        get() = isFraction1Valid && isFraction2Valid && !violatesSubtractionRule

    val isResultEnabled: Boolean
        get() = mode == ExerciseMode.SYSTEM_RANDOM || areOperandsValid

    val expectedReducedResult: Pair<Int, Int>?
        get() {
            if (!areOperandsValid) return null
            return MathLogic.computeFractionResult(
                num1!!, den1!!, num2!!, den2!!, operator
            )
        }
}

object MathLogic {
    fun gcd(a: Int, b: Int): Int {
        var x = abs(a)
        var y = abs(b)
        while (y != 0) {
            val t = y
            y = x % y
            x = t
        }
        return if (x == 0) 1 else x
    }

    fun simplifyFraction(num: Int, den: Int): Pair<Int, Int> {
        if (den == 0) return Pair(num, 1)
        if (num == 0) return Pair(0, 1)
        val divisor = gcd(num, den)
        val sign = if (den < 0) -1 else 1
        return Pair((num / divisor) * sign, abs(den / divisor))
    }

    fun computeFractionResult(
        n1: Int,
        d1: Int,
        n2: Int,
        d2: Int,
        operator: FractionOperator
    ): Pair<Int, Int> {
        val rawNum: Int
        val rawDen: Int
        when (operator) {
            FractionOperator.ADD -> {
                rawNum = n1 * d2 + n2 * d1
                rawDen = d1 * d2
            }
            FractionOperator.SUBTRACT -> {
                rawNum = n1 * d2 - n2 * d1
                rawDen = d1 * d2
            }
            FractionOperator.MULTIPLY -> {
                rawNum = n1 * n2
                rawDen = d1 * d2
            }
            FractionOperator.DIVIDE -> {
                rawNum = n1 * d2
                rawDen = d1 * n2
            }
        }
        return simplifyFraction(rawNum, rawDen)
    }

    /**
     * Checks whether the student's fraction (userNum / userDen) is mathematically
     * equivalent to the expected fraction (expectedNum / expectedDen).
     * Example: 2/4 is equivalent to 1/2, 6/4 is equivalent to 3/2.
     */
    fun areFractionsEquivalent(
        userNum: Int,
        userDen: Int,
        expectedNum: Int,
        expectedDen: Int
    ): Boolean {
        if (userDen <= 0 || expectedDen <= 0) return false
        return userNum.toLong() * expectedDen.toLong() == expectedNum.toLong() * userDen.toLong()
    }
}
