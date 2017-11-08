package br.com.proximojogo.proximojogo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import br.com.proximojogo.proximojogo.entity.AgendaDO;
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
    private String idAgenda;
    private boolean salvou;
    private boolean validarCampos;
    private InputMethodManager imm;

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

        return agendaView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            //idUser = bundle.getString("agenda");
            idAgenda = bundle.getString("idAgenda");
            AgendaDO agenda = new AgendaDO();
            agenda.setIdAgenda(idAgenda);
            agenda.setEvento(bundle.getString("evento"));
            agenda.setArena(bundle.getString("local"));
            agenda.setTimes(bundle.getString("time"));
            agenda.setData(Long.parseLong(bundle.getString("data")));
            agenda.setHora(Long.parseLong(bundle.getString("hora")));
            agenda.setAdversario(bundle.getString("adversario"));
            agenda.setValor(Double.parseDouble(bundle.getString("valor")));
            agenda.setObservacao(bundle.getString("observacao"));

            if (agenda != null) {
                try {
                    helper.preencheFormulario(agenda);
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
            LimparCamposFormulario lf = new LimparCamposFormulario();
            validarCampos = lf.validaEditTextVazio((ViewGroup) this.getView());
            if (!validarCampos) {

                ExibirToast.ExibirToastComIcone(activity, R.drawable.alerta, R.color.colorRed, "Preencha os campos, meu Bem!");

            } else {
                salvou = helper.salvar(v);

            }
        } catch (Exception e) {
            ExibirToast.ExibirToastComIcone(activity, R.drawable.alerta, R.color.colorRed, "Erro ao salvar a agenda ): ");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_salvar_agenda) {

            salvarAgenda(v);

            if (salvou) {
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();

            }

        }
        if (i == R.id.bt_excluir_agenda) {
            if (idAgenda != null) {
                helper.excluir(v, idAgenda);
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
            } else {
                Toast.makeText(activity, "Agenda não pode ser excluída!", Toast.LENGTH_SHORT).show();
            }
        } else if (i == R.id.bt_listar_agenda) {
            getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
