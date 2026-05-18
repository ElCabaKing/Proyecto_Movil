package com.example.proyecto_movil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaItemsActivity extends AppCompatActivity {

    private EditText etBusqueda;
    private ListView listView;
    private InventarioDbHelper dbHelper;
    private ArrayList<ItemInventario> items;
    private ArrayList<String> nombresItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_items);
        setTitle(R.string.title_lista);

        dbHelper = new InventarioDbHelper(this);

        etBusqueda = findViewById(R.id.etBusqueda);
        Button btnBuscar = findViewById(R.id.btnBuscar);
        listView = findViewById(R.id.listView);

        cargarItems();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = etBusqueda.getText().toString().trim();
                if (texto.isEmpty()) {
                    cargarItems();
                } else {
                    buscarItems(texto);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemInventario item = items.get(position);
                Intent intent = new Intent(ListaItemsActivity.this, DetalleItemActivity.class);
                intent.putExtra("item_id", item.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarItems();
    }

    private void cargarItems() {
        items = dbHelper.obtenerItems();
        mostrarItems();
    }

    private void buscarItems(String texto) {
        items = dbHelper.buscarItems(texto);
        mostrarItems();
    }

    private void mostrarItems() {
        nombresItems = new ArrayList<>();
        for (ItemInventario item : items) {
            nombresItems.add(item.getNombre() + " - " + item.getCategoria() + " (" + item.getCantidad() + ")");
        }
        if (nombresItems.isEmpty()) {
            nombresItems.add(getString(R.string.empty_list));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nombresItems
        );
        listView.setAdapter(adapter);
    }
}
