package net.iesseveroochoa.manuelmartinez.practica5_2.activities;

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


    }

    @Override
    protected void onResume() {
        super.onResume();
        //mostramos el dia en pantalla
        df.setDia(dia);
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
}
