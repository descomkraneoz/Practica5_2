package net.iesseveroochoa.manuelmartinez.practica5_2.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaFragment extends Fragment {
    private static final String ARG_DIA = "net.iesseveroochoa.manuelmartinez.practica5_2.fragments.dia";
    TextView tvFechaDiaFragment;
    TextView tvResumenDiaFragment;
    TextView tvValoracionDiaFragment;
    TextView tvNumeroDiaFragment;
    TextView tvTextoDiaFragment;
    private DiaDiario diaDiario;


    public DiaFragment() {
        // Required empty public constructor
    }

    public static DiaFragment newInstance(DiaDiario dia) {
        DiaFragment fragment = new DiaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DIA, dia);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dia, container, false);
        //indicamos que retenga los valores ante reconstrucciones
        //esto es una ventaja importante respecto a las actividades
        this.setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvFechaDiaFragment = getView().findViewById(R.id.tvFechaDiaFragment);
        tvResumenDiaFragment = getView().findViewById(R.id.tvResumenDiaFragment);
        tvValoracionDiaFragment = getView().findViewById(R.id.tvValoracionDiaFragment);
        tvNumeroDiaFragment = getView().findViewById(R.id.tvNumeroDiaFragment);
        tvTextoDiaFragment = getView().findViewById(R.id.tvTextoDiaFragment);

        if (getArguments() != null) {
            //si estamos creando el fragment de forma dinámica y tenemos
            //argumentos lo mostramos
            diaDiario = getArguments().getParcelable(ARG_DIA);
        } else {
            //si estamos creandolo de forma estática mostramos datos temporales
            diaDiario = new DiaDiario(new Date("25/12/2019"), 9, "Navidad", "A cantar villancicos...arre burro arre...");
        }
        visualizaDia();
    }

    private void visualizaDia() {
        tvFechaDiaFragment.setText(diaDiario.getFechaFormatoLocal());
        tvResumenDiaFragment.setText(diaDiario.getResumen());
        tvValoracionDiaFragment.setText(getResources().getString(R.string.valoracion));
        tvNumeroDiaFragment.setText(Integer.toString(diaDiario.getValoracionDia()));
        tvTextoDiaFragment.setText(diaDiario.getContenido());
    }

    public void setDia(DiaDiario dia) {
        diaDiario = dia;
        visualizaDia();
    }

}
