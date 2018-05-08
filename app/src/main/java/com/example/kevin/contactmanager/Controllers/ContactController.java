package com.example.kevin.contactmanager.Controllers;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.kevin.contactmanager.MainActivity;
import com.example.kevin.contactmanager.DesignUtils.IconDrawer;
import com.example.kevin.contactmanager.Utils.PermissionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ContactController{

    public static final ContactController ourInstance = new ContactController();
    private ArrayList<Contact> mContactList;
    private ContentResolver contentResolver;
    private Contact dummyContact; //Variable para añadir contactos a la lista
    Context context = MainActivity.getContext();

    public ArrayList<Contact> getContactList() {
        return  mContactList;
    }


    public static ContactController getInstance() {
        return ourInstance;
    }

    //La lista necesita ser reordenada cuando se añade un nuevo contacto
    public void sortList(){
        Collections.sort(mContactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1, Contact contact2) {
                return contact1.getName().compareTo(contact2.getName());
            }
        });
    }

    private ContactController() {
            loadContacts();
    }

    public void loadContacts(){
        mContactList = new ArrayList<Contact>();
        String sort = ContactsContract.Contacts.DISPLAY_NAME + " ASC"; //Orden en el que aparecerán los nombres
        contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER
                }, null, null, sort);

        //Si el cursor no es nulo y hay más de un contacto, iteramos
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()) {
                dummyContact = new Contact();
                dummyContact.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                dummyContact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                int hasPhoneNumber = Integer.valueOf(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if(hasPhoneNumber > 0) retrieveContactPhoneNumber(dummyContact);
                retrieveContactBirthday(dummyContact);
                retrieveThumbnailPhoto(dummyContact);
                retrieveEmail(dummyContact);
                retrieveAddress(dummyContact);
                mContactList.add(dummyContact);
            }
        }
        cursor.close();


    }

    //Clase interna para definir la interfaz de un usuario
    public static class Contact {

        private String mId;
        private String mName;
        private String mPhoneNumber1;
        private String mPhoneNumber2;
        private String mPhoneNumber3;
        private String mEmail;
        private String mBirthday;
        private String mAddress;
        private Bitmap mPhoto;

        public Contact(){
            mId = " ";
            mName = " ";
            mPhoneNumber1 = " ";
            mPhoneNumber2 = " ";
            mPhoneNumber3 = " ";
            mEmail = " ";
            mBirthday = " ";
            mAddress = " ";
        }

        public String getId() {
            return mId;
        }

        public void setId(String mId) {
            this.mId = mId;
        }

        public String getName() {
            return mName;
        }

        public void setName(String mName) {
            this.mName = mName;
        }

        public String getPhoneNumber1() {
            return mPhoneNumber1;
        }

        public void setPhoneNumber1(String mPhone) {
            this.mPhoneNumber1 = mPhone;
        }

        public String getPhoneNumber2() {
            return mPhoneNumber2;
        }

        public void setPhoneNumber2(String mPhone2) {
            this.mPhoneNumber2 = mPhone2;
        }

        public String getPhoneNumber3() {
            return mPhoneNumber3;
        }

        public void setPhoneNumber3(String mPhone3) {
            this.mPhoneNumber3 = mPhone3;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String mEmail) {
            this.mEmail = mEmail;
        }

        public String getBirthday() {
            return mBirthday;
        }

        public void setBirthday(String mBirthday) {
            this.mBirthday = mBirthday;
        }

        public String getAddress() {
            return mAddress;
        }

        public void setAddress(String mAddress) {
            this.mAddress = mAddress;
        }

        public Bitmap getPhoto() {
            return mPhoto;
        }

        public void setPhoto(Bitmap mPhoto) {
            this.mPhoto = mPhoto;
        }



    }

    //Métodos para obtener los datos
    private void retrieveContactPhoneNumber(Contact contact) {
        String phoneNumber;
        Cursor cursor  = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.RawContacts.ACCOUNT_TYPE + " <> ?",
                new String[]{contact.getId(), "com.whatsapp"},
                null
        );
        for (int i = 1; cursor.moveToNext() && i <= 3; i++) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (i == 1) contact.setPhoneNumber1(phoneNumber);
            else if (i == 2) contact.setPhoneNumber2(phoneNumber);
            else if (i == 3) contact.setPhoneNumber3(phoneNumber);
        }
        cursor.close();
    }

    private void retrieveContactBirthday(Contact contact) {
        String birthday = "";
        Cursor cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.CommonDataKinds.Event.START_DATE},
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                        ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                new String[]{
                        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                        contact.getId()
                },
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            birthday = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
        }
        cursor.close();
        contact.setBirthday(birthday);
    }

    private void retrieveThumbnailPhoto(Contact contact) {
        byte[] ByteSequence = new byte[0];
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contact.getId()));
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = contentResolver.query(
                photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO},
                null, null, null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ByteSequence = cursor.getBlob(0);
        }
        cursor.close();

        Bitmap photo = BitmapFactory.decodeByteArray(ByteSequence, 0, ByteSequence.length);

        if(photo == null){

            String name = contact.getName();
            String firstLetter;

            firstLetter = name != null && !name.isEmpty() ? name.substring(0,1) : "" ;

            //Sacamos un color aleatorio
            int randomColor = IconDrawer.getRandomColor();

            photo = IconDrawer.generateCircleBitmap(context, randomColor, 50, firstLetter);
        }
        contact.setPhoto(photo);
    }

    private void retrieveEmail(Contact contact) {
        String email = "";
        Cursor cursor  = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                new String[]{contact.getId()},
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        }
        cursor.close();
        contact.setEmail(email);
    }

    private void retrieveAddress(Contact contact) {
        String address = "";
        Cursor cursor  = contentResolver.query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS
                },
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + "=?",
                new String[]{contact.getId()},
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            address = cursor.getString(0);
        }
        cursor.close();
        contact.setAddress(address);
    }
}

