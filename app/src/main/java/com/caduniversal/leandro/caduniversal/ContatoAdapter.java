package com.caduniversal.leandro.caduniversal;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContactViewHolder>{

    private List<ContatoInfo> listaContatos;

    ContatoAdapter(List<ContatoInfo> lista){
        listaContatos = lista;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_contato, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ContatoInfo c = listaContatos.get(position);
        holder.nome.setText(c.getNome());
        holder.ref.setText(c.getRef());
        holder.fone.setText(c.getFone());
        holder.end.setText(c.getEnd());
        holder.dta.setText(c.getEmail());

        File imgFile = new File(c.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.foto.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nome;
        TextView ref;
        TextView fone;
        TextView end;
        TextView dta;

        ContactViewHolder(View v){
            super(v);
            foto = v.findViewById(R.id.imageFoto);
            nome = v.findViewById(R.id.textoNome);
            ref = v.findViewById(R.id.textoRef);
            fone = v.findViewById(R.id.textoFone);
            end = v.findViewById(R.id.textoend);
            dta = v.findViewById(R.id.dateCelula);
        }

    }

}
