package com.example.proyecto_movil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleItemActivity extends AppCompatActivity {

    private TextView tvNombre, tvCategoria, tvCantidad, tvUbicacion, tvObservacion, tvFecha;
    private InventarioDbHelper dbHelper;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item);
        setTitle(R.string.title_detalle);

        dbHelper = new InventarioDbHelper(this);

        tvNombre = findViewById(R.id.tvNombre);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvCantidad = findViewById(R.id.tvCantidad);
        tvUbicacion = findViewById(R.id.tvUbicacion);
        tvObservacion = findViewById(R.id.tvObservacion);
        tvFecha = findViewById(R.id.tvFecha);

        Button btnEditar = findViewById(R.id.btnEditar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        Button btnVolver = findViewById(R.id.btnVolver);

        itemId = getIntent().getIntExtra("item_id", -1);
        cargarItem();

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleItemActivity.this, FormItemActivity.class);
                intent.putExtra("item_id", itemId);
                startActivity(intent);
                finish();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoEliminar();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarItem() {
        ItemInventario item = dbHelper.obtenerItemPorId(itemId);
        if (item != null) {
            tvNombre.setText(item.getNombre());
            tvCategoria.setText("Categoría: " + item.getCategoria());
            tvCantidad.setText("Cantidad: " + item.getCantidad());
            tvUbicacion.setText("Ubicación: " + (item.getUbicacion().isEmpty() ? "—" : item.getUbicacion()));
            tvObservacion.setText("Observación: " + (item.getObservacion().isEmpty() ? "—" : item.getObservacion()));
            tvFecha.setText("Registrado: " + item.getFechaRegistro());
        }
    }

    private void mostrarDialogoEliminar() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmar_eliminacion)
                .setMessage(R.string.mensaje_eliminar)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.eliminarItem(itemId);
                        Toast.makeText(DetalleItemActivity.this, R.string.item_eliminado, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
