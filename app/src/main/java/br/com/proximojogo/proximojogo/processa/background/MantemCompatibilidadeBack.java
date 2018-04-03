package br.com.proximojogo.proximojogo.processa.background;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.entity.Resultado;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEstatiscaJogosPorData;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEventoTimeData;
import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;
import br.com.proximojogo.proximojogo.utils.GetUser;

import static br.com.proximojogo.proximojogo.conexao.constantes.dao.ConstantesDAO.AGENDA_DAO;
import static br.com.proximojogo.proximojogo.conexao.constantes.dao.ConstantesDAO.RESULTADO_DAO;

/**
 * Created by Ale on 01/03/2018.
 */

public class MantemCompatibilidadeBack {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(AGENDA_DAO + "/" + GetUser.getUserLogado());
    private Resultado resultado;
    private DatabaseReference mDatabaseRes;
    private DatabaseReference mDatabaseAgenda;

    public void processaCompatibilidade() {
        verificaCompatibilidadeAgenda();

    }

    /**
     * Manter Agenda compativel com as versão do APP
     */
    private void verificaCompatibilidadeAgenda() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                verificaCompatibilidadeCrudAgenda(dataSnapshot);//Passar os dados para a interface grafica
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
            }
        });
    }

    private void verificaCompatibilidadeCrudAgenda(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (!ds.hasChild("idResultado")) {
                mDatabaseRes = FirebaseDatabase.getInstance().getReference(RESULTADO_DAO);
                //getUser id do Firebase para setar na agenda
                // e colocar o nome junto com o id para identificar o nó
                String key = mDatabaseRes.push().getKey();
                resultado = new Resultado();
                String stTime = (String) ds.child("times").getValue();
                String stAdversario = (String) ds.child("adversario").getValue();
                resultado.setIdResultado(key);
                resultado.setTime1(stTime);
                resultado.setTime2(stAdversario);
                mDatabaseRes.child(resultado.getIdResultado()).setValue(resultado);
                AgendaDO agendaDO = ds.getValue(AgendaDO.class);
                agendaDO.setIdResultado(resultado.getIdResultado());
                mDatabaseAgenda = FirebaseDatabase.getInstance().getReference(AGENDA_DAO);
                mDatabaseAgenda.child(agendaDO.getIdUser() + "/" + agendaDO.getIdAgenda()).setValue(agendaDO);
            }
        }
    }
    /**
     * Manter Agenda compativel com as versão do APP
     */


}
