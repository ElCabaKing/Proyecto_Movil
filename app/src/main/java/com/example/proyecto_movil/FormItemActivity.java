package com.example.proyecto_movil;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormItemActivity extends AppCompatActivity {

    private EditText etNombre, etCategoria, etCantidad, etUbicacion, etObservacion;
    private InventarioDbHelper dbHelper;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_item);

        dbHelper = new InventarioDbHelper(this);

        etNombre = findViewById(R.id.etNombre);
        etCategoria = findViewById(R.id.etCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        etUbicacion = findViewById(R.id.etUbicacion);
        etObservacion = findViewById(R.id.etObservacion);

        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        if (getIntent().hasExtra("item_id")) {
            itemId = getIntent().getIntExtra("item_id", -1);
            setTitle(R.string.title_editar);
            cargarItem(itemId);
        } else {
            setTitle(R.string.title_registrar);
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarItem();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarItem(int id) {
        ItemInventario item = dbHelper.obtenerItemPorId(id);
        if (item != null) {
            etNombre.setText(item.getNombre());
            etCategoria.setText(item.getCategoria());
            etCantidad.setText(String.valueOf(item.getCantidad()));
            etUbicacion.setText(item.getUbicacion());
            etObservacion.setText(item.getObservacion());
        }
    }

    private void guardarItem() {
        String nombre = etNombre.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String observacion = etObservacion.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError(getString(R.string.campo_obligatorio));
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                etCantidad.setError(getString(R.string.debe_ser_mayor_cero));
                return;
            }
        } catch (NumberFormatException e) {
            etCantidad.setError(getString(R.string.numero_invalido));
            return;
        }

        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (itemId == -1) {
            ItemInventario item = new ItemInventario(0, nombre, categoria, cantidad, ubicacion, observacion, fecha);
            long resultado = dbHelper.insertarItem(item);
            if (resultado != -1) {
                Toast.makeText(this, R.string.item_guardado, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.error_guardar, Toast.LENGTH_SHORT).show();
            }
        } else {
            ItemInventario item = new ItemInventario(itemId, nombre, categoria, cantidad, ubicacion, observacion, fecha);
            int resultado = dbHelper.actualizarItem(item);
            if (resultado > 0) {
                Toast.makeText(this, R.string.item_actualizado, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.error_guardar, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
