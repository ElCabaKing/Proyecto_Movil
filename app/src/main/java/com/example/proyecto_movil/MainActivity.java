package com.example.proyecto_movil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvSaludo;
    private InventarioDbHelper dbHelper;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new InventarioDbHelper(this);
        preferences = getSharedPreferences("datos_app", MODE_PRIVATE);

        tvSaludo = findViewById(R.id.tvSaludo);

        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        Button btnInventario = findViewById(R.id.btnInventario);
        Button btnPreferencias = findViewById(R.id.btnPreferencias);
        Button btnGenerarReporte = findViewById(R.id.btnGenerarReporte);
        Button btnWebUG = findViewById(R.id.btnWebUG);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormItemActivity.class);
                startActivity(intent);
            }
        });

        btnInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListaItemsActivity.class);
                startActivity(intent);
            }
        });

        btnPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreferenciasActivity.class);
                startActivity(intent);
            }
        });

        btnGenerarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarReporte();
            }
        });

        btnWebUG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ug.edu.ec"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarSaludo();
    }

    private void actualizarSaludo() {
        boolean mostrarSaludo = preferences.getBoolean("mostrar_saludo", false);
        if (mostrarSaludo) {
            String nombre = preferences.getString("nombre", "");
            String paralelo = preferences.getString("paralelo", "");
            String saludo = "Bienvenido, " + nombre;
            if (!paralelo.isEmpty()) {
                saludo += " (" + paralelo + ")";
            }
            tvSaludo.setText(saludo);
            tvSaludo.setVisibility(View.VISIBLE);
        } else {
            tvSaludo.setVisibility(View.GONE);
        }
    }

    private void generarReporte() {
        ArrayList<ItemInventario> items = dbHelper.obtenerItems();
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE INVENTARIO ===\n");
        sb.append("Generado: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date())).append("\n\n");
        if (items.isEmpty()) {
            sb.append("No hay elementos registrados.\n");
        } else {
            for (ItemInventario item : items) {
                sb.append("ID: ").append(item.getId()).append("\n");
                sb.append("Nombre: ").append(item.getNombre()).append("\n");
                sb.append("Categoría: ").append(item.getCategoria()).append("\n");
                sb.append("Cantidad: ").append(item.getCantidad()).append("\n");
                sb.append("Ubicación: ").append(item.getUbicacion()).append("\n");
                sb.append("Observación: ").append(item.getObservacion()).append("\n");
                sb.append("Fecha: ").append(item.getFechaRegistro()).append("\n");
                sb.append("------------------------\n");
            }
        }
        sb.append("=== FIN DEL REPORTE ===");

        try {
            ArchivoHelper.guardarResumenInterno(this, sb.toString());
            ArchivoHelper.guardarReporteExterno(this, sb.toString());
            Toast.makeText(this, R.string.reporte_generado, Toast.LENGTH_SHORT).show();
            compartirReporte(sb.toString());
        } catch (IOException e) {
            Toast.makeText(this, "Error al generar reporte", Toast.LENGTH_SHORT).show();
        }
    }

    private void compartirReporte(String texto) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, texto);
        startActivity(Intent.createChooser(share, getString(R.string.compartir_reporte)));
    }
}
