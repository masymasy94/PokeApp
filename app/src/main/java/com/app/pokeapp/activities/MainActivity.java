package com.app.pokeapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.Button;

import com.app.pokeapp.R;
import com.app.pokeapp.db.PokemonSQLiteHelper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

//    int THEME_COLOR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setButtonsColour(setThemeColour());


        dropPokemonTable(); // TODO remove
        initializeDb();
        setButtonListenerPokedex();
    }

    private void setButtonsColour(int color) {
        findViewById(R.id.pokedex_btn).setBackgroundTintList(ColorStateList.valueOf(color));
        findViewById(R.id.fight_btn).setBackgroundTintList(ColorStateList.valueOf(color));
        findViewById(R.id.big_fight_btn).setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private int setThemeColour(){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    private void dropPokemonTable() {
        PokemonSQLiteHelper dbHelper = new PokemonSQLiteHelper(this);
        dbHelper.dropPokemonTable();
        dbHelper.close();
    }

    private void initializeDb() {
        PokemonSQLiteHelper dbHelper = new PokemonSQLiteHelper(this);

        if (dbHelper.getAllPokemonFromDB().isEmpty()){
            dbHelper.insertFirstGen();
        }

        dbHelper.close();
    }


    private void setButtonListenerPokedex() {
        Button btn = findViewById(R.id.pokedex_btn);

        btn.setOnClickListener(clic -> startActivity(new Intent(MainActivity.this, PokedexActivity.class)));
    }

}