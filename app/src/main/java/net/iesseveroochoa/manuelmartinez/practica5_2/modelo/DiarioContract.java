package net.iesseveroochoa.manuelmartinez.practica5_2.modelo;

import android.provider.BaseColumns;

public class DiarioContract {
    public static class DiaDiarioEntries {
        public static final String TABLE_NAME = "diadiario"; //nombre de la base de datos

        //Nombre de las columnas
        public static final String ID = BaseColumns._ID;//esta columna es necesaria para Android
        public static final String FECHA = "fecha";
        public static final String VALORACION = "valoracionDia";
        public static final String RESUMEN = "resumen";
        public static final String CONTENIDO = "contenido";
        public static final String FOTO_URI = "fotoUri";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
    }
}
