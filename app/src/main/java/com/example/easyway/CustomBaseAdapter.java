package com.example.easyway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyway.R;


public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String caminhoes[];
    int caminhoesImages[];
    LayoutInflater inflater;


    public CustomBaseAdapter(Context ctx, String[] caminhoes, int[] caminhoesImages) {
        this.context = ctx;
        this.caminhoes = caminhoes != null ? caminhoes : new String[0]; // Use o parâmetro
        this.caminhoesImages = caminhoesImages != null ? caminhoesImages : new int[0]; // Use o parâmetro
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return caminhoes.length;
    }

    @Override
    public Object getItem(int position) {return null;}

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_tela_inicial, parent, false);
        TextView txtView = (TextView) convertView.findViewById(R.id.textView);
        ImageView caminhoesImg = (ImageView) convertView.findViewById(R.id.caminhoesImg);
        txtView.setText(caminhoes[position]);
        caminhoesImg.setImageResource(caminhoesImages[position]);
        return convertView;
    }
}
