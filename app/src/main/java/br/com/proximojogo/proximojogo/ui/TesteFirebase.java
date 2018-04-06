package br.com.proximojogo.proximojogo.ui;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEstatiscaJogosPorData;
import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static br.com.proximojogo.proximojogo.conexao.constantes.dao.ConstantesDAO.TOKENS_FCM;

public class TesteFirebase extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference testeReferencia = databaseReference.child(TOKENS_FCM);
    private EditText edeste;
    private Button btSalvar;
    OkHttpClient mClient = new OkHttpClient();

    String refreshedToken = "";//add your user refresh tokens who are logged in with firebase.

    JSONArray jsonArray = new JSONArray();
    private List<String> estatisticas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_teste_firebase, container, false);
        // Inflate the layout for this fragment
        listaTokens();
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
//        String token = FirebaseInstanceId.getInstance().getToken();
//        String token = "esjLOOCPgCA:APA91bFJWHhfVuKZsVvzQ9Un0WI2zyUQZ1Zv5_4bEw5eKiAPMfkkjeOsrVQZNP8kyeIImvqRBOWCVXcVic1EzO-TEewKFu9BPqqNNio3Bb682U5Dcq0GHra6zWq4swkHWcCGN_l_Rx7x";
//        jsonArray.put(token);
        for (String t : estatisticas) {
            jsonArray.put(t);

        }
        sendMessage(jsonArray, "Hello", s, "Http:\\google.com", "My Name is Vishal");

    }


    @SuppressLint("StaticFieldLeak")
    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon, final String message) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.d("Teste FCM", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(getContext(), "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {


        final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AIzaSyAPwlg6HEIxh_2K-F5YtSpAM9xrW07Wyvo")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

    public void listaTokens() {
        testeReferencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);//Passar os dados para a interface grafica
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Se ocorrer um erro
            }
        });
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        estatisticas.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String esj = ds.getValue(String.class);
            estatisticas.add(esj);
        }


    }
}
