package edu.csce4623.rghosh.classcalcexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.widget.Button;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // displays result to user
    TextView result;
    // lil indicater in top right of result to show which action was just pressed
    TextView currentAction;
    // operands to help with calculations
    String operand1 = "";
    String operand2 = "";
    // to help determine which operand currently on (true is operand1, false is operand2)
    boolean whichOperand = true;
    // actual result of calculation
    double numResult = 0.0;
    // to help keep track of if user just hit equal
    boolean justEqualled = false;
    // to help keep track of which operation user is doing
    String action = "";

    @Override
    // very first method that is called upon launch; is a life cycle method
    protected void onCreate(Bundle savedInstanceState) {
        // if u have a saved instance, will pull that first
        super.onCreate(savedInstanceState);
        // inflates view (pulls it up)
        setContentView(R.layout.activity_main);

        // to display results
        result = (TextView) findViewById(R.id.textView);
        currentAction = (TextView) findViewById(R.id.action);
    }

    // helper function to set operands
    private void setOperand(String num) {
        if (whichOperand) {
            operand1+=num;
            result.setText(operand1);
        } else {
            operand2+=num;
            result.setText(operand2);
        }
    }

    // helper function to help determine if result is int or not
    private boolean isInt(double number) {
        if (Math.floor(number) == number) {
            return true;
        } else {
            return false;
        }
    }

    private void doAction(String operation) {
        if (operation == "Add") {
            numResult = Double.parseDouble(operand1) + Double.parseDouble(operand2);
            setTextResult(numResult);
        }
        if (operation == "Sub") {
            numResult = Double.parseDouble(operand1) - Double.parseDouble(operand2);
            setTextResult(numResult);
        }
        if (operation == "Mult") {
            numResult = Double.parseDouble(operand1) * Double.parseDouble(operand2);
            setTextResult(numResult);
        }
        if (operation == "Div") {
            numResult = Double.parseDouble(operand1) / Double.parseDouble(operand2);
            setTextResult(numResult);
        }
    }

    // helper function to determine int vs float display & set TextView result
    private void setTextResult(double numResult) {
        // formatted result in case is too many digits
        String formattedResult;
        if (isInt(numResult) && !Double.toString(numResult).contains("E")) {
            if (Integer.toString((int)numResult).length() > 14) {
                result.setText(Integer.toString((int)numResult).substring(0, 13));
                formattedResult = Integer.toString((int)numResult).substring(0, 13);
            }
            else {
                result.setText(Integer.toString((int)numResult));
                formattedResult = Integer.toString((int)numResult);
            }
        } else {
            if (Double.toString(numResult).contains("E")) {
                result.setText(String.format("%12.7E",numResult));
                formattedResult = String.format("%12.7E",numResult);
            } else {
                result.setText(String.format("%12.7f", numResult));
                formattedResult = String.format("%12.7f", numResult);
            }
        }
        // redefine operand1 to be result & current operand, and reset operand2
        operand1 = formattedResult;
        operand2 = "";
        whichOperand = true;
    }

    @Override
    public void onClick(View view) {
        // in the case app crashes, gives user a warning message
        AlertDialog.Builder errorMsg = new AlertDialog.Builder(MainActivity.this);
        errorMsg.setMessage("error! cannot begin with an operation; " +
                "enter number then use +/- symbol. please press clear and re-enter calculation" +
                " correctly");
        errorMsg.setCancelable(true);
        try {
            // get & store tag of button that was clicked
            String tag = view.getTag().toString();
            // if button is a number or decimal
            if (tag.startsWith("num")) {
                // if just hit equal and tries to put in another num, resets operand1
                if (justEqualled) {
                    operand1 = "";
                }
                // get which particular number (or substring)
                String number = tag.substring(3, tag.length());
                // set operands accordingly by calling helper function
                setOperand(number);
                justEqualled = false;
            }

            // if button is an operator/other
            if (tag.startsWith("op")) {
                // to use later to set little most recent action in top right corner of view
                Button clickedButton = (Button) view;
                // retrieve which particular operator
                String operation = tag.substring(2, tag.length());
                // act accordingly
                switch (operation) {
                    case "Add":
                        // in case user wishes to just string together operations (bypassing =)
                        if (!justEqualled && operand2 != "") {
                            doAction(action);
                        }
                        // set action so when "=" is clicked, it knows what's happening
                        action = "Add";
                        // justEqualled is false since diff op, and whichOperand now indicates operand2
                        justEqualled = false;
                        whichOperand = !whichOperand;
                        break;
                    case "Sub":
                        if (!justEqualled && operand2 != "") {
                            doAction(action);
                        }
                        action = "Sub";
                        justEqualled = false;
                        whichOperand = !whichOperand;
                        break;
                    case "Mult":
                        if (!justEqualled && operand2 != "") {
                            doAction(action);
                        }
                        action = "Mult";
                        justEqualled = false;
                        whichOperand = !whichOperand;
                        break;
                    case "Div":
                        if (!justEqualled && operand2 != "") {
                            doAction(action);
                        }
                        action = "Div";
                        justEqualled = false;
                        whichOperand = !whichOperand;
                        break;
                    case "Back":
                        // determine which operand, and remove last character
                        if (whichOperand) {
                            operand1 = operand1.substring(0, operand1.length() - 1);
                            result.setText(operand1);
                        } else {
                            operand2 = operand2.substring(0, operand2.length() - 1);
                            result.setText(operand2);
                        }
                        // justEqualled is false since diff op
                        justEqualled = false;
                        break;
                    case "Flip":
                        // determine which operand, and flip sign by first converting to int or double
                        if (whichOperand) {
                            if (isInt(Double.parseDouble(operand1))) {
                                operand1 = Integer.toString((int) -(Double.parseDouble(operand1)));
                            } else {
                                operand1 = Double.toString(-(Double.parseDouble(operand1)));
                            }
                            result.setText(operand1);
                        } else {
                            if (isInt(Double.parseDouble(operand2)))
                                operand2 = Integer.toString((int) -(Double.parseDouble(operand2)));
                            else {
                                operand2 = Double.toString(-(Double.parseDouble(operand2)));
                            }
                            result.setText(operand2);
                        }
                        // justEqualled is false since diff op
                        justEqualled = false;
                        break;
                    case "Clear":
                        // clears and resets everything
                        operand1 = "";
                        operand2 = "";
                        whichOperand = true;
                        justEqualled = false;
                        action = "";
                        result.setText("");
                        break;
                    case "Equal":
                        // goes thru depending on current action, operates, & displays value
                        doAction(action);
                        // justEqualled is false since diff op
                        justEqualled = true;
                        break;
                    default:
                        break;
                }
                // sets most recent action in top right corner of view
                currentAction.setText(clickedButton.getText().toString());
            }
        }
        catch(Exception e) {
            AlertDialog alertError = errorMsg.create();
            alertError.show();
        }
    }

}