package com.soumya.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.soumya.calculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun numberAction(view: View)
    {
        if (view is Button)
        {
            if (view.text==".")
            {
                if(canAddDecimal)
                {

                    binding.workingsTV.append(view.text)
                }
                canAddDecimal = false
            }
            else
            {
                binding.workingsTV.append(view.text)
            }

            canAddOperation = true
        }
    }
    fun operationAction(view: View)
    {
        if (view is Button&& canAddOperation)
        {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
        // Add a new case for the modulo operator
        if (view is Button && view.text == "%") {
            binding.workingsTV.append("%")
        }
    }


    fun allClearAction(view: View)
    {
        binding.workingsTV.text=""
        binding.resultsTV.text=""

    }

    fun backspaceAction(view: View)
    {
        val length = binding.workingsTV.length()
        if(length > 0) {
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0,length-1)
        }
    }


    fun equalsAction(view: View)
    {
        binding.resultsTV.text = calculatedResults()
    }

    private fun calculatedResults(): String {

        val digitsOperator = digitsOperators()
        if (digitsOperator.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperator)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {

        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i!= passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if(operator == '+')
                {
                    result += nextDigit
                }
                if(operator == '-')
                {
                    result -= nextDigit
                }
            }
        }


        return result

    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('x')|| list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passesList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()

        var restartIndex = passesList.size

        for(i in passesList.indices)
        {
            if(passesList[i] is Char && i!= passesList.lastIndex && i<restartIndex)
            {
                val operator = passesList[i]
                val prevDigit = passesList[i-1] as Float
                val nextDigit = passesList[i+1] as Float

                when(operator)
                {
                    'x'->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i+1
                    }
                    '/'->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i+1
                    }
                    else->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)

                    }

                }
            }

            if(i>restartIndex)
            {
                newList.add(passesList[i])
            }
        }


        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingsTV.text)
        {
            if(character.isDigit()||character == '.')
            {
                currentDigit +=character
            }
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit!="")
        {
            list.add(currentDigit.toFloat())
        }
        return list
    }
}