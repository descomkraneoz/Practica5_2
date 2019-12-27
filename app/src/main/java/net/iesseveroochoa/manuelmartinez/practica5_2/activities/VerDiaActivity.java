package net.iesseveroochoa.manuelmartinez.practica5_2.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.fragments.DiaFragment;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;


/**
 * Esta Actividad nos permite ver el detalle en pantallas pequeñas
 */
public class VerDiaActivity extends AppCompatActivity {
    public static final String EXTRA_DIA = "net.iesseveroochoa.manuelmartinez.practica5_2.activities.dia";
    private DiaDiario dia;
    private DiaFragment df;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_dia);
        dia = (DiaDiario) getIntent().getParcelableExtra(EXTRA_DIA);
        df = (DiaFragment) getSupportFragmentManager().findFragmentById(R.id.frDia);
        //flecha en el menu para volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Metodo para volver atras al pulsar sobre la flecha del menu
     */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mostramos el dia en pantalla
        df.setDia(dia);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ((MenuItem) menu.findItem((R.id.btBorrar))).setVisible(true);
        ((MenuItem) menu.findItem((R.id.btAcercade))).setVisible(false);
        ((MenuItem) menu.findItem((R.id.btAnyadir))).setVisible(false);
        ((MenuItem) menu.findItem((R.id.btMostrarDesdeHasta))).setVisible(false);
        ((MenuItem) menu.findItem((R.id.btOpciones))).setVisible(false);
        ((MenuItem) menu.findItem((R.id.btOrdenar))).setVisible(false);
        ((MenuItem) menu.findItem((R.id.btValorarVida))).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btBorrar:
                //Llama al metodo para borrar el dia
                dialogoBorrarDia();
                setTitle("Página Borrada");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogoBorrarDia() {
        //Creamos un mensaje de alerta para informar al usuario
        AlertDialog.Builder dialogo = new AlertDialog.Builder(VerDiaActivity.this);
        //Establecemos el título y el mensaje que queremos
        dialogo.setTitle(getResources().getString(R.string.tituloBorrar));
        dialogo.setMessage(getResources().getString(R.string.cuerpoBorrar));
        // agregamos botón de aceptar al dialogo
        dialogo.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent resultado = new Intent();
                //Metemos en el intent los datos
                resultado.putExtra(EXTRA_DIA, new DiaDiario(dia.getFecha(), dia.getValoracionDia(), dia.getResumen(), dia.getContenido()));
                //Establecemos el resultado como bueno y pasamos el intent
                setResult(RESULT_OK, resultado);
                //Terminamos la actividad
                finish();
            }
        });
        //Mostramos el dialogo
        dialogo.show();
    }

}
