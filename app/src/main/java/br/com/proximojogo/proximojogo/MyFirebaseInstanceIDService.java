package br.com.proximojogo.proximojogo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.joda.time.LocalDate;

import br.com.proximojogo.proximojogo.utils.GetUser;

import static br.com.proximojogo.proximojogo.conexao.constantes.dao.ConstantesDAO.AGENDA_DAO;
import static br.com.proximojogo.proximojogo.conexao.constantes.dao.ConstantesDAO.TOKENS_FCM;

/**
 * Created by ale on 09/03/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    private static final String FIREBASE_TOKEN = "Token Firebase";
    private DatabaseReference mDatabaseReference;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        if(refreshedToken != null){
            sendRegistrationToServer(refreshedToken);
        }
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        // Instance ID token to your app server.
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        preferences.edit().putString(FIREBASE_TOKEN, token).apply();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(TOKENS_FCM);
        String key = mDatabaseReference.push().getKey();
        mDatabaseReference.child(token).setValue(token);
    }
}
