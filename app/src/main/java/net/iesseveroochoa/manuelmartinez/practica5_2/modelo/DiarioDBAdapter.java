package net.iesseveroochoa.manuelmartinez.practica5_2.modelo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.iesseveroochoa.manuelmartinez.practica5_2.R;

public class DiarioDBAdapter extends CursorAdapter {

    protected Cursor c;
    protected DiarioDB db;

    public DiarioDBAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    /**
     * crea un item nuevo
     * @param context
     * @param cursor
     * @param parent
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        return inflater.inflate(R.layout.item_diario,parent,false);
    }

    /**
     * Este método carga los valores en cada item del ListView. El cursor
     *que se pasa como parámetro está en la posición del elemento que se tiene
     * @param view
     * @param context
     * @param cursor
     */

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        DiaDiario dia = DiarioDB.deCursorADia(cursor);

        ImageView ivIconoItemDiario = view.findViewById(R.id.ivIconoItemDiario);
        TextView tvResumenItemDiario = view.findViewById(R.id.tvResumenItemDiario);
        TextView tvFechaItemDiario = view.findViewById(R.id.tvFechaItemDiario);

        //layout contenedor del item_diario
        LinearLayout llItem = (LinearLayout) view.findViewById(R.id.llItemDiario);

        if ((cursor.getPosition() % 2 == 0)) {
            llItem.setBackgroundResource(R.drawable.layaout_redondeado_color);
        } else {
            llItem.setBackgroundResource(R.drawable.layout_redondeado);
        }


        //Asignar valores a las posiciones del cursor

        ivIconoItemDiario.setImageResource(dia.getValoracionResumida());
        tvResumenItemDiario.setText(dia.getResumen());
        tvFechaItemDiario.setText(dia.getFechaFormatoLocal());

    }

    public Cursor getCursor() {
        return c;
    }

    public void setCursor(Cursor cursor) {
        this.c = cursor;
    }

    public DiaDiario diaPosicion(int posicion) {
        c.moveToPosition(posicion);
        return DiarioDB.deCursorADia(c);
    }

    public int idPosicion(int posicion) {
        c.moveToPosition(posicion);
        if (c.getCount() > 0) {
            return c.getInt(0);
        } else {
            return -1;
        }
    }


}
