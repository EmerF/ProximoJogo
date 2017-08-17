package br.com.proximojogo.proximojogo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.utils.FormatarData;

public class ListaEventosAgenda extends Fragment {
    private DatabaseReference mDatabase;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);
        mListView = (ListView) eventosDaAgendaView.findViewById(R.id.list_view_agenda);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                AgendaDO agenda = (AgendaDO) lista.getItemAtPosition(position);
                getFragmentManager().beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
//                intentVaiProFormulario.putExtra("Agenda", agenda); // passa valor como parametro
//                startActivity(intentVaiProFormulario);

            }
        });

        Button novaAgenda = (Button)eventosDaAgendaView.findViewById(R.id.novo_agenda);
        novaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
//                Intent vaiPFormulario = new Intent(ListaAgenda.this,AgendaActivityUI.class);
//                startActivity(vaiPFormulario);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("agendas");

        FirebaseListAdapter<AgendaDO> firebaseListAdapter = new FirebaseListAdapter<AgendaDO>(
                getActivity(),
                AgendaDO.class,
                R.layout.row_evento,
                mDatabase.orderByChild("data") // ordena os dados pelo campo informado...

        ) {
            @Override
            protected void populateView(View v, AgendaDO agenda, int position) {
                TextView time = (TextView) v.findViewById(R.id.time_evento);
                time.setText(agenda.getTimes());
                TextView tipo = (TextView) v.findViewById(R.id.adversario_evento);
                tipo.setText(" " + agenda.getAdversario());
                TextView local = (TextView) v.findViewById(R.id.local_evento);
                local.setText("Local: " + agenda.getArena());
                TextView data = (TextView) v.findViewById(R.id.data_evento);
                data.setText("Data: " + FormatarData.getDf().format(agenda.getData()));
                TextView hora = (TextView) v.findViewById(R.id.hora_evento);
                hora.setText("Hora: " + (FormatarData.getDfHora().format(agenda.getHora())));
            }


        };
        mListView.setAdapter(firebaseListAdapter);

        return eventosDaAgendaView;
    }


}
