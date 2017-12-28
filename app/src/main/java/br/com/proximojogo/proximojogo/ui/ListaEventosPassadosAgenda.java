package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.proximojogo.proximojogo.entity.Resultado;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;
import br.com.proximojogo.proximojogo.utils.bundle.BundleAgenda;
import br.com.proximojogo.proximojogo.utils.snapshot.ConverteSnapshotResultado;

public class ListaEventosPassadosAgenda extends Fragment {
    private ListView mListView;
    private DatabaseReference mDatabaseAgenda;
    private DatabaseReference mResultados;
    private Resultado resultado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Histórico de Jogos");
        mListView =  eventosDaAgendaView.findViewById(R.id.list_view_agenda);

        Button novaAgenda = eventosDaAgendaView.findViewById(R.id.novo_agenda);
        novaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
            }
        });

        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Agendas" + "/" + GetUser.getUserLogado());
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
                                setResultado(dataSnapshot);
                                agenda.setResultado(ConverteSnapshotResultado.converteSnapShotParaResultado(dataSnapshot));


                                TextView time = v.findViewById(R.id.time_evento);
                                time.setText(agenda.getResultado().getTime1());

                                TextView adv = v.findViewById(R.id.adversario_evento);
                                adv.setText(agenda.getResultado().getTime2());

                                TextView resultado = v.findViewById(R.id.resultado_jogo);
                                resultado.setText(
                                        agenda.getResultado().getTime1() + " " +
                                        agenda.getResultado().getGols1() + " X " +
                                        agenda.getResultado().getGols2() + " " +
                                        agenda.getResultado().getTime2());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                //############# Resultado ##################

                TextView data =  v.findViewById(R.id.data_evento);
                data.setText("Data: " + FormatarData.getDf().format(agenda.getData()));
                TextView hora =  v.findViewById(R.id.hora_evento);
                hora.setText("Hora: " + (FormatarData.getDfHora().format(agenda.getHora())));
                ImageButton btnExcluir = v.findViewById(R.id.main_line_more);
                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO item = getItem(pos);
                        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Agendas/" +
                                GetUser.getUserLogado() + "/" + item.getIdAgenda());
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
                        agenda.setResultado(resultado);
                        agenda.setDataFutura(false);
                        /**
                         * esse bundle que envia o valor para outro fragment (evitar acoplamento seria interessante
                         * utilizar uma interface) mas não vi necessidade aqui.
                         */
                        AgendaFragment agendaFragment = new AgendaFragment();
                        Bundle bundle = BundleAgenda.retornaBundle(agenda);
                        agendaFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.container, agendaFragment).commit();

                        Toast.makeText(v.getContext(), "Alterando! " + pos, Toast.LENGTH_SHORT).show();
                    }
                });

            }

        };
        mListView.setAdapter(firebaseListAdapter);

        return eventosDaAgendaView;
    }

    private void setResultado(DataSnapshot dataSnapshot){
        this.resultado = ConverteSnapshotResultado.converteSnapShotParaResultado(dataSnapshot);
    }

}
