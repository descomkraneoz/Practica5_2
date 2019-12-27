package net.iesseveroochoa.manuelmartinez.practica5_2.fragments;


import android.database.Cursor;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {

    private DiarioDB db;
    private ListView lvListaFragment;
    private DiarioDBAdapter dDBadapter;
    //contiene una referencia al listener del evento de seleccion de dia
    private OnListaDiarioListener listaDiariosListener;
    //nos permite conocer el orden en el que tenemos la lista
    private String ordenActualDias;



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

        ////////////////////////////////////////////////////////////////////////////////////////////
        //listview
        lvListaFragment = getView().findViewById(R.id.lvListaFragment);
        //ordenar lista
        ordenActualDias = DiarioContract.DiaDiarioEntries.FECHA;
        //inciamos Base de datos, adaptador y lista
        db = new DiarioDB(getContext());
        db.open();
        if (savedInstanceState == null) {
            db.cargaDatosPruebaDesdeBaseDatos();
        }
        Cursor cursor = db.obtenDiario(ordenActualDias);
        dDBadapter = new DiarioDBAdapter(getContext(), cursor);
        lvListaFragment.setAdapter(dDBadapter);
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Hacemos a los datos que se quieran guardar sean miembros de la clase por si hay giros de pantalla
        setRetainInstance(true);


        //si no venimos de una reconstrucción
        if (lvListaFragment == null) {
            db.cargaDatosPruebaDesdeBaseDatos();
        }
        //mostramos los dias ordenados por fecha
        ordenaPorFecha();


        //en caso de click sobre un dia, delegamos a la actividad que tiene que hacer
        lvListaFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listaDiariosListener != null) {
                    //pasar dia seleccionado
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                    DiaDiario dia = DiarioDB.deCursorADia(cursor);
                    //llamamos al metodo implementado en la actividad
                    listaDiariosListener.onDiarioSeleccionado(dia);

                }
            }
        });
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
        db.insertaDia(dia);
        leeAdaptador();
    }

    /**
     * Nos permite eliminar un dia
     *
     * @param dia
     */
    public void delDia(DiaDiario dia) {
        db.borraDia(dia);
        leeAdaptador();
    }

    /**
     * Actualiza el adaptador
     */
    public void leeAdaptador() {
        Cursor crs = db.obtenDiario(ordenActualDias);
        dDBadapter.changeCursor(crs);
        dDBadapter.notifyDataSetChanged();
    }

    /**
     * Mediante esta interface comunicaremos el evento de selección de un dia
     */
    public interface OnListaDiarioListener {
        void onDiarioSeleccionado(DiaDiario dia);
    }

    /**
     * Nos permite ordenar y mostrar por fecha el adaptador
     */
    public void ordenaPorFecha() {
        ordenActualDias = DiarioContract.DiaDiarioEntries.FECHA;
        leeAdaptador();
    }


    /**
     * Nos permite ordenar y mostrar el adaptador
     */

    public void ordenaPor(String orden) {
        ordenActualDias = orden;
        Cursor crs = db.obtenDiario(orden);
        dDBadapter.changeCursor(crs);
        lvListaFragment.setAdapter(dDBadapter);
        dDBadapter.notifyDataSetChanged();
    }


    /**
     * Nos permite saber la media de valoración de los dias del diario
     */

    public int valorarVidaListaFragment() {
        return db.valoraVida();
    }


    /**
     * Metodo que obtiene un cursor
     */
    public Cursor obtenerDiario(String ordenDeseado) {
        return db.obtenDiario(ordenDeseado);
    }


}
