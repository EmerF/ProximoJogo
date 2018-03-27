package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.Jogador;

public class ListaJogadoresTime extends Fragment {
    private ListView mListView;
    Jogador jogador;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View timesUsuarioView = inflater.inflate(R.layout.fragment_lista_jogadores_time, container, false);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Jogadores");
        mListView =  timesUsuarioView.findViewById(R.id.list_view_jogadores_time);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                jogador = (Jogador) lista.getItemAtPosition(position);
                /**
                 * esse bundle que envia o valor para outro fragment (evitar acoplamento seria interessante
                 * utilizar uma interface) mas não vi necessidade aqui.
                 */
                Bundle bundle = new Bundle();
                TimeFragment timeFragment = new TimeFragment();

                bundle.putString("idTime", jogador.getNomeUser());
                bundle.putString("nome", jogador.getPosicao());
                timeFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, timeFragment).commit();

            }
        });

        Button novoTime =  timesUsuarioView.findViewById(R.id.novo_jogador);
        novoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaJogadoresTime()).commit();
            }
        });

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
         final List<String> list = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
                    String userId =  timeSnapshot.getKey();
                    list.add(userId);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //mListView.setAdapter(firebaseListAdapter);
        PreencheAdapter(list);
        return timesUsuarioView;
    }
    public void PreencheAdapter(List<String> userIds){
        for (String uid: userIds
             ) {

        }
        DatabaseReference mDatabaseListView = FirebaseDatabase.getInstance().getReference().child("Users");
        //mDatabaseListView = mDatabase.child(userId);

        FirebaseListAdapter<Jogador> firebaseListAdapter;

        firebaseListAdapter = new FirebaseListAdapter<Jogador>(
                getActivity(),
                Jogador.class,
                R.layout.row_jogador,
                mDatabaseListView


        )
        {
            @Override
            protected void populateView(View v, Jogador jogador, int position) {
                TextView nome =  v.findViewById(R.id.nome_jogador_lista);
                nome.setText(jogador.getNomeUser());

                TextView tel =  v.findViewById(R.id.posicao_jogador);
                tel.setText("Posição: " +jogador.getPosicao());

            }



        };
        Log.d("Total de Jogadores: ", String.valueOf(firebaseListAdapter.getCount()));

    }

}
