package com.example.calculator

import android.widget.TextView

class HandlerClass(_inputTextView : TextView, _resultTextView : TextView) {
    var expression = ""
    var result = ""
    var realTimeCalculatingFlag = true
    var bracketCount : Array<Int> = arrayOf(0, 0)
    var bracketFlag = false
    val reversePolishNotation = ReversePolishNotation()
    var inputTextView : TextView = _inputTextView
    var resultTextView : TextView = _resultTextView

    fun processClick(id : Int){
        /*  clear the area  */
        if(expression.isEmpty()){
            inputTextView.text = ""
            resultTextView.text = ""
        }
        when (id) {
            R.id.buttonClearAll -> deleteSymbol(1)
            R.id.buttonClearOne -> deleteSymbol(0)
            R.id.buttonRes -> {
                makeResult()
                if(realTimeCalculatingFlag && parseExpression()){
                    inputTextView.text = result
                    resultTextView.text = ""
                    expression = result
                    result = ""
                    return
                }
            }
            else -> {
                appendSymbol(id)
            }
        }
        if(realTimeCalculatingFlag){
            if(expression.isNotEmpty() && expression[expression.length - 1] !in "+-*/(")
                makeResult()
            else
                resultTextView.text = ""
        }
    }

    private fun deleteSymbol(flag: Int) {
        if(expression.isEmpty())
            return
        if(flag == 0) {
            if(expression[expression.length - 1] == '(')
                bracketCount[0]--
            else if(expression[expression.length - 1] == ')')
                bracketCount[1]--
            expression = expression.substring(startIndex = 0, endIndex = expression.length - 1)
            inputTextView.text = expression
        }
        else if(flag == 1){
            bracketCount[0] = 0
            bracketCount[1] = 0
            bracketFlag = false
            expression = ""
            result = ""
            inputTextView.text = expression
            resultTextView.text = result
        }
    }

    private fun makeResult() {
        if(!parseExpression())
            return
        var res = reversePolishNotation.calculateExpression(expression)
        if(isWhole(res))
            result = res.toInt().toString()
        else
            result = res.toString()
        resultTextView.text = result
    }

    private fun isWhole(value: Double):Boolean { return value - value.toInt() == 0.0 }

    private fun appendSymbol(id : Int) {
        var str = getSymbol(id)
        if(bracketFlag && str.isNotEmpty() && str[str.length - 1] != '(')
            bracketFlag = false

        expression += str
        inputTextView.append(str)
    }

    private fun parseExpression() : Boolean {
        return when{
            expression.isEmpty() -> false
            expression[expression.length - 1] in "+-*/(" -> false
            expression.contains("/0") -> {
                var i = expression.indexOf("/0")
                var subs = expression.substring(startIndex = i + 1)
                i = subs.lastIndexOfAny("+-*/".toCharArray())
                if(i == -1)
                    i = subs.length
                subs = subs.substring(startIndex = 0, endIndex = i)
                subs.toDouble() != 0.0
            }
            else -> true
        }
    }

    private fun getSymbol(id : Int) : String {
        return when(id){
            R.id.button0 -> nullBalance()
            R.id.button1 -> numberBalance("1")
            R.id.button2 -> numberBalance("2")
            R.id.button3 -> numberBalance("3")
            R.id.button4 -> numberBalance("4")
            R.id.button5 -> numberBalance("5")
            R.id.button6 -> numberBalance("6")
            R.id.button7 -> numberBalance("7")
            R.id.button8 -> numberBalance("8")
            R.id.button9 -> numberBalance("9")
            R.id.buttonMul -> operatorBalance("*")
            R.id.buttonDiv -> operatorBalance("/")
            R.id.buttonSub -> operatorBalance("-")
            R.id.buttonSum -> operatorBalance("+")
            R.id.buttonPoint -> pointBalance()
            R.id.buttonSign -> signBalance()
            R.id.buttonBracket -> bracketBalance()
            else -> ""
        }
    }

    private fun numberBalance(number : String) : String {
        return when{
            expression.isEmpty() -> number
            expression[expression.length - 1] == ')' -> "*$number"
            expression[expression.length - 1] == '0' -> {
                var i = expression.lastIndexOfAny("+-*/(".toCharArray())
                if(i == -1)
                    i = 0
                if(expression.substring(startIndex = i).contains('.'))
                    number
                else {
                    expression = expression.substring(startIndex = 0, endIndex = expression.length - 1)
                    number
                }
            }
            else -> number
        }
    }

    private fun nullBalance() : String {
        return when{
            expression.isEmpty() -> "0"
            expression[expression.length - 1] == ')' -> "*0"
            expression[expression.length - 1] == '0' -> {
                var i = expression.lastIndexOfAny("+-*/(".toCharArray())
                if(i == -1)
                    i = 0
                if(expression.substring(startIndex = i).contains('.') || expression[i] != '0')
                    "0"
                else
                    ""
            }
            else -> "0"
        }
    }

    private fun bracketBalance() : String {
        if(expression.length == 0){
            bracketCount[0]++
            bracketFlag = true
            return "("
        }
        if(expression[expression.length - 1] in "+-*/(") {
            bracketCount[0]++
            bracketFlag = true
            return "("
        }
        else if((bracketCount[0] - bracketCount[1] == 0) && expression[expression.length - 1] !in "+-*/(") {
            bracketCount[0]++
            bracketFlag = true
            return "*("
        }
        else if((bracketCount[0] - bracketCount[1] > 0) && !bracketFlag) {
            bracketCount[1]++
            bracketFlag = false
            return ")"
        }
        else {
            bracketCount[0]++
            bracketFlag = true
            return "("
        }
    }

    private fun pointBalance() : String {
        when {
            expression.isEmpty() -> return "0."
            expression[expression.length - 1] == '.' -> return ""
            expression[expression.length - 1] in "+-*/(" -> return "0."
            expression[expression.length - 1] == ')' -> return "*0."
            else -> {
                var i = expression.lastIndexOfAny("+-*/".toCharArray())
                if(i == -1)
                    i = 0
                var number = expression.substring(startIndex = i)
                if(number.contains('.'))
                    return ""
                else
                    return "."
            }
        }
    }

    private fun signBalance() : String {
        bracketCount[0]++
        bracketFlag = true
        return when{
            expression.isEmpty() -> "(-"
            expression[expression.length - 1] in "+-*/(" -> "(-"
            else -> "*(-"
        }
    }

    private fun operatorBalance(operator : String) : String {
        return when{
            expression.isEmpty() -> ""
            expression[expression.length - 1] in "+-*/.(" -> ""
            else -> operator
        }
    }    
}