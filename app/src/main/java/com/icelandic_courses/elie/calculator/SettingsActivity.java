package com.icelandic_courses.elie.calculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

    private static boolean allowToVibrate;
    private CheckBox checkBoxVibration;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //getSharedPrefs
        prefs = getSharedPreferences("Calculator", 0);

        final Spinner spinnerTheme = (Spinner) findViewById(R.id.theme);
        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedId = (int) spinnerTheme.getSelectedItemId();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("theme", selectedId);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set theme
        int theme = prefs.getInt("theme", 0);
        spinnerTheme.setSelection(theme);

        checkBoxVibration = (CheckBox) findViewById(R.id.vibrateCheckBox);
        checkBoxVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = checkBoxVibration.isChecked();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("vibration", checked);
                editor.commit();
            }
        });
        checkBoxVibration.setChecked(prefs.getBoolean("vibration", false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
