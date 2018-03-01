package br.com.proximojogo.proximojogo.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEventoTimeData;
import br.com.proximojogo.proximojogo.service.VerificaEventosService;

/**
 * Created by ale on 22/02/2018.
 */

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
