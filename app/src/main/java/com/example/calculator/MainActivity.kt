package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  var canAddOperation = false
    private  var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun numberAction(view: View)
    {
        if (view is Button){
            if (view.text == ".")
            {
                if (canAddDecimal)
                    binding.workingsTV.append(view.text)

                canAddOperation=false
            }
            else
            binding.workingsTV.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View)
    {
        if (view is Button && canAddOperation){
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View?)
    {
        binding.workingsTV.text=""
        binding.resultsTV.text=""
    }

    fun backSpaceAction(view: View?)
    {
        val  length = binding.workingsTV.length()
        if (length>0)
            binding.workingsTV.text  = binding.workingsTV.text.subSequence(0,length - 1)
    }

    fun equalsAction(view: View?)
    {
        binding.resultsTV.text = calculateResults()
    }

    private fun  calculateResults(): String
    {
        val digitsOperators = digitsOperators ()
        if (digitsOperators.isEmpty()) return ""

        timesDivisionCalculate(digitsOperators)
    return ""
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>) {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calTimesDiv(list)
        }
    }

    private fun calTimesDiv(passedList: MutableList<Any>): MutableList<Any>  {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in binding.workingsTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}