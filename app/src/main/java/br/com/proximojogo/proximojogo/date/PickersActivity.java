package br.com.proximojogo.proximojogo.date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ale on 31/10/2017.
 */

public class PickersActivity implements View.OnFocusChangeListener, View.OnClickListener {

    private static final String ZERO = "0";
    private static final String BARRA = "/";
    private static final String DOIS_PONTOS = ":";
    //Calendario para obter data e hora
    public final Calendar c = Calendar.getInstance();
    //Variaveis para obter a data
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int ano = c.get(Calendar.YEAR);
    //variaveis Hora
    //Hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    //Widgets
    private EditText edExterno;
    private EditText edHora;
    private int tipo;

    //contexto
    private Context ctx;
    private Calendar myCalendar;

    public PickersActivity(EditText edExterno, Context ctx, int tipo) {
        this.edExterno = edExterno;
        this.edExterno.setOnFocusChangeListener(this);
        this.ctx = ctx;
        this.tipo = tipo;
        myCalendar = Calendar.getInstance();
    }

    private void pegarData() {
        DatePickerDialog pegarData = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdformat = new SimpleDateFormat(myFormat);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                edExterno.setText(sdformat.format(myCalendar.getTime()));

//                final int mesActual = month + 1;
//
//                String diaFormateado = (dayOfMonth < 10)? ZERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
//                String mesFormateado = (mesActual < 10)? ZERO + String.valueOf(mesActual):String.valueOf(mesActual);
//
//                edExterno.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
        }, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        pegarData.getDatePicker().setMinDate(new Date().getTime());
        pegarData.show();

    }

    private void pegarHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String format = ":";
                if (minute < 10) {
                    format = ":0";
                }

                edExterno.setText(hourOfDay + format + minute);

//                String horaFormateada =  (hourOfDay < 9)? String.valueOf(ZERO + hourOfDay) : String.valueOf(hourOfDay);
//                String minutoFormateado = (minute < 9)? String.valueOf(ZERO + minute):String.valueOf(minute);
//
//                String AM_PM;
//                if(hourOfDay < 12) {
//                    AM_PM = "a.m.";
//                } else {
//                    AM_PM = "p.m.";
//                }
//
//                edHora.setText(horaFormateada + DOIS_PONTOS + minutoFormateado + " " + AM_PM);
            }

        }, hora, minuto, true);

        recogerHora.show();
    }

    @Override
    public void onClick(View v) {
        switch (tipo) {
            case 0:
                pegarData();
                break;
            case 1:
                pegarHora();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            switch (tipo) {
                case 0:
                    pegarData();
                    break;
                case 1:
                    pegarHora();
                    break;
            }
        }
    }
}
