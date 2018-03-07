package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.custom.adapter.EstatisticaJogoAdapter;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

public class ListaEstatisticaJogos extends Fragment {
    private DatabaseReference estatisticaReference = FirebaseDatabase.getInstance().getReference("estatistica-jogos-list");
    private ListView mListView;
    private ArrayList<EstatisticaDeJogos> estatisticas = new ArrayList<>();
    private static EstatisticaJogoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_estatistica_jogos, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Estatistica Jogos");
        pesquisaEstatisticas();
        estatisticaReference.keepSynced(true);
        mListView = (ListView) eventosDaAgendaView.findViewById(R.id.list_estatistica_jogo);
        adapter= new EstatisticaJogoAdapter(estatisticas,getContext());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EstatisticaDeJogos dataModel= estatisticas.get(position);

                Snackbar.make(view, dataModel.getTime1()+"\n"+dataModel.getTime2()+" API: "+dataModel.getConfrontos(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

        return eventosDaAgendaView;
    }


    public void pesquisaEstatisticas() {
        estatisticaReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);//Passar os dados para a interface grafica
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
            }
        });
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        estatisticas.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            EstatisticaDeJogos esj = ds.getValue(EstatisticaDeJogos.class);
            estatisticas.add(esj);
        }


    }



}
