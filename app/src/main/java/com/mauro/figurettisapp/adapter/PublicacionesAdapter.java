package com.mauro.figurettisapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mauro.figurettisapp.R;
import com.mauro.figurettisapp.model.Publicaciones;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Admin on 10/11/2017.
 */

public class PublicacionesAdapter extends RecyclerView.Adapter<PublicacionesAdapter.PublicacionesViewHolder> {
    private List<Publicaciones> publicaciones;
    private int rowLayout;
    private Context context;

    public static class PublicacionesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout publicacionesLayout;
        TextView nicknameUsuario;
        TextView numeroFigurita;
        TextView fechaHora;
        TextView cantidad;
        TextView estado;

        public PublicacionesViewHolder(View itemView) {
            super(itemView);
            publicacionesLayout = (LinearLayout) itemView.findViewById(R.id.publicaciones_layout);
            nicknameUsuario = (TextView) itemView.findViewById(R.id.nicknameUsuario);
            numeroFigurita = (TextView) itemView.findViewById(R.id.numeroFigurita);
            fechaHora = (TextView) itemView.findViewById(R.id.fechaHora);
            cantidad = (TextView) itemView.findViewById(R.id.cantidad);
            estado = (TextView) itemView.findViewById(R.id.estado);
        }
    }

    public PublicacionesAdapter(List<Publicaciones> publicaciones, int rowLayout, Context context) {
        this.publicaciones = publicaciones;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public PublicacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PublicacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PublicacionesViewHolder holder,final int position) {
        holder.nicknameUsuario.setText(publicaciones.get(position).getNicknameUsuario());
        holder.numeroFigurita.setText(publicaciones.get(position).getNumeroFigurita().toString());
        holder.fechaHora.setText(publicaciones.get(position).getFechaHora());
        holder.estado.setText(publicaciones.get(position).getEstado().toString());
        holder.cantidad.setText(publicaciones.get(position).getCantidad().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*
                Iniciar nueva actividad al clickear una publicacion.
                 */
                Toast.makeText(getApplicationContext(), "Clickeo una publicacion", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
            return publicaciones.size();
//            return 0;
    }
}
