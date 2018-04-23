/*
package br.com.proximojogo.proximojogo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Calendar;

import br.com.proximojogo.proximojogo.utils.EventosPassadosAsyncTask;

*/
/**
 * Created by ale on 22/02/2018.
 *//*


public class VerificarEventosPassadosTask extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //aqui vai os metodos com as coisas que quero executar
        cancelarProximaExecucao();
        proximaExecucao();
        verificarHorario();
        return START_STICKY;
    }

    private void verificarHorario() {
        new EventosPassadosAsyncTask(this).execute();
    }

    public synchronized void proximaExecucao() {
        Intent myIntent = new Intent(this, VerificarEventosPassadosTask.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        stopService(myIntent);
    }

    public synchronized void cancelarProximaExecucao() {
        Intent myIntent = new Intent(this, VerificarEventosPassadosTask.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
*/
