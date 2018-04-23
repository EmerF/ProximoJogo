package br.com.proximojogo.proximojogo.helper;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.Jogador;
import br.com.proximojogo.proximojogo.entity.Time;
import br.com.proximojogo.proximojogo.utils.GetUser;


/**
 * Created by emer on 16/09/17.
 */

public class HelperTime {
    private static final String TAG = HelperTime.class.getName().toUpperCase();
    private final View viewAtiva;
    private EditText campoNomeTime;
    private EditText campoResponsavelTime;
    private EditText campoTelefoneResponsavel;
    private Handler handler = null;
    private Time time;
    private DatabaseReference mDatabaseAgenda;


    public HelperTime(View activity,Handler handler){
        this.handler = handler;
        inicializaCamposTela(activity);
        viewAtiva = activity;


    }


    public Time pegaTime(){
        time.setNomeTime(campoNomeTime.getText().toString());
        time.setResponsavelTime(campoResponsavelTime.getText().toString());
        time.setTelefoneResponsavel(campoTelefoneResponsavel.getText().toString());
        time.setIdResponsavel(GetUser.getUserLogado());

        return time;

    }

    public void inicializaCamposTela(View activity) {
        campoNomeTime = (EditText) activity.findViewById(R.id.nome_time);
        campoResponsavelTime= (EditText) activity.findViewById(R.id.responsavel_time);
        campoTelefoneResponsavel = (EditText) activity.findViewById(R.id.telefone_responsavel_time);

        time = new Time();

    }

    public void preencheFormulario(Time time) throws ParseException {
        if (time != null) {
            campoNomeTime.setText(time.getNomeTime());
            campoResponsavelTime.setText(time.getResponsavelTime());
            campoTelefoneResponsavel.setText(time.getTelefoneResponsavel());
            this.time = time;
        }

    }

    public void salvarTime(View activity) {

        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("Times");

        Time time = pegaTime();
        if (time.getIdTime() == null) {
            //getUser id do Firebase para setar na agenda
            // e colocar o nome junto com o id para identificar o nó
            String key = mDatabaseAgenda.push().getKey();
            time.setIdTime(key);

            try {
                mDatabaseAgenda.child(time.getIdResponsavel()+ "/" +  time.getIdTime()).setValue(time);
                try {
                    AtualizarJogadorParaAdm(activity);
                }catch (Exception e){
                    Log.d(TAG,"Erro atualizar User Para Administrador");
                }


            }catch (Exception e){
                Toast.makeText(activity.getContext(), "Erro ao cadastrar time: " + e.getCause(), Toast.LENGTH_SHORT).show();
            }


            Toast.makeText(activity.getContext(), "Time Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            mDatabaseAgenda.child(time.getIdResponsavel()+ "/" + time.getIdTime()).setValue(time);
            Toast.makeText(activity.getContext(), "Time Editado com Sucesso!", Toast.LENGTH_SHORT).show();
        }
    }
    public void AtualizarJogadorParaAdm(View activity){
        boolean salvou = false;
        Jogador Jogador = new Jogador();
        Jogador.set_idUser(GetUser.getUserLogado());
        Jogador.setTipoUser("Administrador");
        HelperJogador helperJogador = new HelperJogador();
        salvou = helperJogador.atualizarTipoUser(activity,Jogador);

    }


    public void excluir(View activity, String idTime) {
        try {
            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Times/" + GetUser.getUserLogado() + "/" + idTime);
            mDatabaseAgenda.removeValue();
            Toast.makeText(activity.getContext(), "Time apagado com Sucesso!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity.getContext(), "Erro ao Apagar Time!", Toast.LENGTH_SHORT).show();
        }
    }
}
