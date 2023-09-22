package ru.shirko.mobiledevelopment.calculator2

class MathOperation {
    private var leftNumber: Int? = null
    private var rightNumber: Int? = null

    fun inputNumber(digit: Int) {
        if (rightNumber == null) {
            leftNumber = if (leftNumber == null) digit else leftNumber!! * 10 + digit

        }
        else {
            rightNumber = if (rightNumber == null) digit else rightNumber!! * 10 + digit
        }
    }

}