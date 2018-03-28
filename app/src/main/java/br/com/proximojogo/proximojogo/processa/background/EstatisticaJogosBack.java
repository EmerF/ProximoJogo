package br.com.proximojogo.proximojogo.processa.background;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEstatiscaJogosPorData;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEventoTimeData;
import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by Ale on 01/03/2018.
 */

public class EstatisticaJogosBack {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("agendas" + "/" + GetUser.getUserLogado());
    private DatabaseReference estatisticaReference = FirebaseDatabase.getInstance().getReference("estatistica-jogos-list");
    private ArrayList<AgendaDO> eventos = new ArrayList<>();

    public void processaEstatisticaJogosBack() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void fetchData(DataSnapshot dataSnapshot) {
        eventos.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            AgendaDO agendaDO = ds.getValue(AgendaDO.class);
            eventos.add(agendaDO);
        }
//        ordenaListAdversarioData();
        ordenaListAdversarioDataConfrontos();
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
//            estatisticaReference.setValue(listEstatistica);
//
//        }
//
//        return listEstatistica;
//
//    }

    public List<EstatisticaDeJogos> ordenaListAdversarioDataConfrontos() {
        List<EstatisticaDeJogos> listEstatistica = new ArrayList<>();
        if (eventos.size() > 0 && !eventos.isEmpty()) {
            Collections.sort(eventos, new OrdenaEventoTimeData());
            List<List<AgendaDO>> listaDeLista = new ArrayList<>();
            List<AgendaDO> listTime = new ArrayList<>();
            AgendaDO anterior = eventos.get(0);
            listTime.add(anterior);
            for (int i = 1; i < eventos.size(); i++) {
                String adversarioStr;
                String adversarioStr2;
//                if ("AUDAX".equals(anterior.getAdversario())) {
//                    adversarioStr = anterior.getTimes();
//                } else {
//                    adversarioStr = anterior.getAdversario();
//
//                }
//                if ("AUDAX".equals(eventos.get(i).getAdversario())) {
//                    adversarioStr2 = eventos.get(i).getTimes();
//                } else {
//                    adversarioStr2 = eventos.get(i).getAdversario();
//
//                }
//
//                if (adversarioStr.equals(adversarioStr2)) {
//                    listTime.add(eventos.get(i));
//                } else {
//                    listaDeLista.add(listTime);
//                    listTime = new ArrayList<>();
//                    anterior = eventos.get(i);
//                    listTime.add(anterior);
//                }
            }
            listaDeLista.add(listTime);

            for (List<AgendaDO> list : listaDeLista) {
                AgendaDO evento;
                if (list.size() > 1) {
                    evento = list.get(list.size() - 1);
                } else {
                    evento = list.get(0);
                }
                //TODO comentei pq agora não existe mais adversario
//                listEstatistica.add(new EstatisticaDeJogos(evento.getData(), evento.getTimes(), evento.getAdversario(), evento.getObservacao(), list.size()));
            }
            //só para ver no console
//            for (EstatisticaDeJogos es : listEstatistica) {
//                System.out.println(es.getTime2());
//                System.out.println(FormatarData.getDf().format(es.getDataUltimoComfronto()));
//                System.out.println(es.getConfrontos());
//            }
            Collections.sort(listEstatistica, new OrdenaEstatiscaJogosPorData());
            estatisticaReference.setValue(listEstatistica);
        }

        return listEstatistica;

    }
}
