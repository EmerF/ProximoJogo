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

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Trigger;
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
import br.com.proximojogo.proximojogo.service.ProcessaCompatibilidadeAppService;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;
import br.com.proximojogo.proximojogo.utils.bundle.BundleAgenda;
import br.com.proximojogo.proximojogo.utils.snapshot.ConverteSnapshotResultado;

import static br.com.proximojogo.proximojogo.conexao.constantes.dao.ConstantesDAO.AGENDA_DAO;
import static br.com.proximojogo.proximojogo.conexao.constantes.dao.ConstantesDAO.RESULTADO_DAO;

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
        mListView = eventosDaAgendaView.findViewById(R.id.list_view_agenda);

        Button novaAgenda = eventosDaAgendaView.findViewById(R.id.novo_agenda);
        novaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
            }
        });

        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child(AGENDA_DAO + "/" + GetUser.getUserLogado());
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day - 1);


        //usado teste Ale depois apagar
        FirebaseJobDispatcher dispatcher3 =
                new FirebaseJobDispatcher(
                        new GooglePlayDriver(eventosDaAgendaView.getContext())
                );
        dispatcher3.mustSchedule(
                dispatcher3.newJobBuilder()
                        .setService(ProcessaCompatibilidadeAppService.class)
                        .setTag("PROCESSA_MANTEM_COMPATIBILIDADE_APP")
                        .setRecurring(true)
                        .setTrigger(Trigger.executionWindow(1, 1))
                        .build()
        );
        //usado teste Ale depois apagar


        FirebaseListAdapter<AgendaDO> firebaseListAdapter = new FirebaseListAdapter<AgendaDO>(
                getActivity(),
                AgendaDO.class,
                R.layout.row_evento_excluir_passados,
                mDatabaseAgenda.endAt(c.getTimeInMillis()).orderByChild("data") // ordena os dados pelo campo informado...


        ) {
            @Override
            protected void populateView(final View v, final AgendaDO agenda, int position) {
                final int pos = position;
                //############# Resultado ##################
                mResultados = FirebaseDatabase.getInstance().getReference().child(RESULTADO_DAO + "/" + agenda.getIdResultado());
                mResultados.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                setResultado(dataSnapshot);
                                agenda.setResultado(ConverteSnapshotResultado.converteSnapShotParaResultado(dataSnapshot));

                                TextView time = v.findViewById(R.id.time_evento_passado);
                                time.setText(dataSnapshot.child("time1").getValue(String.class));

                                TextView adv = v.findViewById(R.id.adversario_evento_passado);
                                adv.setText(dataSnapshot.child("time2").getValue(String.class));


                                TextView resultado = v.findViewById(R.id.resultado_jogo);
                                resultado.setText(
                                        //agenda.getResultado().getTime1() + "   " +
                                        agenda.getResultado().getGols1() + " X " +
                                                agenda.getResultado().getGols2());
                                //agenda.getResultado().getTime2());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                //############# Resultado ##################

                TextView data = v.findViewById(R.id.data_evento);
                data.setText("Data: " + FormatarData.getDf().format(agenda.getData()));
                TextView local = v.findViewById(R.id.local_evento);
                local.setText("Local: " + agenda.getArena());


                ImageButton btnExcluir = v.findViewById(R.id.row_excluir_passados);
                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO item = getItem(pos);
                        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child(AGENDA_DAO + "/" +
                                GetUser.getUserLogado() + "/" + item.getIdAgenda());
                        mResultados = FirebaseDatabase.getInstance().getReference().child(RESULTADO_DAO + "/" + agenda.getIdResultado());

                        mDatabaseAgenda.removeValue();
                        mResultados.removeValue();
                        //Acao do primeiro botao
                        Toast.makeText(v.getContext(), "Excluido! ", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageButton btnEditar = v.findViewById(R.id.row_edit_passados);
                btnEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO agenda2 = getItem(pos);
                        agenda2.setResultado(agenda.getResultado());
                        agenda2.setDataFutura(false);
                        /**
                         * esse bundle que envia o valor para outro fragment (evitar acoplamento seria interessante
                         * utilizar uma interface) mas não vi necessidade aqui.
                         */
                        AgendaFragment agendaFragment = new AgendaFragment();
                        BundleAgenda bundleAgenda = new BundleAgenda();
                        Bundle bundle = bundleAgenda.retornaBundle(agenda2);
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

    private void setResultado(DataSnapshot dataSnapshot) {
        this.resultado = ConverteSnapshotResultado.converteSnapShotParaResultado(dataSnapshot);
    }

}
