package com.example.kevin.contactmanager.Fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.contactmanager.Adapters.RecyclerViewAdapter;
import com.example.kevin.contactmanager.Controllers.ContactController;
import com.example.kevin.contactmanager.R;

import java.util.ArrayList;

public class ContactFragment extends Fragment {

    private View mContactView;
    private RecyclerView mRecyclerView;
    private ArrayList<ContactController.Contact> mContactList;
    private RecyclerViewAdapter recyclerAdapter;
    public static final String TAG = "ContactFragment";

    public ContactFragment(){
    }

    public RecyclerViewAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContactList = ContactController.ourInstance.getContactList();
        recyclerAdapter = new RecyclerViewAdapter(getContext(), mContactList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        mContactView = inflater.inflate(R.layout.contact_fragment, container, false);
        mRecyclerView = (RecyclerView) mContactView.findViewById(R.id.contactRecycleView);
        recyclerAdapter = new RecyclerViewAdapter(getContext(), mContactList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(recyclerAdapter);
        return mContactView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search,menu);

        final MenuItem searchItem = menu.findItem(R.id.item_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Buscar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerAdapter.filter(query);
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.filter(newText);
                return true;
            }
        });
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mContactList.clear();
        mContactList.addAll(recyclerAdapter.getOriginal());

    }
}
