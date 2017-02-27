package com.example.manuel.sudo;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;



public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private FragmentManager fragmentManager;
    private MenuItem mCurrItem;
    private MenuItem mCurrItem2;
    public int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mCurrItem = navigationView.getMenu().findItem(R.id.lv_mid);
        setFragment(mCurrItem, "Medium", null);

        mCurrItem2 = navigationView.getMenu().findItem(R.id.about);
        mCurrItem2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent=new Intent(MainActivity2.this,About.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setFragment(item, (String) item.getTitle(), mCurrItem);
        mDrawerLayout.closeDrawers();
        return true;
    }

    private void setFragment(MenuItem item, String title, @Nullable MenuItem oldItem) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldItem == item) {
            return;
        }
        if (oldItem != null) {
            ft.remove(getInstance(oldItem));
        }
        ft.replace(R.id.content, getInstance(item), (String) item.getTitle()).commit();
        item.setChecked(true);
        mCurrItem = item;
        setTitle(title);
    }

    @NonNull
    private Fragment getInstance(MenuItem item) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        Fragment fragment = fragmentManager.findFragmentByTag((String) item.getTitle());
        if (fragment == null) {
            fragment = GameFragment.newInstance("mid");

        }
        return fragment;
    }

}
