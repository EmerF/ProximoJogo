package br.com.proximojogo.proximojogo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.helper.HelperAgenda;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

public class ListaEventosAgenda extends Fragment {
    private DatabaseReference mDatabase;
    private ListView mListView;
    AgendaDO agenda;
    private DatabaseReference mDatabaseAgenda;
    ImageButton btnExcluir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Eventos do Time");
        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("agendas");
        mListView = (ListView) eventosDaAgendaView.findViewById(R.id.list_view_agenda);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                agenda = (AgendaDO) lista.getItemAtPosition(position);
                /**
                 * esse bundle qu envia o valor para outro fragment (evitar acoplamento seria interessante
                 * utilizar uma interface) mas não vi necessidade aqui.
                 */
                Bundle bundle = new Bundle();
                AgendaFragment agendaFragment = new AgendaFragment();
                bundle.putString("agenda", agenda.getIdAgenda());
                agendaFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, agendaFragment).commit();

            }
        });

        Button novaAgenda = (Button) eventosDaAgendaView.findViewById(R.id.novo_agenda);
        novaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("agendas" + "/" + GetUser.getUserLogado());
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day - 1);


        FirebaseListAdapter<AgendaDO> firebaseListAdapter = new FirebaseListAdapter<AgendaDO>(
                getActivity(),
                AgendaDO.class,
                R.layout.row_evento_excluir,
                mDatabase.startAt(c.getTimeInMillis()).orderByChild("data") // ordena os dados pelo campo informado...


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

            @Override
            public View getView(int posicao, View view, ViewGroup viewGroup) {
                final int pos =posicao;
                if (view == null) {
                    view = getActivity().getLayoutInflater().inflate(mLayout, viewGroup, false);
                }
                ImageButton btnExcluir = view.findViewById(R.id.main_line_more);
                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO item = getItem(pos);
                        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("agendas/" + GetUser.getUserLogado() + "/" + item.getIdAgenda());
                        mDatabaseAgenda.removeValue();
                        //Acao do primeiro botao
                        Toast.makeText(v.getContext(), "Vou excluir vc filho daputa posição: " + pos, Toast.LENGTH_SHORT).show();
                    }
                });

                return view;
            }
        };
        mListView.setAdapter(firebaseListAdapter);

        return eventosDaAgendaView;
    }

}
