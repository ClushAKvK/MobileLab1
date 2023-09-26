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

    private lateinit var edLeftNumber: EditText
    private lateinit var edRightNumber: EditText
    private lateinit var tvOperand: TextView
    private lateinit var edResult: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edLeftNumber = findViewById(R.id.edLeftNumber)
        edRightNumber = findViewById(R.id.edRightNumber)
        tvOperand = findViewById(R.id.tvOperand)
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
            val result = calculateExpression()

            edResult.setText(if (result%1 == 0.0) result.toInt().toString() else result.toString())
            clearExpression(result=false)
        }

    }

    private fun inputNumber(digit: Double) {
        if (tempOperand == null) {
            leftNumber = if (leftNumber == null) digit else leftNumber!! * 10 + digit
            edLeftNumber.setText("${if (leftNumber!! %1 == 0.0) leftNumber!!.toInt() else leftNumber}")
        }
        else {
            rightNumber = if (rightNumber == null) digit else rightNumber!! * 10 + digit
            edRightNumber.setText("${rightNumber!!.toInt()}")
        }
    }

    private fun inputOperand(operand: Operands) {

        if (rightNumber == null) {
            tempOperand = operand
            tvOperand.text = operand.visual
            return
        }

        leftNumber = calculateExpression()
        edLeftNumber.setText("${if (leftNumber!! % 1 == 0.0) leftNumber!!.toInt() else leftNumber}")

        tempOperand = operand
        tvOperand.text = operand.visual

        clearExpression(left=false, operand=false)
    }

    private fun calculateExpression():Double {
        var answer: Double = 0.0
        if (leftNumber == null) leftNumber = 0.0
        if (rightNumber == null) rightNumber = 0.0
        when (tempOperand) {
            Operands.PLUS -> answer = leftNumber!! + rightNumber!!
            Operands.MINUS -> answer = leftNumber!! - rightNumber!!
            Operands.MULTIPLICATION -> answer = leftNumber!! * rightNumber!!
            Operands.DIVIDE -> answer = leftNumber!! / rightNumber!!
            Operands.MOD -> answer = leftNumber!! % rightNumber!!
            else -> {}
        }
        return answer
    }

    private fun clearExpression(left: Boolean = true, operand: Boolean = true, right: Boolean = true,
                                result: Boolean = true) {
        if (left) {
            leftNumber = null
            edLeftNumber.setText("")
        }
        if (right) {
            rightNumber = null
            edRightNumber.setText("")
        }
        if (operand) {
            tempOperand = null
            tvOperand.text = ""
        }
        if (result) {
            edResult.setText("")
        }
    }

}