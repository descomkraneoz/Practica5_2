package net.iesseveroochoa.manuelmartinez.practica5_2.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioContract;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioDB;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {

    DiarioDB db;


    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    /**
     * Método que devuelve un cursor con todas las tuplas de la base de datos ordenadas por el parametro pasado
     */
    public Cursor obtenDiario(String ordenadoPor) throws SQLiteException {

        return db.query(DiarioContract.DiaDiarioEntries.TABLE_NAME,
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
    public void cargaDatosPrueba() {
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

}
