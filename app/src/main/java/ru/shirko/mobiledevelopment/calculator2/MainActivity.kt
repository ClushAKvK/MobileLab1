package ru.shirko.mobiledevelopment.calculator2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var leftNumber: Double? = null
    private var rightNumber: Double? = null
    private var tempOperand: Operands? = null
    private var mainResult: Double? = null

    private lateinit var edLeftNumber: EditText
    private lateinit var edRightNumber: EditText
    private lateinit var edOperand: EditText
    private lateinit var edResult: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edLeftNumber = findViewById(R.id.edLeftNumber)
        edRightNumber = findViewById(R.id.edRightNumber)
        edOperand = findViewById(R.id.edOperand)
        edResult = findViewById(R.id.edResult)

        val btnDigits: List<View> = listOf(
            findViewById(R.id.btnDigit_0),
            findViewById(R.id.btnDigit_1),
            findViewById(R.id.btnDigit_2),
            findViewById(R.id.btnDigit_3),
            findViewById(R.id.btnDigit_4),
            findViewById(R.id.btnDigit_5),
            findViewById(R.id.btnDigit_6),
            findViewById(R.id.btnDigit_7),
            findViewById(R.id.btnDigit_8),
            findViewById(R.id.btnDigit_9),
        )

        var digit = 0.0
        val digits: MutableMap<Int, Double> = mutableMapOf()

        for (btn in btnDigits) {
            digits[btn.id] = digit
            digit += 1.0

            btn.setOnClickListener {
                digits.get(btn.id)?.let { it1 -> inputNumber(it1) }
            }

        }

        // Operands
        findViewById<Button>(R.id.btnPlus).setOnClickListener{
            inputOperand(Operands.PLUS)
        }
        findViewById<Button>(R.id.btnMinus).setOnClickListener{
            inputOperand(Operands.MINUS)
        }
        findViewById<Button>(R.id.btnMult).setOnClickListener{
            inputOperand(Operands.MULTIPLICATION)
        }
        findViewById<Button>(R.id.btnDivide).setOnClickListener{
            inputOperand(Operands.DIVIDE)
        }
//        findViewById<Button>(R.id.btnDigit_0).setOnClickListener{
//            inputOperand(Operands.MOD)
//        }

        // Clear and Calculate
        findViewById<Button>(R.id.btnClear).setOnClickListener{
            clearExpression()
        }
        findViewById<Button>(R.id.btnCalculate).setOnClickListener{
            try {
                val result = calculateExpression()
                edResult.setText(if (result%1 == 0.0) result.toInt().toString() else result.toString())
                clearExpression(result=false)
            }
            catch (e: IllegalArgumentException) { }
        }

    }

    private fun inputNumber(digit: Double) {
        if (tempOperand == null) {
            edLeftNumber.error = null
            leftNumber = if (leftNumber == null) digit else leftNumber!! * 10 + digit
            edLeftNumber.setText("${if (leftNumber!! %1 == 0.0) leftNumber!!.toInt() else leftNumber}")
        }
        else {
            edRightNumber.error = null
            rightNumber = if (rightNumber == null) digit else rightNumber!! * 10 + digit
            edRightNumber.setText("${rightNumber!!.toInt()}")
        }
    }

    private fun inputOperand(operand: Operands) {
        if (leftNumber == null && mainResult == null) {
            edOperand.error = "Необходимо ввести первое число"
            return
        }


        edOperand.error = null
        if (rightNumber == null) {

            if (leftNumber == null && mainResult != null) {
                leftNumber = mainResult
                edLeftNumber.error = null
                edLeftNumber.setText("${if (leftNumber!! % 1 == 0.0) leftNumber!!.toInt() else leftNumber}")
                mainResult = null
                edResult.setText("")
            }

            tempOperand = operand
            edOperand.setText( operand.visual)
            return
        }

        tempOperand = operand
        edOperand.setText( operand.visual)

//        leftNumber = calculateExpression()
//        edLeftNumber.setText("${if (leftNumber!! % 1 == 0.0) leftNumber!!.toInt() else leftNumber}")
//
//        tempOperand = operand
//        edOperand.setText(operand.visual)

        clearExpression(left=false, operand=false, right=false)
    }

    private fun calculateExpression():Double {
        var answer: Double = 0.0
        var flag = true
        if (leftNumber == null) {
            edLeftNumber.error = "Необходимо ввести число"
            flag = false
        }
        if (rightNumber == null) {
            edRightNumber.error = "Необходимо ввести число"
            flag = false
        }
        if (tempOperand == null) {
            edOperand.error = "Необходимо ввести операнд"
            flag = false
        }
        if (!flag)
            throw IllegalArgumentException("Null pointer excepted")

        when (tempOperand) {
            Operands.PLUS -> answer = leftNumber!! + rightNumber!!
            Operands.MINUS -> answer = leftNumber!! - rightNumber!!
            Operands.MULTIPLICATION -> answer = leftNumber!! * rightNumber!!
            Operands.DIVIDE -> answer = leftNumber!! / rightNumber!!
            Operands.MOD -> answer = leftNumber!! % rightNumber!!
            else -> {}
        }
        mainResult = answer
        return answer
    }

    private fun clearExpression(left: Boolean = true, operand: Boolean = true, right: Boolean = true,
                                result: Boolean = true) {

        if (left) {
            leftNumber = null
            edLeftNumber.error = null
            edLeftNumber.setText("")
        }
        if (right) {
            rightNumber = null
            edRightNumber.error = null
            edRightNumber.setText("")
        }
        if (operand) {
            tempOperand = null
            edOperand.error = null
            edOperand.setText("")
        }
        if (result) {
            mainResult = null
            edResult.setText("")
        }
    }

}