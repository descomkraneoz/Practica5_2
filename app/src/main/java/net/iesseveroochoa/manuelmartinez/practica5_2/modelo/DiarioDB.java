package net.iesseveroochoa.manuelmartinez.practica5_2.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioContract.DiaDiarioEntries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiarioDB {
    //Constantes de versión y fichero de la base de datos
    private final static int DB_VERSION = 1;
    private final static String DB_NOMBRE = "diario.db";

    //Sentencias de sql de creacion y eliminación de la tabla

    private final static String SQL_CREATE = "CREATE TABLE if not exists " + DiaDiarioEntries.TABLE_NAME + " (" +
            DiaDiarioEntries.ID + " integer primary key autoincrement," +
            DiaDiarioEntries.FECHA + " TEXT UNIQUE NOT NULL," +
            DiaDiarioEntries.VALORACION + " INTEGER NOT NULL," +
            DiaDiarioEntries.RESUMEN + " TEXT," +
            DiaDiarioEntries.CONTENIDO + " TEXT," +
            DiaDiarioEntries.FOTO_URI + " TEXT," +
            DiaDiarioEntries.LATITUD + " TEXT," +
            DiaDiarioEntries.LONGITUD + " TEXT" +
            ")";

    private final static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + DiaDiarioEntries.TABLE_NAME;


    /**
     * DBHelper
     */


    //variables necesarias para la base de datos
    private DiaDiarioDbHelper dbH;
    private SQLiteDatabase db;

    //Nos permitira crear la base de datos
    public DiarioDB(Context context) {
        dbH = new DiaDiarioDbHelper(context);
    }

    //Abre la base de datos
    public void open() throws SQLiteException {
        if ((db == null) || (!db.isOpen())) {
            db = dbH.getWritableDatabase();
        }
    }

    /**
     * Metodos para la fecha
     */

    public static Date fechaBDtoFecha(String f) {
        //Varibale tipo date para almacenar la fecha
        Date fecha = null;
        try {
            //Se pasa el string a Date aplicandole el formato indicado en el SimpleDateFormat y se guarda en la variable
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(f);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        //Se devuelve la fecha
        return fecha;
    }

    //Clase interna con la que decir a android que cree la BD y lo que hacer cuando se modifica la version de la BD
    private class DiaDiarioDbHelper extends SQLiteOpenHelper {

        //Constructor de la clase
        public DiaDiarioDbHelper(Context context) {
            super(context, DB_NOMBRE, null, DB_VERSION);
        }

        //Metodos de la clase que debemos sobreescribir

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_TABLE);
            db.execSQL(SQL_CREATE);
        }
    }

    public static String fechaToFechaDB(Date fecha) {
        //Se pasa la fecha a String aplicandole el formato del SimpleDateFormat y se devuelve
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
    }

    /**
     * Dada una posición del cursor, nos devuelve un objeto Dia
     */
    public static DiaDiario deCursorADia(Cursor c) {
        //Obtenemos los datos del cursor por medio del nombre de la columna dado en DiaDiarioEntry
        Date fecha = DiarioDB.fechaBDtoFecha(c.getString(c.getColumnIndex(DiaDiarioEntries.FECHA)));
        int valoracionDia = c.getInt((c.getColumnIndex(DiaDiarioEntries.VALORACION)));
        String resumen = c.getString(c.getColumnIndex(DiaDiarioEntries.RESUMEN));
        String contenido = c.getString(c.getColumnIndex(DiaDiarioEntries.CONTENIDO));
        String fotoUri = c.getString(c.getColumnIndex(DiaDiarioEntries.FOTO_URI));
        String latitud = c.getString(c.getColumnIndex(DiaDiarioEntries.LATITUD));
        String longitud = c.getString(c.getColumnIndex(DiaDiarioEntries.LONGITUD));
        //Devolvemos un nuevo DiaDiario con los datos obtenidos
        return new DiaDiario(fecha, valoracionDia, resumen, contenido, fotoUri, latitud, longitud);
    }

    //Cierra la base de datos
    public void close() throws SQLiteException {
        if (db.isOpen()) {
            db.close();
        }
    }

    /**
     * CRUD del sql
     */

    //Inserta nuevo Dia en el diario pasado por parametro o lo actualiza si ya existe
    public void insertaDia(DiaDiario dia) throws SQLiteException, SQLiteConstraintException {
        ContentValues values = new ContentValues();
        //Introducimos los valores del nuevo día. Para ello se indica en cada put el nombre
        // de la columna,dado por la clase DiaDiarioEntry y el valor que tenga
        values.put(DiaDiarioEntries.FECHA, DiarioDB.fechaToFechaDB(dia.getFecha()));
        values.put(DiaDiarioEntries.VALORACION, dia.getValoracionDia());
        values.put(DiaDiarioEntries.RESUMEN, dia.getResumen());
        values.put(DiaDiarioEntries.CONTENIDO, dia.getContenido());

        //Miramos si estos valores estan vacios,ya que son opcionales y pueden estarlo
        //En caso de no estar vacios los añadimos,en caso contrario no
        if (dia.getFotoUri() != null && !dia.getFotoUri().isEmpty()) {
            values.put(DiaDiarioEntries.FOTO_URI, dia.getFotoUri());
        }
        if (dia.getLatitud() != null && !dia.getLatitud().isEmpty()) {
            values.put(DiaDiarioEntries.LATITUD, dia.getLatitud());
        }
        if (dia.getLongitud() != null && !dia.getLongitud().isEmpty()) {
            values.put(DiaDiarioEntries.LONGITUD, dia.getLongitud());
        }
        //Si queremos que salte la excepción en caso de problemas en la insercción
        //tenemos que utilizar insertOrThrow, en otro caso podemos utilizar insert
        try {
            db.insertOrThrow(DiaDiarioEntries.TABLE_NAME, null, values);
        } catch (SQLiteException sql) {
            //restriccion para la instruccion sql update
            String where = DiaDiarioEntries.FECHA + "=?";
            ////Obtenemos el valor de la fecha de la tupla que queremos insertar/modificar
            String[] arg = new String[]{DiarioDB.fechaToFechaDB(dia.getFecha())};
            //actualizamos si ya existe en la base de datos
            db.update(DiaDiarioEntries.TABLE_NAME, values, where, arg);
        }
    }

    //Borra una tupla de la base de datos
    public void borraDia(DiaDiario dia) throws SQLiteException, SQLiteConstraintException {
        //Obtenemos el valor de la fecha de la tupla que queremos borrar
        String[] args = {DiarioDB.fechaToFechaDB(dia.getFecha())};
        //Ponemos la restriccion para la instruccion sql
        String where = DiaDiarioEntries.FECHA + " = ?";
        //Ejecutamos el método para la eliminacion de la tupla
        db.delete(DiaDiarioEntries.TABLE_NAME, where, args);
    }

    /**
     * Método que devuelve un cursor con todas las tuplas de la base de datos ordenadas por el parametro pasado
     */
    public Cursor obtenDiario(String ordenadoPor) throws SQLiteException {

        return db.query(DiaDiarioEntries.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ordenadoPor + " DESC");
    }


    /**
     * Metodo que inserta en la base de datos los siguientes datos
     */
    public void cargaDatosPruebaDesdeBaseDatos() {
        DiaDiario[] dias = {new DiaDiario(new Date("11/02/2002"),
                5, "Examen de Lenguaje de Marcas",
                "Los temas que entran son HTML y CSS, deberas hacer una página" +
                        " web con la estructura típica, y contestar veinte preguntas de " +
                        "tipo test en 30 minutos"),
                new DiaDiario(new Date("03/28/2018"),
                        10, "Cumpleaños de Manu",
                        "Fiesta de cumpleaños en Chikipark" +
                                " traer tortada del Zipi-Zape y comprar regalos en Amazon " +
                                "lo pasaremos bien")};
        for (DiaDiario d : dias) {
            this.insertaDia(d);
        }
    }

    /**
     * Método que devuelve la media de las valoraciones de los DiaDiario que hay en la base de datos
     */
    public int valoraVida() {
        //Creamos una variable para almacenar la media y la inicializamos en 0
        int media = 0;
        //Creamos una variable para ponerla como sobrenombre de la columna que nos devuelva y poder obtener con facilidad el valor devuelto
        String sobrenombre = "media";
        //Ejecutamos la instrucción sql y almacenamos el resultado en un cursor
        Cursor c = db.rawQuery("select avg(" + DiaDiarioEntries.VALORACION + ") as " + sobrenombre + " from " + DiaDiarioEntries.TABLE_NAME, null);
        //Miramos si el cursor tiene un resultado
        if (c.moveToFirst()) {
            //Establecemos el valor de la variable como el resultado obtenido
            media = c.getInt(c.getColumnIndex(sobrenombre));
        }
        //Cerramos el cursor
        c.close();

        //Devolvemos el valor
        return media;

    }

}
