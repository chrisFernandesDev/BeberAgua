package com.exercicios.beberagua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnNotify;
    private EditText editMinutes;
    private TimePicker timePicker;

    private int hour;
    private int minute;
    private int interval;

    private boolean activated = false;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getView();
    }

    private void getView() {
        btnNotify = findViewById(R.id.btn_notify);
        editMinutes = findViewById(R.id.edit_txt_number_interval);
        timePicker = findViewById(R.id.time_picker);

        timePicker.setIs24HourView(true);

        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);

        activated = preferences.getBoolean("activated", false);

        if (activated) {
            btnNotify.setText(R.string.pause);
            int color = ContextCompat.getColor(MainActivity.this, R.color.black);
            btnNotify.setBackgroundColor(color);

            int interval = preferences.getInt("interval", 0);
            int hour = preferences.getInt("hour", timePicker.getCurrentHour());
            int minute = preferences.getInt("minute", timePicker.getCurrentMinute());

            editMinutes.setText(String.valueOf(interval));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);

        }

        btnNotify.setOnClickListener(notifyClick);
    }

    public View.OnClickListener notifyClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String sInterval = editMinutes.getText().toString();

            if (sInterval.isEmpty()) {
                Toast.makeText(MainActivity.this, getString(R.string.mensagem_aviso_error), Toast.LENGTH_SHORT).show();
                return;
            }
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
            interval = Integer.parseInt(sInterval);

            if (!activated) {
                btnNotify.setText(R.string.pause);
                int color = ContextCompat.getColor(MainActivity.this, android.R.color.black); //Por que não está aceitando apenas o this?
                btnNotify.setBackgroundColor(color);
                activated = true;

                SharedPreferences.Editor editor = preferences.edit(); // guarda no xml do proprio celular
                editor.putBoolean("activated", true);
                editor.putInt("interval", interval);
                editor.putInt("hour", hour);
                editor.putInt("minute", minute);
                editor.apply();

            } else {
                btnNotify.setText(R.string.notify);
                int color = ContextCompat.getColor(MainActivity.this, R.color.btn);
                btnNotify.setBackgroundColor(color);
                activated = false;

                SharedPreferences.Editor editor = preferences.edit(); // guarda no xml do proprio celular
                editor.putBoolean("activated", false);
                editor.remove("interval");
                editor.remove("hour");
                editor.remove("minute");
                editor.apply();

            }

            Log.d("Teste", " hora: " + hour + " minuto: " + minute + " intervalo: " + interval);
        }
    };

}