package net.iesseveroochoa.manuelmartinez.practica5_2.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;
import net.iesseveroochoa.manuelmartinez.practica5_2.fragments.DatePickerFragment;
import net.iesseveroochoa.manuelmartinez.practica5_2.modelo.DiaDiario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EdicionDiaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_DIA_A_GUARDAR = "net.iessochoa.manuelmartinez.practica5.activities.dia_guardar";

    //Constante que indica la posicion por defecto del spinner de valoracion
    private final int VALORACION_POR_DEFECTO = 5;


    EditText etFecha;
    Button btFecha;
    EditText etResumen;
    Spinner spValorarVida;
    EditText etContenido;
    FloatingActionButton fabGuardar;

    /**
     * Método que genera un dialogo el cual indica que hay algunos campos incompletos y se deben rellenar
     */
    private void dialogoCamposIncompletos() {
        AlertDialog.Builder dlgAcercaDe = new AlertDialog.Builder(this);
        //Establecemos el título y el mensaje que queremos
        dlgAcercaDe.setTitle(getResources().getString(R.string.tituloError));
        dlgAcercaDe.setMessage(getResources().getString(R.string.mensajeErrorCamposIncompletos));
        // agregamos botón de aceptar al dialogo
        dlgAcercaDe.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Cuando hagan click en el boton saldremos automaticamente,de la misma forma que si pulsa fuera del cuadro de dialogo
                onBackPressed();
            }
        });
        //Mostramos el dialogo
        dlgAcercaDe.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_dia);
        etFecha = findViewById(R.id.etFecha);
        btFecha = findViewById(R.id.btFecha);
        etResumen = findViewById(R.id.etResumenBreve);
        spValorarVida = findViewById(R.id.spValorarVida);
        etContenido = findViewById(R.id.etResumenGeneral);
        fabGuardar = findViewById(R.id.fabGuardar);
        this.setTitle(getResources().getText(R.string.TituloEDA));
        //flecha en el menu para volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Por defecto mostraremos como día la fecha de hoy,por lo que la mostramos en el textView puesto para ello
        etFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        //Establecemos el adaptador del spinner de las valoraciones a un adaptador el cual obtiene los datos del archivo de recursos de array
        spValorarVida.setAdapter(ArrayAdapter.createFromResource(this, R.array.valores, android.R.layout.simple_spinner_dropdown_item));
        //Establecemos la posición por defecto del spinner por la que viene dada en la constante
        spValorarVida.setSelection(VALORACION_POR_DEFECTO);

        //Le establecemos un click al boton fecha para que habra el Dialogo de fecha
        btFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), getResources().getString(R.string.fecha));
            }
        });

        //Metodo del boton guardar, para guardar y mandar la nueva entrada del diario al MainActivity
        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Quitamos los espacios de los EditText del resumen y el contenido
                etResumen.setText(etResumen.getText().toString().trim());
                etContenido.setText(etContenido.getText().toString().trim());
                //Miramos si estos EditText estan vacios,en cuyo caso mostraremos un dialgo indicando que los deben rellenar
                if ((getResources().getString(R.string.vacio).equals(etResumen.getText().toString())) ||
                        (getResources().getString(R.string.vacio).equals(etResumen.getText().toString()))) {
                    //Llamamos al metodo dialogoCamposIncompletos el cual genera el dialogo
                    dialogoCamposIncompletos();
                    //En caso de que ambos campos esten completos entramos en este else
                } else {
                    //Creamos un intent
                    Intent resultado = new Intent();
                    //Obtenemos la fecha del textView y le ponemos el formato que queremos
                    Date fecha = null;
                    try {
                        fecha = new SimpleDateFormat("dd/MM/yyyy").parse(etFecha.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Metemos en el intent un nuevo DiaDiario con los datos que hay en los componentes
                    resultado.putExtra(EXTRA_DIA_A_GUARDAR, new DiaDiario(fecha, Integer.parseInt((String) spValorarVida.getSelectedItem()),
                            etResumen.getText().toString(), etContenido.getText().toString()));
                    //Establecemos el resultado como bueno y pasamos el intent
                    setResult(RESULT_OK, resultado);
                    //Terminamos la actividad
                    finish();
                }
            }

        });

    }

    /**
     * Metodo para volver atras al pulsar sobre la flecha del menu
     */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    /**
     * Metodo para obtener una fecha del dialogo datepicker
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        EdicionDiaActivity.this.etFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(c.getTime()));
    }
}
