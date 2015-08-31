package com.icelandic_courses.elie.calculator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {

    private static boolean allowToVibrate;
    private SharedPreferences prefs;
    public final static String EXTRA_VIBRATION = "vibration";
    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("Calculator", 0);

        TextView myTextView=(TextView)findViewById(R.id.resultTextView);
        Typeface typeFace=Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
        myTextView.setTypeface(typeFace);

        vibe = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button addButton = (Button)findViewById(R.id.addButton);
        Button substractButton = (Button)findViewById(R.id.substractButton);
        Button backspaceButton = (Button)findViewById(R.id.backspaceButton);
        Button clearButton = (Button)findViewById(R.id.clearButton);
        Button resultButton = (Button)findViewById(R.id.resultButton);

        int buttonRes = (prefs.getInt("theme", 0) == 0) ? R.drawable.button_green : R.drawable.button_blue;

        addButton.setBackgroundResource(buttonRes);
        substractButton.setBackgroundResource(buttonRes);
        backspaceButton.setBackgroundResource(buttonRes);
        clearButton.setBackgroundResource(buttonRes);
        resultButton.setBackgroundResource(buttonRes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //return true;
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(EXTRA_VIBRATION, allowToVibrate);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Append a number
     * @param view
     */
    public void appendNumber(View view) {
        vibrate();
        Button b = (Button) view;
        TextView tv = (TextView) findViewById(R.id.resultTextView);
        tv.append(b.getText());
    }

    /**
     * Append an operator and validate it
     * @param view
     */
    public void appendOperator(View view) {
        vibrate();
        Button b = (Button) view;
        TextView tv = (TextView) findViewById(R.id.resultTextView);

        String code = tv.getText().toString();
        String operator = b.getText().toString();

        //check for operator at end and remove it
        if(code.length() != 0) {
            char lastCodeChar = code.charAt(code.length() - 1);
            if(lastCodeChar == '+' || lastCodeChar == '-') {
                code = code.subSequence(0, code.length() - 1).toString();
            }
        }

        //append operator
        code = code + operator;

        //apply it
        tv.setText(code);
    }

    /**
     * Remove the last character of the text view
     * @param view
     */
    public void backspace(View view) {
        vibrate();
        TextView tv = (TextView) findViewById(R.id.resultTextView);
        CharSequence text = tv.getText();
        text = text.subSequence(0, Math.max(text.length()-1, 0));
        tv.setText(text);
    }

    /**
     * Clear the text view
     * @param view
     */
    public void clear(View view) {
        vibrate();
        TextView tv = (TextView) findViewById(R.id.resultTextView);
        tv.setText("");
    }

    /**
     * Calculate the result
     * @param view
     */
    public void result(View view) {
        vibrate();
        TextView tv = (TextView) findViewById(R.id.resultTextView);

        // add 0 to be able to calculate "-300+100" as "0-300+100"
        CharSequence code = "0" + tv.getText();

        int i = 0;
        int result = 0;
        int currentValue = 0;
        char lastOperator = '+';

        do {
            char currentChar = code.charAt(i++);
            if(currentChar >= '0' && currentChar <= '9') {
                int currentNumber = Character.getNumericValue(currentChar);
                currentValue *= 10;
                currentValue += currentNumber;
            }
            else if(currentChar == '+' || currentChar == '-') {
                if(lastOperator == '+') {
                    result += currentValue;
                    currentValue = 0;
                }
                else if(lastOperator == '-') {
                    result -= currentValue;
                    currentValue = 0;
                }
                lastOperator = currentChar;
            }
        } while(i < code.length());

        result = result + (lastOperator == '+' ? 1 : -1) * currentValue;

        //apply result value
        tv.setText(String.valueOf(result));
    }

    private void vibrate(){
        if(isAllowToVibrate()){
            vibe.vibrate(100);
        }
    }

    public static void allowVibration(){
        allowToVibrate = true;
    }

    public static void disableVibration(){
        allowToVibrate = false;
    }

    public static boolean isAllowToVibrate(){
        return allowToVibrate;
    }


}
