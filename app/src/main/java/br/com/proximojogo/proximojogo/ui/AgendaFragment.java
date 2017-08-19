package br.com.proximojogo.proximojogo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.util.Collections;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.helper.FormularioHelper;

public class AgendaFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FormularioHelper helper;

    private Button btSalvar;
    private Button btExcluir;
    private Button btListar;
    private View agendaView;
    //estava usando só no Dynamo
    private Handler handler;
    private Activity activity;
    private String idAgenda;

    static class AgendaHandler extends Handler {
        WeakReference<AgendaFragment> weakAgendaFragment;

        public AgendaHandler(AgendaFragment agendaFragment) {
            weakAgendaFragment = new WeakReference<AgendaFragment>(agendaFragment);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (weakAgendaFragment != null) {
                new AlertDialog.Builder(weakAgendaFragment.get().activity)
                        .setTitle("Aviso")
                        .setMessage("Agenda Salva com Sucesso!!!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//
//                            }
//                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = new AgendaHandler(this);
        agendaView = inflater.inflate(R.layout.fragment_agenda, container, false);
        helper = new FormularioHelper(agendaView, handler);

        btSalvar = (Button) agendaView.findViewById(R.id.bt_salvar_agenda);
        btSalvar.setOnClickListener(this);

        btExcluir = (Button) agendaView.findViewById(R.id.bt_excluir_agenda);
        btExcluir.setOnClickListener(this);

        btListar = (Button) agendaView.findViewById(R.id.bt_listar_agenda);
        btListar.setOnClickListener(this);

        return agendaView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            idAgenda = bundle.getString("agenda");
            if (idAgenda != null) {
                try {
                    helper.preencheFormulario(idAgenda);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

    }

    public void salvarAgenda(View v) {
        try {
            helper.salvar(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_salvar_agenda) {
            salvarAgenda(v);
            getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
        } else if (i == R.id.bt_excluir_agenda) {
            if (idAgenda != null) {
                helper.excluir(v, idAgenda);
            } else {
                Toast.makeText(activity, "Agenda não pode ser excluída!", Toast.LENGTH_SHORT).show();
            }
        } else if (i == R.id.bt_listar_agenda) {
            getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
        }
    }
}
