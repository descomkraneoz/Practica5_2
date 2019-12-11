package net.iesseveroochoa.manuelmartinez.practica5_2.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DiaDiario implements Parcelable {
    private Date fecha;
    private int valoracionDia;
    private String resumen;
    private String contenido;
    private String fotoUri;
    private String latitud;
    private String longitud;


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getValoracionDia() {
        return valoracionDia;
    }

    public void setValoracionDia(int valoracionDia) {
        this.valoracionDia = valoracionDia;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public DiaDiario(Date fecha, int valoracionDia, String resumen, String contenido) {
        this.fecha = fecha;
        if(valoracionDia>=0 || valoracionDia<=10){
            this.valoracionDia = valoracionDia;
        }else{
            this.valoracionDia = 5;
        }
        this.resumen = resumen;
        this.contenido = contenido;
    }

    public DiaDiario(Date fecha, int valoracionDia, String resumen, String contenido, String fotoUri, String latitud, String longitud) {
        this.fecha = fecha;
        if(valoracionDia>=0 || valoracionDia<=10){
            this.valoracionDia = valoracionDia;
        }else{
            this.valoracionDia = 5;
        }
        this.resumen = resumen;
        this.contenido = contenido;
        this.fotoUri = fotoUri;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getValoracionResumida(){
        if (valoracionDia<5){
            return 1;
        }else if(valoracionDia>=5 && valoracionDia<=8){
            return 2;
        }else {
            return 3;
        }
    }

    @Override
    public String toString() {
        return "DiaDiario{" +
                "fecha=" + DiarioDB.fechaToFechaDB(fecha) +
                ", valoracionDia=" + getValoracionDia() +
                ", resumen='" + resumen + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fotoUri='" + fotoUri + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                '}';
    }

    protected DiaDiario(Parcel in) {
        //fecha = new SimpleDateFormat("dd/MM/yyyy").format(fecha).toString();
        fecha = DiarioDB.fechaBDtoFecha(in.readString());
        valoracionDia = in.readInt();
        resumen = in.readString();
        contenido = in.readString();
        fotoUri = in.readString();
        latitud = in.readString();
        longitud = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaDiario diaDiario = (DiaDiario) o;
        return DiarioDB.fechaToFechaDB(fecha).equals(diaDiario.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha);
    }

    /**
     * Metodo para mostrar los datos formateados
     */

    public String mostrarDatosBonitos() {
        return new SimpleDateFormat("dd/MM/yyyy").format(fecha) + "\n" +
                "VALORACIÓN DEL DÍA: " + getValoracionDia() + "\n" +
                "BREVE RESUMEN: " + resumen + "\n" +
                "CONTENIDO: " + contenido;
    }

    /**
     * Hacemos los metodos parcelable
     */


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(DiarioDB.fechaToFechaDB(fecha));
        parcel.writeInt(valoracionDia);
        parcel.writeString(resumen);
        parcel.writeString(contenido);
        parcel.writeString(fotoUri);
        parcel.writeString(latitud);
        parcel.writeString(longitud);
    }

    public static final Creator<DiaDiario> CREATOR = new Creator<DiaDiario>() {
        @Override
        public DiaDiario createFromParcel(Parcel in) {
            return new DiaDiario(in);
        }

        @Override
        public DiaDiario[] newArray(int size) {
            return new DiaDiario[size];
        }
    };
}
