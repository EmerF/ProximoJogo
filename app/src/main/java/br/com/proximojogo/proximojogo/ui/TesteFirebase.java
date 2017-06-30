package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.proximojogo.proximojogo.R;

public class TesteFirebase extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference testeReferencia = databaseReference.child("testes");

    private EditText edeste;
    private Button btSalvar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_teste_firebase, container, false);
        // Inflate the layout for this fragment
        edeste = (EditText) inflate.findViewById(R.id.ed_teste);
        btSalvar = (Button) inflate.findViewById(R.id.bt_salvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar(edeste.getText().toString());
            }
        });

        return inflate;
    }

    private void salvar(String s) {
        String key = testeReferencia.push().getKey();
        //teste firebase
        testeReferencia.child(key).setValue(s);
        edeste.setText("");
        Toast.makeText(getActivity(), "Salvou a bagaça!", Toast.LENGTH_SHORT).show();
    }
}
