# Inventario Académico UG — Guía Técnica del Proyecto

## Descripción

Aplicación Android nativa desarrollada en Java utilizando XML, SQLite, SharedPreferences, archivos internos/externos e Intents.

La aplicación permitirá:

- Registrar elementos académicos
- Listar elementos
- Buscar elementos
- Ver detalles
- Editar elementos
- Eliminar elementos
- Guardar preferencias
- Generar reportes
- Compartir reportes

---

# Tecnologías a utilizar

## Lenguaje
- Java

## Interfaz
- XML

## Persistencia
- SQLite
- SharedPreferences
- Archivos internos
- Archivos externos propios de la app

## Componentes Android
- Activities
- Intents explícitos
- Intents implícitos
- Toast
- AlertDialog
- ListView

---

# Restricciones importantes

NO usar:

- Firebase
- APIs externas
- Retrofit
- Room
- MVVM
- Jetpack Compose
- Hilt
- Arquitecturas complejas
- Librerías raras

Mantener el proyecto lo más vanilla posible.

---

# Estructura recomendada del proyecto

```text
app/
└── src/main/
    ├── java/com/example/inventarioug/
    │
    ├── MainActivity.java
    ├── FormItemActivity.java
    ├── ListaItemsActivity.java
    ├── DetalleItemActivity.java
    ├── PreferenciasActivity.java
    │
    ├── ItemInventario.java
    ├── InventarioDbHelper.java
    ├── ArchivoHelper.java
    │
    └── res/
        ├── layout/
        │
        ├── activity_main.xml
        ├── activity_form_item.xml
        ├── activity_lista_items.xml
        ├── activity_detalle_item.xml
        ├── activity_preferencias.xml
        │
        ├── values/
        │   ├── strings.xml
        │   └── colors.xml
        │
        └── drawable/
            └── logo_ug.png
```

---

# Flujo general de la aplicación

```text
MainActivity
│
├── Registrar elemento
│       ↓
│   FormItemActivity
│
├── Ver inventario
│       ↓
│   ListaItemsActivity
│       ↓
│   DetalleItemActivity
│
├── Preferencias
│       ↓
│   PreferenciasActivity
│
├── Generar reporte
│
└── Abrir web UG
```

---

# Pantallas necesarias

# 1. MainActivity

Pantalla principal.

## Debe contener

- Título de la aplicación
- Imagen/logo
- Nombre del estudiante
- Botón registrar
- Botón inventario
- Botón preferencias
- Botón generar reporte
- Botón abrir web UG

## Layout recomendado

- ConstraintLayout
o
- LinearLayout

## Componentes XML

```xml
TextView
ImageView
Button
```

## Navegación

| Botón | Acción |
|---|---|
| Registrar | Abre FormItemActivity |
| Inventario | Abre ListaItemsActivity |
| Preferencias | Abre PreferenciasActivity |
| Generar reporte | Genera archivos |
| Web UG | Intent implícito |

---

# 2. FormItemActivity

Pantalla para registrar o editar elementos.

## Campos requeridos

| Campo | Tipo |
|---|---|
| Nombre | EditText |
| Categoría | EditText o Spinner |
| Cantidad | EditText number |
| Ubicación | EditText |
| Observación | EditText |

## Botones

- Guardar
- Cancelar

## Layout recomendado

- LinearLayout
o
- TableLayout

## Validaciones obligatorias

### Nombre obligatorio

```java
if(nombre.isEmpty()) {
    etNombre.setError("Campo obligatorio");
    return;
}
```

### Cantidad válida

```java
try {

    int cantidad =
        Integer.parseInt(
            etCantidad.getText().toString()
        );

    if(cantidad <= 0) {

        etCantidad.setError(
            "Debe ser mayor que cero"
        );

        return;
    }

} catch(Exception e) {

    etCantidad.setError(
        "Número inválido"
    );

}
```

---

# 3. ListaItemsActivity

Pantalla para mostrar todos los elementos guardados.

## Debe contener

- ListView
- EditText búsqueda
- Botón buscar

## Funcionalidad

Al hacer click sobre un elemento:

```java
DetalleItemActivity
```

## ListView simple

```java
ArrayAdapter<String> adapter =
    new ArrayAdapter<>(
        this,
        android.R.layout.simple_list_item_1,
        lista
    );

listView.setAdapter(adapter);
```

---

# 4. DetalleItemActivity

Pantalla que muestra todos los datos del elemento seleccionado.

## Funcionalidades

- Editar
- Eliminar
- Volver

## Eliminación

Usar:

```java
AlertDialog
```

---

# 5. PreferenciasActivity

Pantalla para guardar configuraciones usando SharedPreferences.

## Campos

| Campo | Tipo |
|---|---|
| Nombre estudiante | EditText |
| Paralelo | EditText |
| Mostrar saludo | Switch |

---

# Modelo de datos

# Clase ItemInventario

```java
public class ItemInventario {

    private int id;
    private String nombre;
    private String categoria;
    private int cantidad;
    private String ubicacion;
    private String observacion;
    private String fechaRegistro;

    public ItemInventario() {
    }

    public ItemInventario(
            int id,
            String nombre,
            String categoria,
            int cantidad,
            String ubicacion,
            String observacion,
            String fechaRegistro
    ) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.ubicacion = ubicacion;
        this.observacion = observacion;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
}
```

---

# SQLite

# Clase InventarioDbHelper

Debe extender:

```java
SQLiteOpenHelper
```

---

# Base de datos

## Nombre

```text
inventario.db
```

## Tabla

```sql
CREATE TABLE inventario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    categoria TEXT NOT NULL,
    cantidad INTEGER NOT NULL,
    ubicacion TEXT,
    observacion TEXT,
    fecha_registro TEXT
);
```

---

# Métodos obligatorios

## Insertar

```java
public long insertarItem(ItemInventario item)
```

## Listar

```java
public ArrayList<ItemInventario> obtenerItems()
```

## Buscar

```java
public ArrayList<ItemInventario> buscarItems(String texto)
```

## Actualizar

```java
public int actualizarItem(ItemInventario item)
```

## Eliminar

```java
public int eliminarItem(int id)
```

---

# Uso obligatorio de ContentValues

```java
ContentValues values = new ContentValues();

values.put("nombre", item.getNombre());
values.put("categoria", item.getCategoria());
values.put("cantidad", item.getCantidad());
values.put("ubicacion", item.getUbicacion());
values.put("observacion", item.getObservacion());
values.put("fecha_registro", item.getFechaRegistro());

db.insert("inventario", null, values);
```

---

# SharedPreferences

# Crear preferencias

```java
SharedPreferences preferences =
    getSharedPreferences("datos_app", MODE_PRIVATE);
```

---

# Datos a guardar

- Nombre estudiante
- Paralelo
- Mostrar saludo

---

# Guardar datos

```java
SharedPreferences.Editor editor = preferences.edit();

editor.putString("nombre", nombre);
editor.putString("paralelo", paralelo);
editor.putBoolean("mostrar_saludo", true);

editor.apply();
```

---

# Leer datos

```java
String nombre =
    preferences.getString("nombre", "");
```

---

# Archivos internos

# Archivo requerido

```text
resumen_interno.txt
```

---

# Clase ArchivoHelper

```java
public class ArchivoHelper {
}
```

---

# Guardar archivo interno

```java
FileOutputStream fos =
    context.openFileOutput(
        "resumen_interno.txt",
        Context.MODE_PRIVATE
    );

fos.write(texto.getBytes());

fos.close();
```

---

# Archivos externos

# Archivo requerido

```text
reporte_inventario.txt
```

---

# Obtener directorio externo

```java
File directorio = getExternalFilesDir(null);
```

---

# Guardar archivo externo

```java
File archivo =
    new File(
        directorio,
        "reporte_inventario.txt"
    );

FileOutputStream fos =
    new FileOutputStream(archivo);

fos.write(texto.getBytes());

fos.close();
```

---

# Compartir reporte

```java
Intent share =
    new Intent(Intent.ACTION_SEND);

share.setType("text/plain");

share.putExtra(
    Intent.EXTRA_TEXT,
    textoReporte
);

startActivity(
    Intent.createChooser(
        share,
        "Compartir reporte"
    )
);
```

---

# Intents

# Intent explícito

```java
Intent intent =
    new Intent(
        MainActivity.this,
        ListaItemsActivity.class
    );

startActivity(intent);
```

---

# Intent implícito

Abrir página web de la UG.

```java
Intent intent = new Intent(
    Intent.ACTION_VIEW,
    Uri.parse("https://www.ug.edu.ec")
);

startActivity(intent);
```

---

# Búsqueda simple

Usar:

```java
EditText
```

y:

```java
Button
```

Consulta:

```sql
SELECT * FROM inventario
WHERE nombre LIKE ?
OR categoria LIKE ?
```

---

# Uso obligatorio de strings.xml

NO colocar textos hardcodeados.

## Correcto

```xml
<string name="btn_guardar">
    Guardar
</string>
```

Uso:

```xml
android:text="@string/btn_guardar"
```

---

# Activities mínimas

La aplicación tendrá:

- MainActivity
- FormItemActivity
- ListaItemsActivity
- DetalleItemActivity
- PreferenciasActivity

---

# AndroidManifest.xml

Registrar Activities.

```xml
<activity android:name=".FormItemActivity"/>
<activity android:name=".ListaItemsActivity"/>
<activity android:name=".DetalleItemActivity"/>
<activity android:name=".PreferenciasActivity"/>
```

---

# Diseño recomendado

Mantener diseño simple.

## Usar

- padding
- margin
- textSize
- match_parent
- wrap_content

## Evitar

- RecyclerView
- MVVM
- Compose
- Room
- Arquitecturas avanzadas

---

# Orden recomendado de desarrollo

## Fase 1
Layouts XML

## Fase 2
Navegación entre Activities

## Fase 3
Validaciones

## Fase 4
SQLite CRUD

## Fase 5
ListView

## Fase 6
SharedPreferences

## Fase 7
Archivos internos

## Fase 8
Archivos externos

## Fase 9
Compartir reporte

## Fase 10
Pruebas generales

---

# Objetivo final

La aplicación debe:

- Abrir sin errores
- Navegar correctamente entre pantallas
- Guardar datos en SQLite
- Permitir CRUD completo
- Guardar preferencias
- Generar reportes
- Exportar archivos
- Compartir información
- Mantener código simple y organizado