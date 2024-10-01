package com.example.calculatorap
import android.media.VolumeShaper.Operation
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.Math.toRadians
import kotlin.math.*
//import kotlinx.android.synthetic.main.activity_main.*

// To preserve operand and pendingOp when activity is destroyed:
//These constants serve as keys for storing and retrieving specific pieces of data (operand1 and pendingOperation) in the Bundle object.
private const val STATE_PENDING_OPERATION = "pendingOperation"
private const val STATE_OPERAND1 = "operand1"
private const val STATE_OPERAND1_STORED = "operand1_stored"

class MainActivity : AppCompatActivity() {
    private var result: EditText? = null
    private var newNumber: EditText? = null
    private val displayOperation by lazy {findViewById<TextView>(R.id.operation)} //another way to store widget reference

    private var operand1: Double? = null //operand1 can be null
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
        val buttonExp: Button = findViewById(R.id.buttonExp);
        val buttonFactorial: Button = findViewById(R.id.buttonFactorial);
        val buttonSqrt: Button = findViewById(R.id.buttonSqrt);
       // val buttonExp: Button = findViewById(R.id.buttonExp);

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
        fun getOperationListener(isBinaryOperation: Boolean): View.OnClickListener {
            return View.OnClickListener { v ->
                val op = (v as Button).text.toString() //Clicked button's text is stored in op
                try {    //To prevent the app from crashing when . is passed as I/P and an operator button is clicked
                    val value = newNumber?.text.toString().toDouble() //newNumber(input box)'s text is stored in value
                    if (isBinaryOperation) {
                        performBinaryOperation(value, op)
                    } else {
                        performUnaryOperation(value, op)
                    }
                }catch(e: NumberFormatException){ //When value = . (Exception)
                    newNumber?.setText("")
                }
                pendingOperation = op

                displayOperation.text = pendingOperation //The displayOperation textView displays the pending operation
                result?.setText(operand1.toString()) //Display operand1 in result box
                newNumber?.setText("")  //After the result is displayed, clear the I/P box's text
            }
        }

        //1: Binary operator, 0: Unary operator
        buttonEquals.setOnClickListener(getOperationListener(true))
        buttonPlus.setOnClickListener(getOperationListener(true))
        buttonMinus.setOnClickListener(getOperationListener(true))
        buttonMultiply.setOnClickListener(getOperationListener(true))
        buttonDivide.setOnClickListener(getOperationListener(true))
        buttonExp.setOnClickListener(getOperationListener(true))

        buttonSin.setOnClickListener(getOperationListener(false))
        buttonLog.setOnClickListener(getOperationListener(false))
        buttonCos.setOnClickListener(getOperationListener(false))
        buttonTan.setOnClickListener(getOperationListener(false))
        buttonFactorial.setOnClickListener(getOperationListener(false))
        buttonSqrt.setOnClickListener(getOperationListener(false))
    }
    private fun performBinaryOperation(value: Double, operation: String){
        if(operand1 == null){ //If opr1 is null, no operand was previously entered, assign the entered value to operand1
            operand1 = value
        }
        else{
            if(pendingOperation == "="){
                pendingOperation = operation
            }
            when(pendingOperation){
                "=" -> operand1 = value
                "/" -> operand1 = if(value == 0.0) {  //division by Zero
                    Double.NaN
                }else{
                    operand1!! / value// !! ensures operand1 is only calculated if operand1 is not null
                }
                "+" -> operand1 = operand1!! + value
                "-" -> operand1 = operand1!! - value
                "*" -> operand1 = operand1!! * value
                "^"-> operand1 = operand1!!.pow(value)
            }
        }

    }
    private fun performUnaryOperation(value:Double, operation: String){

        when(operation){
            "sin" -> operand1 = sin(toRadians(value))
            "cos" -> operand1 = cos(toRadians(value))
            "tan" -> operand1 = tan(toRadians(value))
            "log" -> operand1 = if (value > 0) log10(value) else Double.NaN
            "sqrt" -> operand1 = if (value >= 0) sqrt(value) else Double.NaN
            "!" -> operand1 = if(value >= 0 && value == value.toInt().toDouble()) factorial(value.toInt()).toDouble() else Double.NaN
        }
    }
    private fun factorial(n: Int): Long{
        var res:Long = 1
        for(i in 2..n) res *= i
        return res;
    }
    override fun onSaveInstanceState(outState: Bundle) { //To save the operand1, pending operation before activity is destroyed i.e when mode is changed from portrait to landscape
        super.onSaveInstanceState(outState)
        if(operand1 != null){ //If operand1 has a value (i.e., it's not null), it's saved into the Bundle using the key STATE_OPERAND1.
                                // This ensures that the first operand is preserved across activity recreation.
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        //Regardless of whether pendingOperation is null or not, it's saved into the Bundle using the key STATE_PENDING_OPERATION.
        //his ensures that the current operation is preserved across activity recreation.
        outState?.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { //Restores the state of the activity, ensuring that user data and UI elements maintain their state across activity recreation.
        super.onRestoreInstanceState(savedInstanceState)

       /* Checks a boolean flag (STATE_OPERAND1_STORED) to determine if
           the first operand was saved before the activity was destroyed.
           If the flag is true, it retrieves the stored double value associated with the key STATE_OPERAND1
           If the flag is false, it sets operand1 to null, indicating that there is no first operand stored.*/
        if(savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)){
            operand1 = savedInstanceState.getDouble(STATE_OPERAND1)
        }
        else{
            operand1 = null
        }
       // Retrieves the string value associated with the key STATE_PENDING_OPERATION, which represents the operation that has been selected by the user.
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        displayOperation.text = pendingOperation

    }
}