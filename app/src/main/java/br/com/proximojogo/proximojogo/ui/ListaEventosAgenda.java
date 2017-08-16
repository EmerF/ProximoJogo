package br.com.proximojogo.proximojogo.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.adapter.RecyclerAdapterEventosDaAgenda;
import br.com.proximojogo.proximojogo.entity.AgendaDO;

public class ListaEventosAgenda extends Fragment implements ClickRecyclerViewInterface {

    RecyclerAdapterEventosDaAgenda adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<AgendaDO> eventosDaAgenda = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    DatabaseReference databaseAgendas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);

        databaseAgendas = FirebaseDatabase.getInstance().getReference("agendas");

        //e avisa o adapter que o conteúdo da lista foi alterado
        //  adapter.notifyDataSetChanged();
        setaRecyclerView(eventosDaAgendaView);
        setaButtons(eventosDaAgendaView);

        return eventosDaAgendaView;
    }

    @Override
    public void onCustomClick(Object object) {

    }

    public void setaRecyclerView(View activity) {

        //Aqui é instanciado o Recyclerview
        mRecyclerView = (RecyclerView) activity.findViewById(R.id.recycler_eventos_agenda);
        mLayoutManager = new LinearLayoutManager(activity.getContext());
        // mLayoutManager = new GridLayoutManager(activity.getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        eventosDaAgenda = listar();

        adapter = new RecyclerAdapterEventosDaAgenda(activity.getContext(), eventosDaAgenda, this);
        mRecyclerView.setAdapter(adapter);
    }

    public List<AgendaDO> listar() {
        try {
           final ArrayList<AgendaDO> eventos = new ArrayList<>();
            //attaching value event listener
            databaseAgendas.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //clearing the previous artist list
                    eventos.clear();

                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //getting artist
                        AgendaDO agendaDO = postSnapshot.getValue(AgendaDO.class);
                        //adding artist to the list
                        eventos.add(agendaDO);
                    }
                }
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return eventos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setaButtons(View activity) {

        floatingActionButton = (FloatingActionButton) activity.findViewById(R.id.fab_fabteste);

    }

    /**
     * Chama os listeners para os botões
     */
    public void listenersButtons() {
        final FragmentManager fragmentManager = getFragmentManager();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.container, new AgendaFragment()).commit();

            }
        });
    }
}
