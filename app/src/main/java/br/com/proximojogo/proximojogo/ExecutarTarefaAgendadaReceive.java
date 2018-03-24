package br.com.proximojogo.proximojogo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import br.com.proximojogo.proximojogo.utils.FormatarData;

/**
 * Created by Ale on 21/02/2018.
 */

public class ExecutarTarefaAgendadaReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //      Código a executar
        //Definir a hora de início
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE,50);
        calendar.set(Calendar.SECOND, 0);
        Log.i("TEMPO", FormatarData.getDfHora().format(new Date().getTime()));

        //Definir intervalo de 6 horas
//        long intervalo = 6*60*60*1000; //6 horas em milissegundos
        long intervalo = 60*1000; //1 min em milissegundos

        Intent tarefaIntent = new Intent(context, ExecutarTarefaAgendadaReceive.class);
        PendingIntent tarefaPendingIntent = PendingIntent.getBroadcast(context,1234, tarefaIntent,0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Definir o alarme para acontecer todos os dias às 10 horas
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, tarefaPendingIntent);

        //Definir o alarme para acontecer de 6 em 6 horas a partir das 10 horas
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                intervalo, tarefaPendingIntent);
    }

}
