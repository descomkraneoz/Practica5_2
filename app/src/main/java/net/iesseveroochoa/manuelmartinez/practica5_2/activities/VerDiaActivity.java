package net.iesseveroochoa.manuelmartinez.practica5_2.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.fragments.DiaFragment;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;

public class VerDiaActivity extends AppCompatActivity {
    public static final String EXTRA_DIA = "dia";
    private DiaDiario dia;
    private DiaFragment df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_dia);
        dia = (DiaDiario) getIntent().getParcelableExtra(EXTRA_DIA);
        df = (DiaFragment) getSupportFragmentManager().findFragmentById(R.id.flDiaFragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //mostramos el dia en pantalla
        df.setDia(dia);
    }
}
