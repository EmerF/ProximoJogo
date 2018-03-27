package br.com.proximojogo.proximojogo.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

/**
 * Created by emer on 22/08/17.
 * Classe que vai retornar o id do usuário logado no App
 */

public class GetUser  implements Serializable{
    private static FirebaseAuth mAuth;
    private static FirebaseUser currentUser;

    public static void getInstanceFirebase(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public static String getUserLogado(){
        getInstanceFirebase();

        //return "Txr5w0STR5YX2r9QjGuqab0KOB13";
        return currentUser.getUid();
    }
    public static String getNomeUserLogado(){
        getInstanceFirebase();
        return currentUser.getDisplayName();
    }

    public void getTelefoneUserLogado(){
        DatabaseReference mDatabaseAgenda;

    }




}
