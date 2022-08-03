package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var tvInput : TextView? = null

    private var isLastEntryDot : Boolean = false
    private var isLastEntryNumeric : Boolean = false

    private var hasDot : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
    }

    fun onDigit(view: View){
        tvInput?.append((view as Button).text)
        isLastEntryNumeric = true
        isLastEntryDot = false
    }

    fun onOperator(view: View){
        tvInput?.text?.let {

            if(isLastEntryNumeric && !isOperatorAdded(it.toString())){
                tvInput?.append((view as Button).text)
                isLastEntryDot = false
                isLastEntryNumeric = false
                hasDot = false
            }
        }
    }

    fun onClear(view: View){
        tvInput?.text = ""
        isLastEntryDot = false
        isLastEntryNumeric = false
        hasDot = false
    }

    fun onDecimalPoint(view: View){
        if(isLastEntryNumeric && !isLastEntryDot && !hasDot){
            tvInput?.append((view as Button).text)
            isLastEntryNumeric = false
            isLastEntryDot = true
        }

        if(tvInput?.text.toString().contains(".")){
            hasDot = true
        }

    }

    fun onEquals(view: View){
        if(isLastEntryNumeric){
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try {
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }
                if(tvValue.contains("-")) {
                    var splitValue = splitOperation(tvValue.split("-"), prefix)
                    tvInput?.text = removeZeroAfterDot(
                                    (splitValue[0].toDouble() - splitValue[1].toDouble()).toString())
                }
                else if(tvValue.contains("+")) {
                    var splitValue = splitOperation(tvValue.split("+"), prefix)
                    tvInput?.text = removeZeroAfterDot(
                                    (splitValue[0].toDouble() + splitValue[1].toDouble()).toString())
                }
                else if(tvValue.contains("/")) {
                    var splitValue = splitOperation(tvValue.split("/"),prefix)
                    tvInput?.text = removeZeroAfterDot(
                                    (splitValue[0].toDouble() / splitValue[1].toDouble()).toString())
                }
                else if(tvValue.contains("*")) {
                    var splitValue = splitOperation(tvValue.split("*"), prefix)
                    tvInput?.text = removeZeroAfterDot(
                                    (splitValue[0].toDouble() * splitValue[1].toDouble()).toString())
                }

            }catch (e: java.lang.ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    private fun splitOperation(splitValue: List<String>, prefix: String) : List<String>{
        var one = splitValue[0]
        var two = splitValue[1]

        if(prefix.isNotEmpty())
        {
            one = prefix + one
        }

        return listOf(one, two)
    }

    private fun removeZeroAfterDot(result:String) : String{
        var value = result
        if(result.endsWith(".0")){
            value = result.substring(0, result.length-2)
        }

        return value
    }

    private fun isOperatorAdded(value : String) : Boolean {
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("+")
                    ||value.contains("*")
                    ||value.contains("/")
                    ||value.contains("-")
        }
    }
}