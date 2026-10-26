package com.example

import com.example.model.AddSubField
import com.example.model.AddSubOperator
import com.example.model.EvaluationStatus
import com.example.model.ExerciseMode
import com.example.model.FractionField
import com.example.model.FractionOperator
import com.example.model.MathLogic
import com.example.viewmodel.MathViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `grade 2 system mode always generates numbers in 1 to 99 and non-negative subtraction`() {
        val vm = MathViewModel()
        repeat(60) {
            vm.generateSystemAddSubExercise()
            val state = vm.addSubState.value
            val op1 = state.operand1!!
            val op2 = state.operand2!!
            assertTrue("Operand 1 must be in 1..99", op1 in 1..99)
            assertTrue("Operand 2 must be in 1..99", op2 in 1..99)
            if (state.operator == AddSubOperator.SUBTRACT) {
                assertTrue("Minuendo ($op1) must be >= Sustraendo ($op2)", op1 >= op2)
            }
        }
    }

    @Test
    fun `grade 2 creator mode blocks result when minuendo is less than sustraendo`() {
        val vm = MathViewModel()
        vm.setAddSubMode(ExerciseMode.CREATOR_MANUAL)
        vm.selectAddSubOperator(AddSubOperator.SUBTRACT)

        // Enter 15 - 40
        vm.selectAddSubField(AddSubField.OPERAND_1)
        vm.onAddSubDigitPressed(1)
        vm.onAddSubDigitPressed(5)

        vm.selectAddSubField(AddSubField.OPERAND_2)
        vm.onAddSubDigitPressed(4)
        vm.onAddSubDigitPressed(0)

        val stateBeforeSwap = vm.addSubState.value
        assertTrue(stateBeforeSwap.violatesSubtractionRule)
        assertFalse(stateBeforeSwap.isResultEnabled)

        // Swap numbers -> 40 - 15
        vm.swapAddSubOperands()
        val stateAfterSwap = vm.addSubState.value
        assertFalse(stateAfterSwap.violatesSubtractionRule)
        assertTrue(stateAfterSwap.isResultEnabled)
        assertEquals(25, stateAfterSwap.expectedAnswer)
    }

    @Test
    fun `grade 4 fraction equivalence recognizes simplified and unsimplified answers`() {
        // 1/4 + 1/4 = 2/4 = 1/2
        val vm = MathViewModel()
        vm.setFractionProblemForTest(1, 4, 1, 4, FractionOperator.ADD)

        // Enter unsimplified 2/4
        vm.onFractionDigitPressed(2)
        vm.toggleNumeratorDenominator()
        assertEquals(FractionField.RESULT_DEN, vm.fractionState.value.activeField)
        vm.onFractionDigitPressed(4)
        vm.verifyFractionAnswer()
        assertEquals(EvaluationStatus.CORRECT, vm.fractionState.value.evaluationStatus)

        // Clear and enter simplified 1/2
        vm.clearFractionResult()
        vm.onFractionDigitPressed(1)
        vm.toggleNumeratorDenominator()
        vm.onFractionDigitPressed(2)
        vm.verifyFractionAnswer()
        assertEquals(EvaluationStatus.CORRECT, vm.fractionState.value.evaluationStatus)

        // Clear and enter wrong fraction 3/4
        vm.clearFractionResult()
        vm.onFractionDigitPressed(3)
        vm.toggleNumeratorDenominator()
        vm.onFractionDigitPressed(4)
        vm.verifyFractionAnswer()
        assertEquals(EvaluationStatus.INCORRECT, vm.fractionState.value.evaluationStatus)
    }

    @Test
    fun `math logic computes all four fraction operations accurately`() {
        assertEquals(Pair(5, 6), MathLogic.computeFractionResult(1, 2, 1, 3, FractionOperator.ADD))
        assertEquals(Pair(1, 6), MathLogic.computeFractionResult(1, 2, 1, 3, FractionOperator.SUBTRACT))
        assertEquals(Pair(1, 6), MathLogic.computeFractionResult(1, 2, 1, 3, FractionOperator.MULTIPLY))
        assertEquals(Pair(3, 2), MathLogic.computeFractionResult(1, 2, 1, 3, FractionOperator.DIVIDE))
    }
}
