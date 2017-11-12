package com.mauro.figurettisapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    public static final String BASE_URL_FIGURETTIS = "http://192.168.1.106:80/Figurettis/";
    private List<Publicaciones> publicaciones;
    private int rowLayout;
    private Context context;

    public static class PublicacionesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout publicacionesLayout;
        ImageView imagen;
        TextView nombre;
        ImageView bandera;
        TextView numero;

        public PublicacionesViewHolder(View itemView) {
            super(itemView);
            publicacionesLayout = (LinearLayout) itemView.findViewById(R.id.publicaciones_layout);
            imagen = (ImageView) itemView.findViewById(R.id.imagen);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            bandera = (ImageView) itemView.findViewById(R.id.bandera);
            numero = (TextView) itemView.findViewById(R.id.numero);
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
//        holder.imagen.setText(publicaciones.get(position).getImagen());
        String rutaFotoPerfil = "";
        String rutaBandera = "";
        rutaFotoPerfil = rutaFotoPerfil + BASE_URL_FIGURETTIS;
        rutaBandera = rutaBandera + BASE_URL_FIGURETTIS;
        rutaFotoPerfil = rutaFotoPerfil + publicaciones.get(position).getImagen();
        rutaBandera = rutaBandera + publicaciones.get(position).getBandera();
        Glide.with(holder.imagen.getContext()).load(rutaFotoPerfil).into(holder.imagen);
//        Glide.with(getApplicationContext()).load(rutaFotoPerfil).into(holder.imagen);
        holder.nombre.setText(publicaciones.get(position).getNombre());
        String num = "Figurita N°: "+ publicaciones.get(position).getNumero().toString();
//        String cant = "Cantidad : "+ publicaciones.get(position).getCantidad().toString();
//        String bandera = publicaciones.get(position).getBandera();

        holder.numero.setTypeface(null, Typeface.BOLD_ITALIC);
        holder.numero.setText(num);
        Glide.with(holder.bandera.getContext()).load(rutaBandera).into(holder.bandera);


        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*
                Iniciar nueva actividad al clickear una publicacion.
                 */
                Toast.makeText(holder.itemView.getContext(), "Clickeo una publicacion", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
            return publicaciones.size();
//            return 0;
    }

//    public void updateList(List<Publicaciones> publicacionesUpdate, int rowLayoutUpdate, Context contextUpdate){
    public void updateList(List<Publicaciones> publicacionesUpdate){

        publicaciones = publicacionesUpdate;
//        rowLayout = rowLayoutUpdate;
//        context = contextUpdate;
//        notifyDataSetChanged();

    }
}
