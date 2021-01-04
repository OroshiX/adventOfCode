package advent

import java.util.*

fun solve18(scanner: Scanner): String {
    with(scanner) {
        val operations = parseIntoListOfMath(nextLine())
        val t = Add(
            Multiply(Value(2), Value(3)), Multiply(Value(4), Value(5))
        )
        print(t.value())
        return operations.toString()
    }
}

fun parseIntoListOfMath(line: String): List<InputMathElement> {
    var wasDigit = false
    var digitToParse = ""
    val operations = mutableListOf<InputMathElement>()
    loop@ for (c in line) {
        if (c.isDigit()) {
            digitToParse += c
            wasDigit = true
        } else {
            if (wasDigit) {
                operations.add(InputValue(digitToParse.toInt()))
                wasDigit = false
                digitToParse = ""
            }
            when (c) {
                ' ' -> continue@loop
                '(' -> operations.add(InputParenthesis(true))
                ')' -> operations.add(InputParenthesis(false))
                '+' -> operations.add(InputMathOperation(false))
                '*' -> operations.add(InputMathOperation(true))
            }
        }
    }
    if (wasDigit) {
        operations.add(InputValue(digitToParse.toInt()))
    }
    return operations
}

sealed class InputMathElement

class InputParenthesis(val start: Boolean) : InputMathElement()
class InputValue(val value: Int) : InputMathElement()
class InputMathOperation(val multiply: Boolean) : InputMathElement()

sealed class MathElement {
    abstract fun value(): Int
}

data class Value(val value: Int) : MathElement() {
    override fun value(): Int = this.value
    override fun toString(): String {
        return value.toString()
    }
}

data class Parenthesis(val inside: List<MathElement>)

sealed class BinaryOperation(val left: MathElement, val right: MathElement) : MathElement()

class Multiply(left: MathElement, right: MathElement) : BinaryOperation(left, right) {
    override fun value(): Int = left.value() * right.value()
    override fun toString(): String {
        return "($left) * ($right)"
    }
}

class Add(left: MathElement, right: MathElement) : BinaryOperation(left, right) {
    override fun value(): Int = left.value() + right.value()
    override fun toString(): String {
        return "($left) + ($right)"
    }
}
