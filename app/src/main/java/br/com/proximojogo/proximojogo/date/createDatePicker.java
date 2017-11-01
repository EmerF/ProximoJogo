package br.com.proximojogo.proximojogo.date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by emerson on 03/12/16.
 */

/**
 * Pia deprecatei ela agora usar {@link PickersActivity}
 */
@Deprecated
public class createDatePicker implements View.OnFocusChangeListener, View.OnClickListener, DatePickerDialog.OnDateSetListener {
    /* Cria um datepicker a partir do click em um edit text e preenche com data após clicar em ok

     */

    private EditText editText;
    private Calendar myCalendar;
    private Context ctx;
    private String title;
    private DatePickerDialog datePickerDialog;
    private long dataMinima;

    public createDatePicker(EditText editText, Context ctx, String title, Long dataMinima){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        this.ctx = ctx;
        this.title = title;
        this.dataMinima = dataMinima;
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {
        // this.editText.setText();

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        editText.setText(sdformat.format(myCalendar.getTime()));

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){
            datePickerDialog = new DatePickerDialog(ctx, this,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(dataMinima);
            datePickerDialog.setTitle(this.title);
            datePickerDialog.show();
        }
    }


    @Override
    public void onClick(View v) {
        datePickerDialog = new DatePickerDialog(ctx, this,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(dataMinima);
        datePickerDialog.setTitle(this.title);
        datePickerDialog.show();


    }
}
