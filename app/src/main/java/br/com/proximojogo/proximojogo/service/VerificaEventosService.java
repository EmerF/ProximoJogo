package br.com.proximojogo.proximojogo.service;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEstatiscaJogosPorData;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEventoTimeData;
import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by Ale on 28/02/2018.
 */

public class VerificaEventosService extends JobService {
//    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("agendas" + "/" + GetUser.getUserLogado());
    private DatabaseReference estatisticaReference = FirebaseDatabase.getInstance().getReference("estatistica-jogos-list");
    private ArrayList<AgendaDO> eventos = new ArrayList<>();
    private ArrayList<EstatisticaDeJogos> estatisticas = new ArrayList<>();

    @Override
    public boolean onStartJob(JobParameters job) {
//        retrieve();
        buscaEventos();
        return false;

    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    private void postNotif(EstatisticaDeJogos estatisticaDeJogos) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.soccer_player_icon)
                .setTicker("Há confrontos com mais de 30 dias!")
                .setContentTitle("Há " + eventos.size()+" com mais de 30 dias!")
                .setContentText("Vai Audax! Vamos ganhar!")
                .setAutoCancel(true);

        int id = 1;

        NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.notify(id, builder.build());
    }

//    public List<EstatisticaDeJogos> ordenaListAdversarioData() {
//        List<EstatisticaDeJogos> listEstatistica = new ArrayList<>();
//        if (eventos.size() > 0 && !eventos.isEmpty()) {
//            Collections.sort(eventos, new OrdenaEventoTimeData());
//            AgendaDO anterior = eventos.get(0);
//            for (int i = 1; i < eventos.size(); i++) {
//                if (!anterior.getAdversario().equals(eventos.get(i).getAdversario())) {
//                    listEstatistica.add(new EstatisticaDeJogos(anterior.getData(), anterior.getTimes(), anterior.getAdversario(), anterior.getObservacao()));
//                    anterior = eventos.get(i);
//                }
//            }
//            Collections.sort(listEstatistica, new OrdenaEstatiscaJogosPorData());
//            postNotif(listEstatistica.get(0));
//        }
//
//        return listEstatistica;
//
//    }

    public void buscaEventos() {
        LocalDate hoje = new LocalDate(new Date());
        LocalDate trintaDiasAtras = hoje.minusDays(120);
        Query query1 = estatisticaReference.orderByChild("tim2").equalTo("GALATICOS");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);//Passar os dados para a interface grafica
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
            }
        });
    }
//    public void buscaEventos() {
//        Query query1 = mDatabase;
//        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                fetchData(dataSnapshot);//Passar os dados para a interface grafica
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //Se ocorrer um erro
//            }
//        });
//    }

//    private void fetchData(DataSnapshot dataSnapshot) {
//        eventos.clear();
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            AgendaDO agendaDO = ds.getValue(AgendaDO.class);
//            eventos.add(agendaDO);
//        }
//        for(AgendaDO a: eventos){
//            Log.i("EVENTOS", a.getAdversario()+" Data: "+FormatarData.getDf().format(a.getData()));
//        }
//    }
    private void fetchData(DataSnapshot dataSnapshot) {
        eventos.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            EstatisticaDeJogos estatisticaDeJogos = ds.getValue(EstatisticaDeJogos.class);
            estatisticas.add(estatisticaDeJogos);
        }
        for(EstatisticaDeJogos a: estatisticas){
            Log.i("EVENTOS", a.getTime2()+" Data: "+FormatarData.getDf().format(a.getDataUltimoComfronto()));
        }
    }
}
