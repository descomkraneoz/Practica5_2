package net.iesseveroochoa.manuelmartinez.practica5_2.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.fragments.ListaFragment;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;

public class MainActivity extends AppCompatActivity {

    //Constante para mandar datos de una actividad a otra cuando se edita o crea una nueva entrada al diario
    public final static int REQUEST_OPTION_NUEVA_ENTRADA_DIARIO = 0;
    public final static int REQUEST_OPTION_VER_ENTRADA_DIARIO = 1;


    static public String TAG_ERROR="P5EjemploDB-Error:";
    //Declaracion de los distintos elementos
    Button btAcercade;
    Button btAnyadir;
    Button btOrdenar;
    Button btBorrar;

    ListaFragment listaFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAcercade = findViewById(R.id.btAcercade);
        btAnyadir = findViewById(R.id.btAnyadir);
        btOrdenar = findViewById(R.id.btOrdenar);
        btBorrar = findViewById(R.id.btBorrar);

        //buscamos el fragment que contiene la lista
        listaFragment = (ListaFragment) getSupportFragmentManager().findFragmentById(R.id.frMain);

        //Asignamos el evento de seleccion de correo
        listaFragment.setOnListaDiarioListener(new ListaFragment.OnListaDiarioListener() {
            @Override
            public void onDiarioSeleccionado(DiaDiario dia) {

                //if (esPantallaGrande) {
                //creamos el fragmento de forma dinámica
                //   crearFragment(correo);

                // } else {
                //si es pantalla pequeña, mostramos el correo en
                //la actividad correspondiente
                mostrarDiaPantallaPeque(dia);
                //}

            }
        });
    }

    /**
     * Metodo para pantalla pequeña que muestra un correo seleccionado
     */
    public void mostrarDiaPantallaPeque(DiaDiario dia) {
        //creamos un intent y le pasamos la actividad que llama a la actividad que recibe
        Intent i = new Intent(MainActivity.this, VerDiaActivity.class);
        i.putExtra(VerDiaActivity.EXTRA_DIA, dia);
        //iniciamos el intent y el pasamos una constante para guardar/enviar los datos
        startActivityForResult(i, REQUEST_OPTION_NUEVA_ENTRADA_DIARIO);
    }




    /**
     * @param menu, para poder mostrar el menu de la aplicacion hace falta inflarlo antes tal que asi.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Metodo para crear el mensaje de alerta al pulsar sobre el boton de Acerca de del Menu de la app
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
     * Metodo del boton (+) del menu que llama a la PoblacionActivity
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
                listaFragment.dialogoOrdenarPor();
                break;
            case R.id.btBorrar:
                //Llama al metodo para borrar el primer dia
               // borrarPrimerDia();
                break;
            case R.id.btValorarVida:
                listaFragment.valorarVidaDialog();
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

    private void mostrarMensajeError(Exception e) {
        Log.e(TAG_ERROR,e.getMessage());
        Toast.makeText(this, getString(R.string.errorLeerBD)+": "+e.getMessage(),Toast.LENGTH_LONG).show();
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
                    //Usamos el metodo mostrarDias para enseñar la base de datos con todos los datos
                    listaFragment.dialogoOrdenarPor();
                    break;
                case REQUEST_OPTION_VER_ENTRADA_DIARIO:
                    //recuperamos los datos de la otra actividad y los guardamos en un objeto DiaDiario
                    DiaDiario dia = data.getParcelableExtra(VerDiaActivity.EXTRA_DIA);
                    //Guardamos en la base de datos el objeto recuperado
                    listaFragment.addDia(dia);
                    //Usamos el metodo mostrarDias para enseñar la base de datos con todos los datos
                    listaFragment.dialogoOrdenarPor();
                    break;
            }
        }
    }

    /**


     * Metodo para borrar dias del diario


    private void borrarPrimerDia() {
        //Obtenemos un cursor el cual esta ordenado por el orden que hay actualmente
        Cursor c = db.obtenDiario(ordenActualDias);
        //Nos intentamos mover a la primera posición del cursor
        if (c.moveToFirst()) {
            //Eliminamos la tupla de la base de datos obteniendo la información del cursor
            db.borraDia(DiarioDB.deCursorADia(c));
        }
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.tmMensajeBorrar),
                Toast.LENGTH_LONG).show();
        mostrarDias(DiarioContract.DiaDiarioEntries.FECHA);
    }






    */


    /**
     * Metodo para mostrar en el textview del MainActivity los datos de la base de datos


     private void mostrarDias() {
     //Obtenemos un cursor el cual esta ordenado por el orden que hay por defecto
     Cursor c = db.obtenDiario(ordenActualDias);
     //Almacenamos el dia
     DiaDiario dia;
     //limpiamos el campo de texto
     tvPrincipal.setText("");
     //Nos aseguramos de que existe al menos un registro
     if (c.moveToFirst()) {
     //Recorremos el cursor hasta que no haya más registros
     do {
     dia = DiarioDB.deCursorADia(c);
     //podéis sobrecargar toString en DiaDiario para mostrar los datos
     //tvPrincipal.append(dia.toString() + "\n");
     tvPrincipal.append(dia.mostrarDatosBonitos() + "\n");
     } while (c.moveToNext());
     }
     }


     * Metodo para mostrar en el textview del MainActivity los datos de la base de datos
     * en funcion a un orden que me pasan por parametro


     private void mostrarDias(String ordenadoPor) {
     //Obtenemos un cursor el cual esta ordenado por el orden que me pasan
     Cursor c = db.obtenDiario(ordenadoPor);
     //Almacenamos el dia
     DiaDiario dia;
     //limpiamos el campo de texto
     tvPrincipal.setText("");
     //Nos aseguramos de que existe al menos un registro
     if (c.moveToFirst()) {
     //Recorremos el cursor hasta que no haya más registros
     do {
     dia = DiarioDB.deCursorADia(c);
     //podéis sobrecargar toString en DiaDiario para mostrar los datos
     //tvPrincipal.append(dia.toString() + "\n");
     tvPrincipal.append(dia.mostrarDatosBonitos() + "\n");
     } while (c.moveToNext());
     }
     }

     */

}
