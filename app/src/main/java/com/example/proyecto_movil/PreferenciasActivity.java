package com.example.proyecto_movil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PreferenciasActivity extends AppCompatActivity {

    private EditText etNombre, etParalelo;
    private Switch switchMostrarSaludo;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        setTitle(R.string.title_preferencias);

        preferences = getSharedPreferences("datos_app", MODE_PRIVATE);

        etNombre = findViewById(R.id.etNombreEstudiante);
        etParalelo = findViewById(R.id.etParalelo);
        switchMostrarSaludo = findViewById(R.id.switchMostrarSaludo);
        Button btnGuardar = findViewById(R.id.btnGuardarPreferencias);

        cargarPreferencias();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPreferencias();
            }
        });
    }

    private void cargarPreferencias() {
        String nombre = preferences.getString("nombre", "");
        String paralelo = preferences.getString("paralelo", "");
        boolean mostrarSaludo = preferences.getBoolean("mostrar_saludo", false);

        etNombre.setText(nombre);
        etParalelo.setText(paralelo);
        switchMostrarSaludo.setChecked(mostrarSaludo);
    }

    private void guardarPreferencias() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombre", etNombre.getText().toString().trim());
        editor.putString("paralelo", etParalelo.getText().toString().trim());
        editor.putBoolean("mostrar_saludo", switchMostrarSaludo.isChecked());
        editor.apply();

        Toast.makeText(this, R.string.preferencias_guardadas, Toast.LENGTH_SHORT).show();
        finish();
    }
}
