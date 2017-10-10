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
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.helper.HelperTime;
import br.com.proximojogo.proximojogo.utils.LimparCamposFormulario;


public class TimeFragment extends Fragment  implements View.OnClickListener {

    private DatabaseReference databaseReference;


    private EditText nomeTime;
    private EditText responsavel;
    private EditText telefone;
    private Activity activity;
    private ImageButton btSalvar;
    private HelperTime helper;
    private Handler handler;
    private View timeView;
    private boolean salvou;
    private String idUser;


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
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaEventosAgenda()).commit();

            }
        }

    }

   /* @Override
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
    }*/

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
