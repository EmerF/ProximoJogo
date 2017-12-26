package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

public class ListaEventosPassadosAgenda extends Fragment {
    private ListView mListView;
    AgendaDO agenda;
    private DatabaseReference mDatabaseAgenda;
    ImageButton btnExcluir;
    private DatabaseReference mResultados;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Histórico de Jogos");
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

        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("agendas" + "/" + GetUser.getUserLogado());
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day - 1);


        FirebaseListAdapter<AgendaDO> firebaseListAdapter = new FirebaseListAdapter<AgendaDO>(
                getActivity(),
                AgendaDO.class,
                R.layout.row_evento_excluir,
                mDatabaseAgenda.endAt(c.getTimeInMillis()).orderByChild("data") // ordena os dados pelo campo informado...


        ) {
            @Override
            protected void populateView(final View v, final AgendaDO agenda, int position) {
                final int pos = position;
                //############# Resultado ##################
                mResultados = FirebaseDatabase.getInstance().getReference().child("Resultados/" + agenda.getIdResultado());
                mResultados.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                TextView time =  v.findViewById(R.id.time_evento);
                                time.setText(dataSnapshot.child("time1").getValue(String.class));

                                TextView gols1 = v.findViewById(R.id.gols1);
                                gols1.setText(dataSnapshot.child("gols1").getValue(String.class));

                                TextView adv =  v.findViewById(R.id.adversario_evento);
                                adv.setText(" " + dataSnapshot.child("time2").getValue(String.class));

                                TextView gols2 = v.findViewById(R.id.gols2);
                                gols2.setText(dataSnapshot.child("gols2").getValue(String.class));


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                //############# Resultado ##################

                TextView data = (TextView) v.findViewById(R.id.data_evento);
                data.setText("Data: " + FormatarData.getDf().format(agenda.getData()));
                TextView hora = (TextView) v.findViewById(R.id.hora_evento);
                hora.setText("Hora: " + (FormatarData.getDfHora().format(agenda.getHora())));
                ImageButton btnExcluir = v.findViewById(R.id.main_line_more);
                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO item = getItem(pos);
                        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Agendas/" + GetUser.getUserLogado() + "/" + item.getIdAgenda());
                        mResultados = FirebaseDatabase.getInstance().getReference().child("Resultado/" + agenda.getIdResultado());
                                                
                        mDatabaseAgenda.removeValue();
                        mResultados.removeValue();
                        //Acao do primeiro botao
                        Toast.makeText(v.getContext(), "Excluido! ", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageButton btnEditar = v.findViewById(R.id.row_edit);
                btnEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO agenda = getItem(pos);
                        /**
                         * esse bundle qu envia o valor para outro fragment (evitar acoplamento seria interessante
                         * utilizar uma interface) mas não vi necessidade aqui.
                         */
                        Bundle bundle = new Bundle();
                        AgendaFragment agendaFragment = new AgendaFragment();
                        bundle.putString("idAgenda", agenda.getIdAgenda());
                        bundle.putString("evento", agenda.getEvento());
                        bundle.putString("local", agenda.getArena());
                        //bundle.putString("time", agenda.getTimes());
                        bundle.putString("data", agenda.getData().toString());
                        bundle.putString("hora", agenda.getHora().toString());
                        //bundle.putString("adversario", agenda.getAdversario());
                        bundle.putString("valor", agenda.getValor().toString());
                        bundle.putString("observacao", agenda.getObservacao());
                        agendaFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.container, agendaFragment).commit();
                        Toast.makeText(v.getContext(), "Alterado! " + pos, Toast.LENGTH_SHORT).show();
                    }
                });

            }

        };
        mListView.setAdapter(firebaseListAdapter);

        return eventosDaAgendaView;
    }

}
