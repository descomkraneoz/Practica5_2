package net.iesseveroochoa.manuelmartinez.practica5_2.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioContract;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioDB;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioDBAdapter;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {

    DiarioDB db;
    ListView lvListaFragment;
    DiarioDBAdapter dDBadapter;
    //contiene una referencia al listener del evento de seleccion de dia
    private OnListaDiarioListener listaDiariosListener;



    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Hacemos a los datos que se quieran guardar sean miembros de la clase
        setRetainInstance(true);

        lvListaFragment = getView().findViewById(R.id.lvListaFragment);

        //BASE DE DATOS, inicializamos y cargamos datos de prueba
        try {
            //Inicializa la base de datos
            db = new DiarioDB(getContext());
            //abre la base de datos
            db.open();
            //cargamos unos datos de prueba en la base de datos
            //db.cargaDatosPrueba();

        } catch (android.database.sqlite.SQLiteException e) {
            e.printStackTrace();
        }

        //si no venimos de una reconstrucción
        if (lvListaFragment == null) {
            cargaDatosPrueba();
        }
        dDBadapter = new DiarioDBAdapter(getContext(), db.obtenDiario(DiarioContract.DiaDiarioEntries.FECHA));
        lvListaFragment.setAdapter(dDBadapter);

        //en caso de click sobre un correo, delegamos a la actividad que tiene que hacer
        lvListaFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                listaDiariosListener.onDiarioSeleccionado((DiaDiario) dDBadapter.getItem(i));
            }
        });


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
            db.insertaDia(d);
        }
    }

    //Sobreescribimos el metodo onDestroy para que cierre la base de datos cuando se cierre la aplicacion
    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    /**
     * Este método nos permite asignar el listener para el evento de seleccion de dia
     *
     * @param listener
     */
    public void setOnListaDiarioListener(OnListaDiarioListener listener) {
        listaDiariosListener = listener;
    }

    /**
     * Nos permite añadir un dia
     *
     * @param dia
     */
    public void addDia(DiaDiario dia) {
        dDBadapter.addDia(dia);
    }

    /**
     * Nos permite eliminar un dia
     *
     * @param pos
     */
    public void delDia(int pos) {
        dDBadapter.delDia(pos);
    }

    /**
     * Mediante esta interface comunicaremos el evento de selección de un dia
     */
    public interface OnListaDiarioListener {
        void onDiarioSeleccionado(DiaDiario dia);
    }


}
