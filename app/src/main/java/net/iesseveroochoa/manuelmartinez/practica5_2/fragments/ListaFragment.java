package net.iesseveroochoa.manuelmartinez.practica5_2.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
        //Hacemos a los datos que se quieran guardar sean miembros de la clase por si hay giros de pantalla
        setRetainInstance(true);

        lvListaFragment = getView().findViewById(R.id.lvListaFragment);
        //abrimos la base de datos
        iniciaBaseDatos();

        //si no venimos de una reconstrucción
        if (lvListaFragment == null) {
            cargaDatosPrueba();
        }
        //mostramos los dias ordenador por fecha
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

    /**
     * Metodo para Abrir la BAse de datos, tb podemos cargar datos de muestra aunque no lo haremos aquí
     */

    public void iniciaBaseDatos() {
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
        dDBadapter.agregaDiaBD(dia);
    }

    /**
     * Nos permite eliminar un dia
     *
     * @param pos
     */
    public void delDia(int pos) {
        dDBadapter.borraDiaBD(pos);
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
        dDBadapter = new DiarioDBAdapter(getContext(), db.obtenDiario(ordenActualDias));
        lvListaFragment.setAdapter(dDBadapter);
    }

    /**
     * Nos permite ordenar y mostrar el adaptador
     */
    public void ordenaPor(String orden) {
        ordenActualDias = orden;
        dDBadapter = new DiarioDBAdapter(getContext(), db.obtenDiario(ordenActualDias));
        lvListaFragment.setAdapter(dDBadapter);
    }

    /**
     * Nos permite ordenar y mostrar por fecha el adaptador
     */
    public void dialogoOrdenarPor() {

        //array de elementos
        final CharSequence[] itemsDialogo = getResources().getStringArray(R.array.item_menu);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle(getResources().getString(R.string.ordenarPor));
        dialogo.setItems(itemsDialogo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        ordenaPor(DiarioContract.DiaDiarioEntries.FECHA);
                        Toast.makeText(getContext(), getResources().getString(R.string.ordenarPorFecha),
                                Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        ordenaPor(DiarioContract.DiaDiarioEntries.VALORACION);
                        Toast.makeText(getContext(), getResources().getString(R.string.ordenarPorValoracion),
                                Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        ordenaPor(DiarioContract.DiaDiarioEntries.RESUMEN);
                        Toast.makeText(getContext(), getResources().getString(R.string.ordenarPorResumen),
                                Toast.LENGTH_LONG).show();
                        break;
                }
                dialog.dismiss();
            }
        }).show();

    }

    /**
     * Método que genera un dialogo el cual muestra la media de la puntuacion de los días que hay en la base de datos
     */

    public void valorarVidaDialog() {
        //Creamos un mensaje de alerta para informar al usuario
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        //Establecemos el título y el mensaje que queremos
        dialogo.setTitle(getResources().getString(R.string.TituloValorarVida));
        dialogo.setMessage(getResources().getString(R.string.mensajeValoraVida) + " " + db.valoraVida());
        // agregamos botón de aceptar al dialogo
        dialogo.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Cuando hagan click en el boton saldremos automaticamente,de la misma forma que si pulsa fuera del cuadro de dialogo
                getExitTransition();
            }
        });
        //Mostramos el dialogo
        dialogo.show();
    }


}
