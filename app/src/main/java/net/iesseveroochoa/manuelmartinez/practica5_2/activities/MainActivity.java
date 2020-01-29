package net.iesseveroochoa.manuelmartinez.practica5_2.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.fragments.DiaFragment;
import net.iesseveroochoa.manuelmartinez.practica5_2.fragments.ListaFragment;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioContract;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiarioDB;

public class MainActivity extends AppCompatActivity {

    //Constante para mandar datos de una actividad a otra cuando se edita o crea una nueva entrada al diario
    public final static int REQUEST_OPTION_NUEVA_ENTRADA_DIARIO = 0;
    public final static int REQUEST_OPTION_BORRAR_ENTRADA_DIARIO = 1;

    //Declaracion de los distintos elementos
    Button btAcercade;
    Button btAnyadir;
    Button btOrdenar;
    Button btBorrar;
    //para cuando no haya dia seleccionado en tablet
    TextView tvSinDia;
    //para el dialogo de valorar vida en imagen
    ImageView ivImagen;
    //fragmento detalle de dia
    DiaFragment diaFragmentDinamico;
    //fragmento contenedor para tablet de dia
    FrameLayout frameContenedorDinamico;
    //el fragment que se va a mostrar con la lista de dias
    ListaFragment listaFragment;
    //bandera para saber en que tipo de pantalla estamos
    private boolean esPantallaGrande;
    //nos permite establecer el orden en el que mostraremos la lista
    private String ordenActualDias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAcercade = findViewById(R.id.btAcercade);
        btAnyadir = findViewById(R.id.btAnyadir);
        btOrdenar = findViewById(R.id.btOrdenar);
        btBorrar = findViewById(R.id.btBorrar);
        tvSinDia = findViewById(R.id.tvSindia);

        //Cargar el imagenView del dialogo valorar vida, asignar un valor por defecto----> error null pointer************************************************
        ivImagen = findViewById(R.id.ivImagenVV);
        //ivImagen.setBackgroundResource(R.drawable.neutrop);


        //Orden actual de dias
        ordenActualDias = DiarioContract.DiaDiarioEntries.FECHA;

        //comprobamos si estamos en una pantalla grande mirando si existe el frameLayout que contendrá el fragment
        frameContenedorDinamico = (FrameLayout) findViewById(R.id.frm_contenedorFrgDinamico);

        //buscamos los fragment que contiene la lista y los dias
        listaFragment = (ListaFragment) getSupportFragmentManager().findFragmentById(R.id.frMain);
        diaFragmentDinamico = (DiaFragment) getSupportFragmentManager().findFragmentById(R.id.frDia);

        if (frameContenedorDinamico != null) {
            esPantallaGrande = true;

        } else {
            esPantallaGrande = false;

        }

        //Asignamos el evento de seleccion de un dia
        listaFragment.setOnListaDiarioListener(new ListaFragment.OnListaDiarioListener() {
            @Override
            public void onDiarioSeleccionado(DiaDiario dia) {

                if (!esPantallaGrande) {
                    //si es pantalla pequeña, mostramos el dia en la actividad correspondiente
                    borrarDiaPantallaPeque(dia);

                } else {
                    //creamos el fragmento de forma dinámica
                    crearFragment(dia);
                    recuperarUltimoDia();

                }
            }
        });

    }

    /**
     * Guardamos y Recuperamos el dia en las preferencias para que se muestre en pantalla grande al salir de la app
     */

    public void guardarUltimoDia() {
        if (esPantallaGrande) {
            DiaDiario dia = new DiaDiario();
            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(dia);
            prefsEditor.putString("UltimoDia", json);
            prefsEditor.commit();
        }

    }

    public void recuperarUltimoDia() {
        if (esPantallaGrande) {
            SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
            Gson gson = new Gson();
            String json = mPrefs.getString("UltimoDia", "");
            DiaDiario obj = gson.fromJson(json, DiaDiario.class);
        }
    }

    @Override
    protected void onPause() {
        guardarUltimoDia();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        guardarUltimoDia();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarUltimoDia();
    }

    /**
     * Metodo para controlar cuando el usuario pulsa el boton de back
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (esPantallaGrande) {
            //buscamos el fragment anterior.
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() == 0) {
                //si no hay dia que mostrar en la pila, mostramos el campo de texto y no permitimos borrar
                tvSinDia.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Crea un nuevo fragment detalle dia permitiendo la navegabilidad.
     *
     * @param dia
     */
    private void crearFragment(DiaDiario dia) {
        //creamos un nuevo fragment enviandole el dia
        diaFragmentDinamico = DiaFragment.newInstance(dia);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frm_contenedorFrgDinamico, diaFragmentDinamico);
        //permitimos la navegabilidad
        transaction.addToBackStack(null);
        //activamos el fragment nuevo
        transaction.commit();
        tvSinDia.setVisibility(View.INVISIBLE);//no mostrar el textview

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            tvSinDia.setVisibility(View.VISIBLE);//mostrar el textview
        }
    }

    /**
     * Metodo para pantalla pequeña que borrara un correo seleccionado, sustituye al de mostrar dia
     */
    public void borrarDiaPantallaPeque(DiaDiario dia) {
        //creamos un intent y le pasamos la actividad que llama a la actividad que recibe
        Intent i = new Intent(MainActivity.this, VerDiaActivity.class);
        i.putExtra(VerDiaActivity.EXTRA_DIA, dia);
        //iniciamos el intent y le pasamos una constante para guardar/enviar los datos
        startActivityForResult(i, REQUEST_OPTION_BORRAR_ENTRADA_DIARIO);
    }

    /**
     * @param menu, para poder mostrar el menu de la aplicacion hace falta inflarlo antes tal que asi.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!esPantallaGrande) {//si es pantalla pequeña ocultamos menu borrar
            ((MenuItem) menu.findItem((R.id.btBorrar))).setVisible(false);
        }
        return true;
    }

    /**
     * Metodo para crear el mensaje de alerta al pulsar sobre el boton de Acerca de... del Menu de la app
     */

    public void MensajeAcercade() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        // titulo y mensaje
        dialogo.setTitle(getResources().getString(R.string.acercadeTitulo));
        dialogo.setMessage(getResources().getString(R.string.acercadeMensaje));

        // agregamos botón Ok y su evento
        dialogo.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Qué hacemos en caso ok
                        onRestart();
                    }
                });
        dialogo.show();
    }

    /**
     * Metodo del boton (+) del menu que llama a la EdicionDiaActivity
     */

    public void agregaDiaDiario() {
        //creamos un intent y le pasamos la actividad que llama a la actividad que recibe
        Intent intent = new Intent(MainActivity.this, EdicionDiaActivity.class);
        //iniciamos el intent y el pasamos una constante para guardar/enviar los datos
        startActivityForResult(intent, REQUEST_OPTION_NUEVA_ENTRADA_DIARIO);
    }

    /**
     * Llamada a los botones o elementos del menu de la app
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btAcercade:
                //Muestra el dialogo Acerca de
                MensajeAcercade();
                break;
            case (R.id.btAnyadir):
                //Llama al metodo para agregar un nuevo dia en EdicionDiaActivity
                agregaDiaDiario();
                break;
            case R.id.btOrdenar:
                //Llama al metodo para ordenar según unos parámetros
                dialogoOrdenarPor();
                break;
            case R.id.btBorrar:
                //Llama al metodo para borrar el primer dia
                borrarPrimerDia();
                break;
            case R.id.btValorarVida:
                valorarVidaDialogImagen();
                //valorarVidaDialog();
                break;
            case R.id.btMostrarDesdeHasta:
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.tmMensajeERROR), Toast.LENGTH_LONG).show();
                break;
            case R.id.btOpciones:
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.tmMensajeERROR), Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Método que se ejecuta cuando se vuelve de una actividad iniciada con startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Miramos que el resultado devuelto sea correcto y que el código de solicitud sea el de añadir una nueva actividad
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_OPTION_NUEVA_ENTRADA_DIARIO:
                    //recuperamos los datos de la otra actividad y los guardamos en un objeto DiaDiario
                    DiaDiario p = data.getParcelableExtra(EdicionDiaActivity.EXTRA_DIA_A_GUARDAR);
                    //Guardamos en la base de datos el objeto recuperado
                    listaFragment.addDia(p);
                    break;
                case REQUEST_OPTION_BORRAR_ENTRADA_DIARIO:
                    //recuperamos los datos de la otra actividad y los guardamos en un objeto DiaDiario
                    DiaDiario diaBorrar = data.getParcelableExtra(VerDiaActivity.EXTRA_DIA);
                    //Borramos en la base de datos el objeto recuperado
                    listaFragment.delDia(diaBorrar);
                    break;
            }
        }
    }


    /**
     * Dialogo que nos permite ordenar y mostrar por fecha, valoración o resumen
     */
    public void dialogoOrdenarPor() {

        //array de elementos
        final CharSequence[] itemsDialogo = getResources().getStringArray(R.array.item_menu);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle(getResources().getString(R.string.ordenarPor));
        dialogo.setItems(itemsDialogo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        listaFragment.ordenaPor(DiarioContract.DiaDiarioEntries.FECHA);
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.ordenarPorFecha),
                                Toast.LENGTH_LONG).show();
                        listaFragment.leeAdaptador();
                        break;
                    case 1:
                        listaFragment.ordenaPor(DiarioContract.DiaDiarioEntries.VALORACION);
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.ordenarPorValoracion),
                                Toast.LENGTH_LONG).show();
                        listaFragment.leeAdaptador();
                        break;
                    case 2:
                        listaFragment.ordenaPor(DiarioContract.DiaDiarioEntries.RESUMEN);
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.ordenarPorResumen),
                                Toast.LENGTH_LONG).show();
                        listaFragment.leeAdaptador();
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
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        //Establecemos el título y el mensaje que queremos
        dialogo.setTitle(getResources().getString(R.string.TituloValorarVida));
        dialogo.setMessage(getResources().getString(R.string.mensajeValoraVida) + " " + listaFragment.valorarVidaListaFragment());
        // agregamos botón de aceptar al dialogo
        dialogo.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Cuando hagan click en el boton saldremos automaticamente,de la misma forma que si pulsa fuera del cuadro de dialogo
                onRestart();
            }
        });
        //Mostramos el dialogo
        dialogo.show();
    }

    /**
     * Método que genera un dialogo el cual muestra una imagen segun la media de la puntuacion de los días que hay en la base de datos
     */

    public void valorarVidaDialogImagen() {
        //inflamos el layout que contendra la imagen para valorar vida y su vista
        LayoutInflater layoutImagenValorarVida = LayoutInflater.from(MainActivity.this);
        final View vistaValoraVida = layoutImagenValorarVida.inflate(R.layout.imagenvv, null);

        //ivImagen.setBackgroundResource(R.drawable.sadp);
        //ivImagen.setImageDrawable(getResources().getDrawable(R.drawable.sadp));
        //ivImagen.setImageResource(R.drawable.sadp);

        int valor = listaFragment.valorarVidaListaFragment();


        //Creamos un mensaje de alerta para informar al usuario
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        //Establecemos el título y el mensaje que queremos
        if (valor < 5) {
            String uri = "@drawable/sadp";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable imagen = ContextCompat.getDrawable(getApplicationContext(), imageResource);
            ivImagen.setImageDrawable(imagen);
            //ivImagen.setBackgroundResource(R.drawable.sadp);
            //ivImagen.setImageDrawable(getResources().getDrawable(R.drawable.sadp));
            //ivImagen.setImageResource(R.drawable.sadp);
        } else if (valor >= 5 && valor <= 8) {
            String uri = "@drawable/neutrop";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable imagen = ContextCompat.getDrawable(getApplicationContext(), imageResource);
            ivImagen.setImageDrawable(imagen);
            ((ImageView) findViewById(R.id.ivImagenVV)).setImageDrawable(imagen);
            //ivImagen.setBackgroundResource(R.drawable.neutrop);
            //ivImagen.setImageResource(R.drawable.neutrop);
            //ivImagen.setImageDrawable(getResources().getDrawable(R.drawable.neutrop));
        } else {
            String uri = "@drawable/smilep";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable imagen = ContextCompat.getDrawable(getApplicationContext(), imageResource);
            ivImagen.setImageDrawable(imagen);
            //ivImagen.setBackgroundResource(R.drawable.smilep);
            //ivImagen.setImageDrawable(getResources().getDrawable(R.drawable.smilep));
            //ivImagen.setImageResource(R.drawable.smilep);
        }
        //cargamos la vista del dialogo
        dialogo.setView(vistaValoraVida);

        // agregamos botón de aceptar al dialogo
        dialogo.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Cuando hagan click en el boton saldremos automaticamente,de la misma forma que si pulsa fuera del cuadro de dialogo
                onRestart();
            }
        });
        //Mostramos el dialogo
        dialogo.show();
    }


    /**
     * Metodo para borrar dias del diario en pantalla tablet.
     */

    private void borrarPrimerDia() {
        //Creamos un mensaje de alerta para informar al usuario
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        //Establecemos el título y el mensaje que queremos
        dialogo.setTitle(getResources().getString(R.string.tituloBorrar));
        dialogo.setMessage(getResources().getString(R.string.cuerpoBorrar));
        // agregamos botón de aceptar al dialogo
        dialogo.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Obtenemos un cursor el cual esta ordenado por el orden que hay actualmente
                Cursor c = listaFragment.obtenerDiario(ordenActualDias);
                //Nos intentamos mover a la primera posición del cursor
                if (c.moveToFirst()) {
                    //Eliminamos la tupla de la base de datos obteniendo la información del cursor
                    listaFragment.delDia(DiarioDB.deCursorADia(c));
                }
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    tvSinDia.setVisibility(View.VISIBLE);//mostramos textview que muestra "MI DIARIO"
                }
                listaFragment.ordenaPor(ordenActualDias);
                listaFragment.leeAdaptador();
                onRestart();
            }
        });
        //Mostramos el dialogo
        dialogo.show();
    }

}
