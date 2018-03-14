package br.com.proximojogo.proximojogo;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ale on 18/11/2017.
 */

public class ProximoJogoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
