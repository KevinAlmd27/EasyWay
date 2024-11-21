package com.example.easyway.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.easyway.R;

public class CustomBaseAdapter extends RecyclerView.Adapter<CustomBaseAdapter.ViewHolder> {

    private Context context;
    private String[] caminhoes;
    private String[] placas;
    private int[] caminhoesImages;
    private OnItemClickListener onItemClickListener;

    // Construtor
    public CustomBaseAdapter(Context context, String[] caminhoes, String[] placas, int[] caminhoesImages, OnItemClickListener listener) {
        this.context = context;
        this.caminhoes = caminhoes;
        this.placas = placas;
        this.caminhoesImages = caminhoesImages;
        this.onItemClickListener = listener;
    }

    // Método para criar os itens do RecyclerView (ViewHolder)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main_caminhao, parent, false);
        return new ViewHolder(view); // Criar e retornar o ViewHolder
    }

    // Método para associar os dados aos itens da lista
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nomeCaminhao.setText(caminhoes[position]);
        holder.placaCaminhao.setText(placas[position]);
        holder.imagemCaminhao.setImageResource(caminhoesImages[position]);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    // Método para retornar a quantidade de itens
    @Override
    public int getItemCount() {
        return caminhoes.length;
    }

    // ViewHolder é uma classe interna para reter as referências de cada item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomeCaminhao, placaCaminhao;
        ImageView imagemCaminhao;

        public ViewHolder(View itemView) {
            super(itemView);
            nomeCaminhao = itemView.findViewById(R.id.caminhao_nome);
            placaCaminhao = itemView.findViewById(R.id.caminhao_placa); // Placa do caminhão
            imagemCaminhao = itemView.findViewById(R.id.caminhao_image);
        }
    }

    // Interface para o clique nos itens
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
