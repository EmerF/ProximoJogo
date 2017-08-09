package br.com.proximojogo.proximojogo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by ale on 08/08/2017.
 */

public class FormatarData {
    static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    static SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
    static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    static DateFormat dfHora = new SimpleDateFormat("HH:mm");



    public static SimpleDateFormat getFormato() {
        return formato;
    }
    public static SimpleDateFormat getFormatoHora() {
        return formatoHora;
    }


    public static DateFormat getDf() {
        return df;
    }
    public static DateFormat getDfHora() {
        return dfHora;
    }


    public static void setDf(DateFormat df) {
        FormatarData.df = df;
    }
}
