package com.example.calculatorap
import android.media.VolumeShaper.Operation
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var result: EditText? = null
    private var newNumber: EditText? = null
    private val displayOperation by lazy {findViewById<TextView>(R.id.operation)} //another way to store widget reference

    private var operand1: Double? = null
    private var operand2: Double? = 0.0
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        result = findViewById(R.id.resultDisplay)
        newNumber = findViewById((R.id.newNumber))

        //Data input buttons
        val button0: Button = findViewById(R.id.button0);
        val button1: Button = findViewById(R.id.button1);
        val button2: Button = findViewById(R.id.button2);
        val button3: Button = findViewById(R.id.button3);
        val button4: Button = findViewById(R.id.button4);
        val button5: Button = findViewById(R.id.button5);
        val button6: Button = findViewById(R.id.button6);
        val button7: Button = findViewById(R.id.button7);
        val button8: Button = findViewById(R.id.button8);
        val button9: Button = findViewById(R.id.button9);
        val buttonDot: Button = findViewById(R.id.buttonDot);

        //Operation buttons
        val buttonEquals: Button = findViewById(R.id.buttonEquals);
        val buttonPlus: Button = findViewById(R.id.buttonPlus);
        val buttonMinus: Button = findViewById(R.id.buttonMinus);
        val buttonDivide: Button = findViewById(R.id.buttonDivide);
        val buttonMultiply: Button = findViewById(R.id.buttonMultiply);
        val buttonLog: Button = findViewById(R.id.buttonLog);
        val buttonSin: Button = findViewById(R.id.buttonSin);
        val buttonCos: Button = findViewById(R.id.buttonCos);
        val buttonTan: Button = findViewById(R.id.buttonTan);

        //The listener is designed to respond to click events on buttons.
        //When a button associated with this listener is clicked, the code within the listener's onClick method (defined by the lambda) gets executed.
        //lambda expression { v -> ... }, where v represents the clicked view (the button)
        val listener = View.OnClickListener { v->
            val b = v as Button
            newNumber?.append(b.text) //Entered value is displayed in  newNumber textView
        }

        //Assign listener to all input buttons
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        //Valid for binary operators: +,-,*,/,=
        val opBinaryListener = View.OnClickListener { v->
            val op = (v as Button).text.toString() //Clicked button's text is stored in op
            val value = newNumber?.text.toString() //newNumber(input box)'s text is stored in value
            if(value.isNotEmpty()){  //If no value is enetered as i/p, skip, as binary operation can't be performed
                performOperation(value, op)
            }
            pendingOperation = op

            displayOperation.text = pendingOperation //The displayOperation textView displays the pending operation
        }

        //Whenever any of the below buttons is clicked, opBinaryListener is executed
        buttonEquals.setOnClickListener(opBinaryListener)
        buttonPlus.setOnClickListener(opBinaryListener)
        buttonMinus.setOnClickListener(opBinaryListener)
        buttonMultiply.setOnClickListener(opBinaryListener)
        buttonDivide.setOnClickListener(opBinaryListener)
    }
    private fun performOperation(value: String, operation: String){
        displayOperation.text = operation

    }

}