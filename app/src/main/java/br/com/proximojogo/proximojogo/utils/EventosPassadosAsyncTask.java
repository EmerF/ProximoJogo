package br.com.proximojogo.proximojogo.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by ale on 22/02/2018.
 */

public class EventosPassadosAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context context;

    public EventosPassadosAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i("PORRA",FormatarData.getDfHora().format(new Date().getTime()));
        return null;
    }
}
