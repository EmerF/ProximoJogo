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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.Time;
import br.com.proximojogo.proximojogo.helper.HelperTime;
import br.com.proximojogo.proximojogo.utils.LimparCamposFormulario;


public class TimeFragment extends Fragment  implements View.OnClickListener {

    private DatabaseReference databaseReference;


    private EditText nomeTime;
    private EditText responsavel;
    private EditText telefone;
    private Activity activity;
    private HelperTime helper;
    private Handler handler;
    private View timeView;
    private boolean salvou;
    private String idTime;
    private ImageButton btSalvar;
    private ImageButton btExcluir;
    private ImageButton btListar;


    static class TimeHandler extends Handler {
        WeakReference<TimeFragment> weakTimeFragment;

        public TimeHandler(TimeFragment timeFragment) {
            weakTimeFragment = new WeakReference<TimeFragment>(timeFragment);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (weakTimeFragment != null) {
                new AlertDialog.Builder(weakTimeFragment.get().activity)
                        .setTitle("Aviso")
                        .setMessage("Time Salvo com Sucesso!!!")
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
//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = new TimeHandler(this);
        timeView = inflater.inflate(R.layout.fragment_cadastrar_time, container, false);
        helper = new HelperTime(timeView, handler);

        btSalvar = (ImageButton) timeView.findViewById(R.id.bt_salvar_time);
        btSalvar.setOnClickListener(this);

        btExcluir = (ImageButton) timeView.findViewById(R.id.bt_excluir_time);
        btExcluir.setOnClickListener(this);

        btListar = (ImageButton) timeView.findViewById(R.id.bt_listar_times);
        btListar.setOnClickListener(this); // teste Ale Viado peludo

        return timeView;
    }


    public void salvarTime(View v) {
        try {
            LimparCamposFormulario lf = new LimparCamposFormulario();
            salvou = lf.validaEditTextVazio((ViewGroup) this.getView());
            if(!salvou){
                //Toast.makeText(activity,"Preencher os campos obrigaórios, por favor !",Toast.LENGTH_SHORT).show();
                ExibirToast.ExibirToastComIcone(activity,R.drawable.alerta,R.color.colorRed,"Preencha os campos, meu Bem!");


            }else {
                helper.salvarTime(v);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.bt_salvar_time){
            salvarTime(v);
            if(salvou){
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaTimesUsuario()).commit();

            }
        }
        if(i == R.id.bt_listar_times){
            getFragmentManager().beginTransaction().replace(R.id.container, new ListaTimesUsuario()).commit();
        }
        if (i == R.id.bt_excluir_time) {
            if (idTime != null) {
                helper.excluir(v, idTime);
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaTimesUsuario()).commit();
            } else {
                Toast.makeText(activity, "Time não pode ser excluído!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            idTime = bundle.getString("idTime");
            Time time = new Time();
            time.setIdTime(idTime);
            time.setNomeTime(bundle.getString("nome"));
            time.setResponsavelTime(bundle.getString("responsavel"));
            time.setTelefoneResponsavel(bundle.getString("telefone"));

            if (idTime != null) {
                try {
                    helper.preencheFormulario(time);
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

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }


    public EditText getNomeTime() {
        return nomeTime;
    }

    public void setNomeTime(EditText nomeTime) {
        this.nomeTime = nomeTime;
    }

    public EditText getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(EditText responsavel) {
        this.responsavel = responsavel;
    }

    public EditText getTelefone() {
        return telefone;
    }

    public void setTelefone(EditText telefone) {
        this.telefone = telefone;
    }


    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ImageButton getBtSalvar() {
        return btSalvar;
    }

    public void setBtSalvar(ImageButton btSalvar) {
        this.btSalvar = btSalvar;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public View getTimeView() {
        return timeView;
    }

    public void setTimeView(View timeView) {
        this.timeView = timeView;
    }
}
