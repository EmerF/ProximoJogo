package br.com.proximojogo.proximojogo.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

/**
 * Created by emer on 22/08/17.
 * Classe que vai retornar o id do usuário logado no App
 */

public class GetUser  implements Serializable{
    private static  FirebaseAuth mAuth;

    public static String getUserLogado(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //return "Txr5w0STR5YX2r9QjGuqab0KOB13";
        return currentUser.getUid();
    }


}
