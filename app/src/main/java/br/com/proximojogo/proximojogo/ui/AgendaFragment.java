package br.com.proximojogo.proximojogo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.helper.HelperAgenda;
import br.com.proximojogo.proximojogo.utils.LimparCamposFormulario;

public class AgendaFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private HelperAgenda helper;

    private ImageButton btSalvar;
    private ImageButton btExcluir;
    private ImageButton btListar;
    private View agendaView;
    private EditText obs;
    //estava usando só no Dynamo
    private Handler handler;
    private Activity activity;
    private String idUser;
    private boolean salvou;

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
        helper = new HelperAgenda(agendaView, handler);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Criar Evento do Time");
        btSalvar = (ImageButton) agendaView.findViewById(R.id.bt_salvar_agenda);
        btSalvar.setOnClickListener(this);

        btExcluir = (ImageButton) agendaView.findViewById(R.id.bt_excluir_agenda);
        btExcluir.setOnClickListener(this);

        btListar = (ImageButton) agendaView.findViewById(R.id.bt_listar_agenda);
        btListar.setOnClickListener(this);

        /*obs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                return false;
            }
        });*/

        return agendaView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            idUser = bundle.getString("agenda");
            if (idUser != null) {
                try {
                    helper.preencheFormulario(idUser);
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
           salvou = LimparCamposFormulario.validaEditTextVazio((ViewGroup)this.getView());
            if(!salvou){

                ExibirToast.ExibirToastComIcone(activity,R.drawable.alerta,R.color.colorPrimary,"Preencha os campos, meu Bem!");


            }else {
                helper.salvar(v);
            }
        } catch (Exception e) {
            ExibirToast.ExibirToastComIcone(activity,R.drawable.alerta,R.color.colorRed,"Erro ao salvar a agenda ): ");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_salvar_agenda) {
            salvarAgenda(v);
            if(salvou){
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
            }

        } else if (i == R.id.bt_excluir_agenda) {
            if (idUser != null) {
                helper.excluir(v, idUser);
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
            } else {
                Toast.makeText(activity, "Agenda não pode ser excluída!", Toast.LENGTH_SHORT).show();
            }
        } else if (i == R.id.bt_listar_agenda) {
            getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
        }
    }


}
