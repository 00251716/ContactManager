package com.example.kevin.contactmanager.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.contactmanager.R;

public class FavFragment extends Fragment {

    private View mFavView;

    public FavFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mFavView = inflater.inflate(R.layout.fav_fragment, container, false);
        return mFavView;
    }

}
