package com.example.kevin.contactmanager.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kevin.contactmanager.Controllers.ContactController;
import com.example.kevin.contactmanager.DesignUtils.IconDrawer;
import com.example.kevin.contactmanager.R;

import java.util.Arrays;
import java.util.Comparator;

public class NewContactDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_contact, null);

        //Obteniendo referencias de objetos en el heap
        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextPhone = dialogView.findViewById(R.id.editTextPhone);
        Button doneBtn = dialogView.findViewById(R.id.doneBtn);

        builder.setView(dialogView); //.setMessage("Your Note");



        //Lo que hay que hacer antes de irnos
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Primero creamos un nuevo contacto
                ContactController.Contact newContact = new ContactController.Contact();

                //Seteamos sus variables para matchear lo que el usuario ha puesto en la form
                newContact.setName(editTextName.getText().toString());
                newContact.setPhoneNumber1(editTextPhone.getText().toString());
                Bitmap image = IconDrawer.generateCircleBitmap(getActivity(), 0xFF3F51B5, 50, "E");
                newContact.setPhoto(image);

                //Pasamos los datos al controlador de contactos
                ContactController.ourInstance.getContactList().add(newContact);

                //Ahora hay que volver a ordenar la lista en base al nombre
                ContactController.ourInstance.sortList();

                dismiss();
            }
        });

        return builder.create();
    }

}
