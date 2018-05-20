package com.example.kevin.contactmanager.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin.contactmanager.Controllers.ContactController;
import com.example.kevin.contactmanager.R;


import java.util.ArrayList;

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ContactController.Contact> mData;
    Dialog myDialog; //Este es el dialog que se abre al hacer clic sobre un contacto
    ArrayList<ContactController.Contact> itemsCopy = new ArrayList<>();

    public RecyclerViewAdapter(Context mContext, ArrayList<ContactController.Contact> mData){
        this.mContext = mContext;
        this.mData = mData;
        itemsCopy.addAll(ContactController.getInstance().getContactList());
    }

    public RecyclerViewAdapter(){

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        myDialog = new Dialog(mContext); //Inicializamos el diálogo con el contexto como argumento del constructor
        myDialog.setContentView(R.layout.contact_dialog); //Le asociamos el layout respectivo
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.contactItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView dName = myDialog.findViewById(R.id.dName);
                final TextView dPhone = myDialog.findViewById(R.id.dPhone);
                final ImageView dImg = myDialog.findViewById(R.id.dImg);
                final TextView dEmail = myDialog.findViewById(R.id.dEmail);
                final TextView dAddress = myDialog.findViewById(R.id.dAddress);

                dName.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dPhone.setText(mData.get(vHolder.getAdapterPosition()).getPhoneNumber1());
                dImg.setImageBitmap(mData.get(vHolder.getAdapterPosition()).getPhoto());
                dEmail.setText(mData.get(vHolder.getAdapterPosition()).getEmail());
                dAddress.setText(mData.get(vHolder.getAdapterPosition()).getAddress());

                Button dCallBtn = (Button) myDialog.findViewById(R.id.dCallBtn);
                dCallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                myDialog.show();
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewName.setText(mData.get(position).getName());
        holder.textViewPhone.setText(mData.get(position).getPhoneNumber1());
        holder.img.setImageBitmap(mData.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //Este método sirve para filtrar resultados del buscador
    public void filter(String text) {
        mData.clear();
        if(text.isEmpty()){
            mData.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for(ContactController.Contact c : itemsCopy) {
                if(c.getName().toLowerCase().contains(text)) mData.add(c);
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<ContactController.Contact> getOriginal(){
        return itemsCopy;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout contactItem;
        private TextView textViewName;
        private TextView textViewPhone;
        private ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            contactItem = itemView.findViewById(R.id.contactItemLayout);
            textViewName = itemView.findViewById(R.id.ContactName);
            textViewPhone = itemView.findViewById(R.id.ContactPhone);
            img = itemView.findViewById(R.id.ContactImg);

        }
    }

}
