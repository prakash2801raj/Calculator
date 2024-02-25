package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // Declare binding variable

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use binding.root instead of R.layout.activity_main
    }

    fun numberAction(view: View){
        if(view is Button){
            if(view.text == ".") {
                if (canAddDecimal) {
                    binding.workingsTV.append(view.text) // Use binding.workingsTV
                }
                canAddDecimal=false
            }else{
                binding.workingsTV.append(view.text) // Use binding.workingsTV
            }
            canAddOperation = true
        }
    }

    fun operatorAction(view: View){
        if(view is Button && canAddOperation){
            binding.workingsTV.append(view.text) // Use binding.workingsTV
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        binding.workingsTV.text = "" // Use binding.workingsTV
        binding.resultTV.text = "" // Use binding.resultsTV
    }

    fun backSpaceAction(view: View) {
        val length = binding.workingsTV.length() // Use binding.workingsTV
        if (length > 0)
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1) // Use binding.workingsTV
    }

    fun equalsAction(view: View) {
        binding.resultTV.text = calculateResults() // Use binding.resultsTV
    }

    private fun calculateResults(): String {
        val digitOperators = digitOperators()
        if(digitOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitOperators)

        if(timesDivision.isEmpty()) return ""

        val result = addSubstractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubstractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in 1 until passedList.size step 2){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i] as Char
                val nextDigit = passedList[i+1] as Float
                when(operator){
                    '+' -> result += nextDigit
                    '-' -> result -= nextDigit
                }
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x')||list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i< restartIndex){
                val operator = passedList[i] as Char
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator){
                    'x'->{
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/'->{
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    '%'-> {
                        newList.add(prevDigit % nextDigit)
                        restartIndex = i + 1
                    }
                }
            }

            if(i>restartIndex){
                newList.add(passedList[i])
            }
        }
        return newList
    }

    private fun digitOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingsTV.text){
            if(character.isDigit()|| character == '.'){
                currentDigit += character
            }else{
                list.add(currentDigit.toFloat())
                currentDigit=""
                list.add(character)
            }
        }
        if(currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return  list
    }
}
