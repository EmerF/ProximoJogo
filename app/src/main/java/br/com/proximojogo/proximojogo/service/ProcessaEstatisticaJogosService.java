package br.com.proximojogo.proximojogo.service;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEstatiscaJogosPorData;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEventoTimeData;
import br.com.proximojogo.proximojogo.processa.background.EstatisticaJogosBack;
import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by Ale on 28/02/2018.
 */

public class ProcessaEstatisticaJogosService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.i(ProcessaEstatisticaJogosService.class.getName().toString(), "Inicio processamento Estatisitca Jogos");
        new EstatisticaJogosBack().processaEstatisticaJogosBack();
        return false;

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

}
