package com.equipo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo.Fragmentos.*;
import com.equipo.compartidos.HistorialesComportidos;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;


public class FirstScreen extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    //
    String prueba = "";
    private int position_screen;

    // variables para el menu
    private static final int POS_CLOSE = 0;
    private static final int POS_DASHBOARD = 1;
    private static final int POS_MY_PROFILE = 2;
    private static final int POS_COMPARTIDOS = 3;
    private static final int POS_LOGOUT = 4;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slindingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);


        prueba = getIntent().getStringExtra("position");
///////////////////////////////////////
        ///tool bar

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Clinico");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleMarginStart(350);
        setSupportActionBar(toolbar);

        slindingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_CLOSE),
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_MY_PROFILE),
                createItemFor(POS_COMPARTIDOS),
                new SpaceItems(260),
                createItemFor(POS_LOGOUT)
        ));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_DASHBOARD);

    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.pink))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.pink))
                .withSelectedTextTint(color(R.color.pink));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];

        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        if (position_screen == POS_DASHBOARD) {
            finish();
        } else {
            onItemSelected(POS_DASHBOARD);
        }
    }

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        position_screen = position;

        if (position == POS_DASHBOARD) {
            pagina_principal page = new pagina_principal();
            transaction.replace(R.id.container, page);
            prueba = null;
        } else if (position == POS_MY_PROFILE) {
            fragment2 fragment2 = new fragment2();
            transaction.replace(R.id.container, fragment2);
            prueba = null;
        } else if (position == POS_COMPARTIDOS) {
            HistorialesComportidos compartidos = new HistorialesComportidos();
            transaction.replace(R.id.container, compartidos);
            prueba = null;
        }else if (position == POS_LOGOUT) {
            Intent intent = new Intent(FirstScreen.this, Log_in.class);
            startActivity(intent);
            finish();
            prueba = null;
        }
        slindingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
