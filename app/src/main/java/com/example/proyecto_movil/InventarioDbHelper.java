package com.example.proyecto_movil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InventarioDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "inventario.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "inventario";

    private static final String COL_ID = "id";
    private static final String COL_NOMBRE = "nombre";
    private static final String COL_CATEGORIA = "categoria";
    private static final String COL_CANTIDAD = "cantidad";
    private static final String COL_UBICACION = "ubicacion";
    private static final String COL_OBSERVACION = "observacion";
    private static final String COL_FECHA_REGISTRO = "fecha_registro";

    public InventarioDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NOMBRE + " TEXT NOT NULL, "
                + COL_CATEGORIA + " TEXT NOT NULL, "
                + COL_CANTIDAD + " INTEGER NOT NULL, "
                + COL_UBICACION + " TEXT, "
                + COL_OBSERVACION + " TEXT, "
                + COL_FECHA_REGISTRO + " TEXT"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertarItem(ItemInventario item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, item.getNombre());
        values.put(COL_CATEGORIA, item.getCategoria());
        values.put(COL_CANTIDAD, item.getCantidad());
        values.put(COL_UBICACION, item.getUbicacion());
        values.put(COL_OBSERVACION, item.getObservacion());
        values.put(COL_FECHA_REGISTRO, item.getFechaRegistro());
        return db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<ItemInventario> obtenerItems() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ItemInventario> lista = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COL_NOMBRE + " ASC");
        while (cursor.moveToNext()) {
            lista.add(desdeCursor(cursor));
        }
        cursor.close();
        return lista;
    }

    public ArrayList<ItemInventario> buscarItems(String texto) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ItemInventario> lista = new ArrayList<>();
        String like = "%" + texto + "%";
        String selection = COL_NOMBRE + " LIKE ? OR " + COL_CATEGORIA + " LIKE ?";
        String[] args = {like, like};
        Cursor cursor = db.query(TABLE_NAME, null, selection, args, null, null, COL_NOMBRE + " ASC");
        while (cursor.moveToNext()) {
            lista.add(desdeCursor(cursor));
        }
        cursor.close();
        return lista;
    }

    public int actualizarItem(ItemInventario item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, item.getNombre());
        values.put(COL_CATEGORIA, item.getCategoria());
        values.put(COL_CANTIDAD, item.getCantidad());
        values.put(COL_UBICACION, item.getUbicacion());
        values.put(COL_OBSERVACION, item.getObservacion());
        values.put(COL_FECHA_REGISTRO, item.getFechaRegistro());
        return db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(item.getId())});
    }

    public int eliminarItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public ItemInventario obtenerItemPorId(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        ItemInventario item = null;
        if (cursor.moveToFirst()) {
            item = desdeCursor(cursor);
        }
        cursor.close();
        return item;
    }

    private ItemInventario desdeCursor(Cursor cursor) {
        return new ItemInventario(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORIA)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_CANTIDAD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_UBICACION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_OBSERVACION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_REGISTRO))
        );
    }
}
