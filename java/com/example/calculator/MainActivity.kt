package com.example.calculator

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val _inputTextKey = "inputTextView.text"
    private val _resultTextKey = "resultTextView.text"
    private val _expressionKey = "expression"
    private var inputText : String = "default"
    private var resultText : String = "default"
    lateinit var inputTextView  : TextView
    lateinit var resultTextView : TextView
    lateinit var handlerClass : HandlerClass

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        inputTextView = findViewById<TextView>(R.id.inputTextView)
        resultTextView = findViewById<TextView>(R.id.resultTextView)
        inputTextView.setMovementMethod(ScrollingMovementMethod())
        resultTextView.setMovementMethod(ScrollingMovementMethod())
        handlerClass = HandlerClass(_inputTextView = inputTextView, _resultTextView = resultTextView)

        super.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            if(intent.getStringExtra(_inputTextKey) == null || intent.getStringExtra(_resultTextKey) == null){
                inputText = resources.getString(R.string.input_text)
                resultText = resources.getString(R.string.result_text)
            }
            else{
                inputText = intent.getStringExtra(_inputTextKey).toString()
                resultText = intent.getStringExtra(_resultTextKey).toString()
                handlerClass.expression = intent.getStringExtra(_expressionKey).toString()
            }
        }
        else{
            this.inputText = savedInstanceState.getString(_inputTextKey).toString()
            this.resultText = savedInstanceState.getString(_resultTextKey).toString()
            this.handlerClass.expression = savedInstanceState.getString(_expressionKey).toString()
        }

        this.inputTextView.text = inputText
        this.resultTextView.text = resultText
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        this.inputText = this.inputTextView.text.toString()
        this.resultText = this.resultTextView.text.toString()
        outState.putString(_inputTextKey, this.inputText)
        outState.putString(_resultTextKey, this.resultText)
        outState.putString(_expressionKey, this.handlerClass.expression)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        this.inputText = savedInstanceState.getString(_inputTextKey).toString()
        this.resultText = savedInstanceState.getString(_resultTextKey).toString()
        this.handlerClass.expression = savedInstanceState.getString(_expressionKey).toString()
    }

    fun onClick(view : View)
    {
        var button : Button = findViewById<Button>(view.id)
        handlerClass.processClick(id = button.id)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val intent = Intent(this, LandscapeActivity::class.java)
            this.inputText = this.inputTextView.text.toString()
            this.resultText = this.resultTextView.text.toString()
            intent.putExtra(_inputTextKey, this.inputText)
            intent.putExtra(_resultTextKey, this.resultText)
            intent.putExtra(_expressionKey, this.handlerClass.expression)
            startActivity(intent)
        }
    }
}