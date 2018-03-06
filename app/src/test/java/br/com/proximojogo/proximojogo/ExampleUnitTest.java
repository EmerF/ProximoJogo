package br.com.proximojogo.proximojogo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEstatiscaJogosPorData;
import br.com.proximojogo.proximojogo.ordenacao.OrdenaEventoTimeData;
import br.com.proximojogo.proximojogo.service.VerificaEventosService;
import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;
import br.com.proximojogo.proximojogo.utils.FormatarData;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static final String ASSET_BASE_PATH = "./app/src/main/assets/";


    @Test
    public void teste() throws Exception {
        List<AgendaDO> agendaDOS = geraListaEventos();
        List<List<AgendaDO>> listaDeLista = ordenaListAdversarioDataEmLista(agendaDOS);
        for (List<AgendaDO> list : listaDeLista ) {
            System.out.println(list.get(0).getAdversario());
            for (AgendaDO a: list ) {
                System.out.println(FormatarData.getDf().format(a.getData()));
            }
        }
    }
    @Test
    public void consultaBase() throws Exception {
        List<AgendaDO> agendaDOS = geraListaEventos();
      /*  for (AgendaDO a : agendaDOS
                ) {
            System.out.println(FormatarData.getDf().format(a.getData()));
            System.out.println(a);

        }*/
        List<EstatisticaDeJogos> agendaDOS1 = ordenaListAdversarioData(agendaDOS);
        Collections.sort(agendaDOS1,new OrdenaEstatiscaJogosPorData());
        for (EstatisticaDeJogos a : agendaDOS1
                ) {
            System.out.println(FormatarData.getDf().format(a.getDataUltimoComfronto()));
            System.out.println(a);

        }

    }
    public List<List<AgendaDO>> ordenaListAdversarioDataEmLista(List<AgendaDO> list) {
        Collections.sort(list, new OrdenaEventoTimeData());
        List<List<AgendaDO>> listaDeLista = new ArrayList<>();
        if (!list.isEmpty()) {
            List<AgendaDO>listTime = new ArrayList<>();
            AgendaDO anterior = list.get(0);
            listTime.add(anterior);
            for (int i = 1; i < list.size(); i++) {
                if (anterior.getAdversario().equals(list.get(i).getAdversario())) {
                    listTime.add(list.get(i));
                }else{
                    listaDeLista.add(listTime);
                    listTime = new ArrayList<>();
                    anterior = list.get(i);
                    listTime.add(anterior);
                }
                listaDeLista.add(listTime);
            }
        }

        return listaDeLista;

    }

    public List<EstatisticaDeJogos> ordenaListAdversarioData(List<AgendaDO> list) {
        Collections.sort(list, new OrdenaEventoTimeData());
        List<EstatisticaDeJogos> listEstatistica = new ArrayList<>();
        if (!list.isEmpty()) {
            AgendaDO anterior = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                if (!anterior.getAdversario().equals(list.get(i).getAdversario())) {
                    listEstatistica.add(new EstatisticaDeJogos(anterior.getData(), anterior.getTimes(), anterior.getAdversario(), anterior.getObservacao(),0));
                    anterior = list.get(i);
                }
            }
        }

        return listEstatistica;

    }

    class EstatisticaJogos {

        public EstatisticaJogos(Long dataUltimoComfronto, String time1, String time2, String obs) {
            this.dataUltimoComfronto = dataUltimoComfronto;
            this.time1 = time1;
            this.time2 = time2;
            this.obs = obs;
        }

        private Long dataUltimoComfronto;
        private String time1;
        private String time2;
        private Integer vitoriasTime1;
        private Integer vitoriasTime2;
        private Integer derrotasTime1;
        private Integer derrotasTime2;
        private Integer empates;
        private Integer golsTime1;
        private Integer golsTime2;
        private String obs;

        public Long getDataUltimoComfronto() {
            return dataUltimoComfronto;
        }

        public void setDataUltimoComfronto(Long dataUltimoComfronto) {
            this.dataUltimoComfronto = dataUltimoComfronto;
        }

        public String getTime1() {
            return time1;
        }

        public void setTime1(String time1) {
            this.time1 = time1;
        }

        public String getTime2() {
            return time2;
        }

        public void setTime2(String time2) {
            this.time2 = time2;
        }

        public String getObs() {
            return obs;
        }

        public void setObs(String obs) {
            this.obs = obs;
        }

        @Override
        public String toString() {
            return "EstatisticaJogos{" +
                    "dataUltimoComfronto=" + dataUltimoComfronto +
                    ", time1='" + time1 + '\'' +
                    ", time2='" + time2 + '\'' +
                    ", obs='" + obs + '\'' +
                    '}';
        }
    }

    public List<AgendaDO> geraListaEventos() {
        Gson gson = new Gson();
        List<AgendaDO> list = new ArrayList<>();

        JsonElement jOb = lerJson();
        JsonObject jOb2 = jOb.getAsJsonObject();
        JsonObject jOb3 = jOb2.get("agendas").getAsJsonObject();
        JsonArray jOb4 = jOb3.get("Txr5w0STR5YX2r9QjGuqab0KOB13").getAsJsonArray();
        for (JsonElement o : jOb4
                ) {
            list.add(gson.fromJson(o, AgendaDO.class));
        }
        return list;
    }

    /**
     * Acha o arquivo local simulando a base json
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public String readJsonFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ASSET_BASE_PATH + filename)));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        return sb.toString();
    }

    /**
     * Devolve a base Json encontrata no formato certo do Json
     *
     * @return
     */
    public JsonElement lerJson() {
        Gson gson = new GsonBuilder().create();
        try {
            String s = readJsonFile("baseTeste.json");
//            JsonParser parser = new JsonParser();
//            JsonObject o = parser.parse(s).getAsJsonObject();
            //gson.toJson(s);
            return gson.fromJson(s, JsonElement.class);
        } catch (IOException e) {
            e.printStackTrace();
            //retorna vazio se não achar nada
            return new JsonObject();
        }
    }

}