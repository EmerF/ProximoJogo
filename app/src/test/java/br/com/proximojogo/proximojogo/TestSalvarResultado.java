package br.com.proximojogo.proximojogo;

import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import br.com.proximojogo.proximojogo.entity.Resultado;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestSalvarResultado extends ApplicationTestCase<MyApplication>{

    private static MyApplication application;
    public TestSalvarResultado(Class<TestSalvarResultado> applicationClass) {
        super(MyApplication.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (application == null) {
            application = getApplication();
        }
        if (application == null) {
            application = (MyApplication) getContext().getApplicationContext();
            assertNotNull(application);
            long start = System.currentTimeMillis();
           /* while (!application.){
                Thread.sleep(300);  //wait until FireBase is totally initialized
                if ( (System.currentTimeMillis() - start ) >= 1000 )
                    throw new TimeoutException(this.getClass().getName() +"Setup timeOut");
            }*/
        }
    }
    @Test
    public void salvarResultado() throws Exception {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseAgenda = database.getReference("resultados");
            try {
                //FirebaseApp.initializeApp();
                //mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("resultados");
                Resultado resultado= pegaResultado();
                if (resultado.getIdResultado().equals("")) {
                    //getUser id do Firebase para setar na agenda
                    // e colocar o nome junto com o id para identificar o nó
                    String key = mDatabaseAgenda.push().getKey();
                    resultado.setIdResultado(key);
                    // substituir pelo id do usuário qdo o login estiver pronto
                    //mDatabaseAgenda.child(agenda.getIdAgenda()).setValue(agenda);

                }
                mDatabaseAgenda.child(resultado.getIdResultado()).setValue(resultado);
            }catch (Exception e){
                e.printStackTrace();
                Log.d("Erro", e.getMessage());
            }



        }

    private Resultado pegaResultado() {
        Resultado resultado = new Resultado();
        resultado.setTime1("Audax");
        resultado.setTime2("Atentados");
        resultado.setGols1("3");
        resultado.setGols2("2");
        return resultado;
    }



}