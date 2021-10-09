package com.example.calculator

import java.lang.Exception
import java.util.ArrayDeque

class ReversePolishNotation
{
    public fun calculateExpression(expression: String): Double {
        var input = expression


        while (input.contains("(-")) {
            var i = input.lastIndexOf("(-") + 1
            input = input.substring(0, i) + "0" + input.substring(i)
        }

        var bracket = 0
        for(i in input.indices){
            if(input[i] == '(') bracket++
            if(input[i] == ')') bracket--
        }
        if(bracket < 0)
            while (bracket != 0) {
                input = input.substring(0, input.length - 1)
                bracket--
            }
        else
            while (bracket != 0) {
                input += ")"
                bracket--
            }

        var output = getExpression(input)
        return countExpression(output)
    }

    private fun getExpression(input : String) :String
    {
        var output = ""
        var operationStack = ArrayDeque<Char>()

        var i = 0
        while (i < input.length)
        {
            if (isDelimiter(input[i])) {
                i++
                continue
            }

            if(input[i].isDigit())
            {

                while (!isDelimiter(input[i]) && !isOperator(input[i]))
                {
                    output += input[i]
                    i++

                    if (i == input.length) break
                }

                output += " "
                i--
            }

            if (isOperator(input[i]))
            {
                if (input[i] == '(')
                    operationStack.push(input[i])
                else if (input[i] == ')')
                {
                    var s = operationStack.pop()

                    while (s != '(')
                    {
                        output += s.toString() + ' '
                        s = operationStack.pop()
                    }
                }
                else
                {
                    if (operationStack.size > 0)
                        if (getPriority(input[i]) <= getPriority(operationStack.peek()))
                            output += operationStack.pop().toString() + " "

                    operationStack.push(input[i])

                }
            }
            i++
        }

        while (operationStack.size > 0)
            output += operationStack.pop() + " "

        return output
    }

    private fun countExpression(input : String) : Double
    {
        var result : Double = 0.0
        var temp = ArrayDeque<Double>()

        var i = 0
        while (i < input.length)
        {
            if (input[i].isDigit())
            {
                var a = ""

                while (!isDelimiter(input[i]) && !isOperator(input[i]))
                {
                    a += input[i]
                    i++
                    if (i == input.length) break
                }
                temp.push(a.toDouble())
                i--
            }
            else if (isOperator(input[i]))
            {
                var a = temp.pop()
                var b = temp.pop()

                when (input[i])
                {
                    '+' -> result = b + a
                    '-' -> result = b - a
                    '*' -> result = b * a
                    '/' -> result = b / a
                    '^' -> result = Math.pow(b, a)
                }
                temp.push(result)
            }
            i++
        }
        return temp.peek()
    }

    private fun  isDelimiter(c : Char) : Boolean
    {
        if (c in " =")
            return true
        return false
    }

    private fun getPriority(s : Char) : Byte
    {
        return when(s) {
            '(' -> 0
            ')' -> 1
            '+' -> 2
            '-' -> 3
            '*' -> 4
            '/' -> 4
            '^' -> 5
            else -> 6
        }
    }

    private fun isOperator(c : Char) : Boolean
    {
        if (c in "+-/*^()")
            return true
        return false
    }
}
