package com.example.proyecto_movil;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArchivoHelper {

    public static void guardarResumenInterno(Context context, String texto) throws IOException {
        FileOutputStream fos = context.openFileOutput("resumen_interno.txt", Context.MODE_PRIVATE);
        fos.write(texto.getBytes());
        fos.close();
    }

    public static void guardarReporteExterno(Context context, String texto) throws IOException {
        File directorio = context.getExternalFilesDir(null);
        if (directorio != null) {
            File archivo = new File(directorio, "reporte_inventario.txt");
            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(texto.getBytes());
            fos.close();
        }
    }
}
