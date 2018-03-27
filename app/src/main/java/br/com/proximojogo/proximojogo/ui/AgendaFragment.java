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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.entity.Resultado;
import br.com.proximojogo.proximojogo.helper.HelperAgenda;
import br.com.proximojogo.proximojogo.utils.LimparCamposFormulario;

public class AgendaFragment extends Fragment implements View.OnClickListener {

    private HelperAgenda helper;

    private Button btSalvar;
    private View agendaView;
    private EditText obs;
    //estava usando só no Dynamo
    private Handler handler;
    private Activity activity;
    private String idAgenda;
    private boolean salvou;
    private boolean validarCampos;
    private boolean dataFutura = true;

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
        btSalvar = (Button) agendaView.findViewById(R.id.bt_salvar_agenda);
        btSalvar.setOnClickListener(this);
        setRetainInstance(true);
        return agendaView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /* Pode haver inicialização com dados do jogos passados
        por esse motivo repete-se o código para inicilização
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AgendaDO agenda = new AgendaDO();
        Bundle bundle = getArguments();

        if (bundle != null) {

            idAgenda = bundle.getString("idAgenda");
            agenda.setIdAgenda(idAgenda);
            agenda.setEvento(bundle.getString("evento"));
            agenda.setArena(bundle.getString("local"));
            agenda.setData(Long.parseLong(bundle.getString("data")));
            agenda.setTimes(bundle.getString("time"));
           agenda.setData(Long.parseLong(bundle.getString("data")));
            agenda.setHora(Long.parseLong(bundle.getString("hora")));
            agenda.setDataFutura(Boolean.valueOf(bundle.getString("dataFutura")));
            agenda.setValor(Double.parseDouble(bundle.getString("valor")));
            agenda.setResultado(new Resultado(
                    bundle.getString("idResultado"),
                    bundle.getString("time1"),
                    bundle.getString("time2"),
                    bundle.getString("gols1"),
                    bundle.getString("gols2")));
            dataFutura = agenda.getDataFutura();
            try {
                    helper.inicializaCamposTela(agendaView, agenda);
                  helper.preencheFormulario(agenda);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            helper.inicializaCamposTela(agendaView, agenda);
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

                ExibirToast.ExibirToastComIcone(activity, R.drawable.alerta, R.color.colorRed, "Preencher campos vazios!");

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
                if(dataFutura){
                    getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();
                }else {
                    getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosPassadosAgenda()    ).commit();
                }


            }
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


