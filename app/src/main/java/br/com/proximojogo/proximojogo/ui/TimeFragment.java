package br.com.proximojogo.proximojogo.ui;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.helper.HelperAgenda;
import br.com.proximojogo.proximojogo.helper.HelperTime;
import br.com.proximojogo.proximojogo.utils.LimparCamposFormulario;


public class TimeFragment extends Fragment  implements View.OnClickListener {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference testeReferencia = databaseReference.child("Times");

    private EditText nomeTime;
    private EditText responsavel;
    private EditText telefone;
    private Activity activity;




    private ImageButton btSalvar;
    private TimeFragment helper;
    private Handler handler;
    private View timeView;


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

//        handler = new AgendaFragment.AgendaHandler(this);
//        agendaView = inflater.inflate(R.layout.fragment_agenda, container, false);
//        helper = new HelperAgenda(agendaView, handler);


        btSalvar = (ImageButton) timeView.findViewById(R.id.bt_salvar_);
        btSalvar.setOnClickListener(this);

        btExcluir = (ImageButton) agendaView.findViewById(R.id.bt_excluir_agenda);
        btExcluir.setOnClickListener(this);

        btListar = (ImageButton) agendaView.findViewById(R.id.bt_listar_agenda);
        btListar.setOnClickListener(this);

        return timeView;
    }


    public void salvarTime(View v) {
        try {
            boolean salvou = LimparCamposFormulario.validaEditTextVazio((ViewGroup) this.getView());
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

    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public DatabaseReference getTesteReferencia() {
        return testeReferencia;
    }

    public void setTesteReferencia(DatabaseReference testeReferencia) {
        this.testeReferencia = testeReferencia;
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

    @Override
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Button getBtSalvar() {
        return btSalvar;
    }

    public void setBtSalvar(Button btSalvar) {
        this.btSalvar = btSalvar;
    }

    public TimeFragment getHelper() {
        return helper;
    }

    public void setHelper(TimeFragment helper) {
        this.helper = helper;
    }
}
