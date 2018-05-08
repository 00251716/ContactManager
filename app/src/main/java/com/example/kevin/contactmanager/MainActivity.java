package com.example.kevin.contactmanager;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kevin.contactmanager.Adapters.RecyclerViewAdapter;
import com.example.kevin.contactmanager.Adapters.ViewPagerAdapter;
import com.example.kevin.contactmanager.Fragments.ContactFragment;
import com.example.kevin.contactmanager.Fragments.FavFragment;
import com.example.kevin.contactmanager.Fragments.NewContactDialog;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    private static AppCompatActivity instance;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private ContactFragment contactFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);


        instance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mostramos el diálogo respectivo
                NewContactDialog dialog = new NewContactDialog();
                dialog.show(getFragmentManager(), "123");
            }
        });


        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        //-------------- Testing ------------------
        fragmentManager = getSupportFragmentManager();
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("myContent")){
                String content = savedInstanceState.getString("myContent");
                if(content.equals(ContactFragment.TAG)){
                    //Referencia
                    contactFragment = (ContactFragment) fragmentManager.findFragmentByTag(contactFragment.TAG);
                    mViewPagerAdapter.AddFragment(contactFragment, "Contacts");
                    mViewPagerAdapter.AddFragment( new FavFragment(), "Favorites");

                    mViewPager.setAdapter(mViewPagerAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);

                    //mTabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
                    mTabLayout.getTabAt(0).setIcon(R.drawable.ic_group);
                    mTabLayout.getTabAt(1).setIcon(R.drawable.ic_star);
                } else {
                    contactFragment = new ContactFragment();
                    mViewPagerAdapter.AddFragment(contactFragment, "Contacts");
                    mViewPagerAdapter.AddFragment( new FavFragment(), "Favorites");

                    mViewPager.setAdapter(mViewPagerAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);

                    //mTabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
                    mTabLayout.getTabAt(0).setIcon(R.drawable.ic_group);
                    mTabLayout.getTabAt(1).setIcon(R.drawable.ic_star);
                }
            }
        } else {
            contactFragment = new ContactFragment();
            mViewPagerAdapter.AddFragment(contactFragment, "Contacts");
            mViewPagerAdapter.AddFragment( new FavFragment(), "Favorites");

            mViewPager.setAdapter(mViewPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);

            //mTabLayout.getTabAt(0).setIcon(R.drawable.ic_call);
            mTabLayout.getTabAt(0).setIcon(R.drawable.ic_group);
            mTabLayout.getTabAt(1).setIcon(R.drawable.ic_star);
        }

        //-------------- Testing ------------------




        //Removiendo la sombra
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setElevation(0);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    //útil
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("myContent", ContactFragment.TAG);
    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(fragmentManager.getBackStackEntryCount()>0);
    }
}
