package br.com.proximojogo.proximojogo.adapter;

/**
 * Created by emerson on 04/07/16.

public class AgendaAdapter extends BaseAdapter {
    private final Context context;
    private final List<Agenda> agendas;

    public AgendaAdapter(Context context, List<Agenda> agendas) {
        
        this.context = context;
        this.agendas = agendas;
    }

    @Override
    public int getCount() {
        return agendas.size();
    }

    @Override
    public Object getItem(int position) {
        return agendas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return agendas.get(position).getId();
        //return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Agenda agenda = agendas.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;

        if(convertView == null){
            view = inflater.inflate(R.layout.list_item,parent,false);
        }


        TextView campoEvento = (TextView) view.findViewById(R.id.item_evento);
        campoEvento.setText(agenda.getEvento().toString());

        TextView campoTelefone =(TextView) view.findViewById(R.id.item_data);

        campoTelefone.setText(agenda.getInicio().toString());

        /*
        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
        String caminhoFoto = aluno.getCaminhoFoto();
        if (caminhoFoto != null){
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap,100,100,true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);


        }


        return view;
    }
}
                */