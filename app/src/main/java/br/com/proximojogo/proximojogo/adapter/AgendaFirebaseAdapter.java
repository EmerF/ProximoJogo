package br.com.proximojogo.proximojogo.adapter;

/**
 * Created by emerson on 04/07/16.
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the ExampleObject class to encapsulate the
 * data for each individual chat message


public class AgendaFirebaseAdapter extends FirebaseListAdapter {
    ListView  agendas;
    Firebase mRef = new Firebase("https://<yourapp>.firebaseio.com");
    FirebaseListAdapter<Agenda> adapter = new FirebaseListAdapter<Agenda>(
            ListaAgenda.class, Agenda.class, android.R.layout.two_line_list_item, mRef)
    {
        @Override
        protected void populateView(View v, Agenda agenda, int position) {
            ((TextView)v.findViewById(android.R.id.text1)).setText(agenda.getEvento().toString());
            ((TextView)v.findViewById(android.R.id.text2)).setText(agenda.getArena().toString());

        }


    };


    @Override
    protected void populateView(View v, Object model, int position) {

    }

    @Override
    public Object getItem(int position) {
            Log.i("XXX", "getItem() was called");


            // put all (parsed) list items into an array

            ArrayList<Agenda> temp_person_list = new ArrayList();

            for (int i = 1; i<=(mSnapshots.getCount()); i++){

                temp_person_list.add((Person) parseSnapshot(mSnapshots.getItem(i-1)));

            }

            // sort the array based on distance property
            Collections.sort(temp_person_list);


            // return the originally desired position
            return (T) temp_person_list.get(position);
    }
}
 */