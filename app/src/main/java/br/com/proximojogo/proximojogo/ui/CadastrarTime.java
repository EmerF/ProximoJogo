package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.utils.LimparCamposFormulario;


public class CadastrarTime extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference testeReferencia = databaseReference.child("testes");

    private EditText nomeTime;
    private EditText responsavel;
    private EditText telefone;



    private Button btSalvar;
    private  salvou;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_cadastrar_time, container, false);
        // Inflate the layout for this fragment
        nomeTime = (EditText) inflate.findViewById(R.id.nome_time);
        responsavel = (EditText) inflate.findViewById(R.id.responsavel_time);
        responsavel = (EditText) inflate.findViewById(R.id.telefone_responsavel_time);

        btSalvar = (Button) inflate.findViewById(R.id.bt_salvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar(nomeTime.getText().toString());
            }
        });

        return inflate;
    }

    private void salvar(String s) {
        String key = testeReferencia.push().getKey();
        //teste firebase
        testeReferencia.child(key).setValue(s);
        nomeTime.setText("");
        Toast.makeText(getActivity(), "Salvou a bagaça!", Toast.LENGTH_SHORT).show();
    }

    public void salvarTime(View v) {
        try {
            boolean salvou = LimparCamposFormulario.validaEditTextVazio((ViewGroup) this.getView());
            if(!salvou){
                //Toast.makeText(activity,"Preencher os campos obrigaórios, por favor !",Toast.LENGTH_SHORT).show();
                ExibirToast.ExibirToastComIcone(activity,R.drawable.alerta,R.color.colorRed,"Preencha os campos, meu Bem!");


            }else {
                helper.salvar(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
