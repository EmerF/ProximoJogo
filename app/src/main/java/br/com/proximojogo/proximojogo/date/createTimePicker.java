package br.com.proximojogo.proximojogo.date;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by emerson on 03/12/16.
 */

public class createTimePicker  implements View.OnFocusChangeListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    /* Cria um timepicker a partir do click em um edit text e preenche com data após clicar em ok

     */

    private EditText editText;
    private Calendar myCalendar;
    private Context ctx;
    private String title;
    private TimePickerDialog timePickerDialog;
    private int hour;
    private int minute ;

    public createTimePicker(EditText editText, Context ctx, String title){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        this.ctx = ctx;
        this.title = title;
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String format=":";
        if(minute < 10 ){
            format = ":0";
        }

        this.editText.setText( hourOfDay + format + minute);

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){

            hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            minute = myCalendar.get(Calendar.MINUTE);
            timePickerDialog =  new TimePickerDialog(ctx, 3,this, hour, minute,true);
            //timePickerDialog.setTitle(this.title);
            timePickerDialog.show();
        }
    }


    @Override
    public void onClick(View v) {

        timePickerDialog = new TimePickerDialog(ctx,3, this,
                myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);

        timePickerDialog.setTitle(this.title);
        timePickerDialog.show();


    }


}
