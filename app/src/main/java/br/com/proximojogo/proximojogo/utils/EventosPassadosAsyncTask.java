/*
package br.com.proximojogo.proximojogo.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;

import br.com.proximojogo.proximojogo.service.VerificaEventosService;

*/
/**
 * Created by ale on 22/02/2018.
 *//*


public class EventosPassadosAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private DatabaseReference mDatabase;
    private ArrayList<AgendaDO> eventos = new ArrayList<>();
    private boolean finalizado = false;

    public EventosPassadosAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i("SERVICE_JOGOS",FormatarData.dfHora.format(new Date()));
        int seisHoras = 6 * 60 * 60;
        FirebaseJobDispatcher dispatcher =
                new FirebaseJobDispatcher(
                        new GooglePlayDriver(context)
                );
        dispatcher.mustSchedule(
                dispatcher.newJobBuilder()
                        .setService(VerificaEventosService.class)
                        .setTag("JOGOS_MAIS_30_DIAS")
                        .setRecurring(true)
                        .setTrigger(Trigger.executionWindow(5, 30))
                        .build()
        );
        return null;
    }



}
*/
