package com.app.pokeapp.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.Button;
import com.app.pokeapp.R;
import com.app.pokeapp.db.ChallengerSQLiteHelper;
import com.app.pokeapp.db.PokemonSQLiteHelper;
import com.app.pokeapp.db.RandomSQLiteHelper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setButtonsColour(setThemeColour());


        dropPokemonTable(); // TODO remove
        dropChallengerTable(); // TODO remove
        dropRandomTable(); // TODO remove
        initializeDB();
        setButtonListenerPokedex();
        setButtonListenerTrainers();
        setButtonListenerChallengers();
        setButtonListenerRandoms();
    }

    private void dropChallengerTable() {
        ChallengerSQLiteHelper dbHelper = new ChallengerSQLiteHelper(this);
        dbHelper.dropTable();
        dbHelper.close();
    }

    private void dropRandomTable() {
        RandomSQLiteHelper dbHelper = new RandomSQLiteHelper(this);
        dbHelper.dropTable();
        dbHelper.close();
    }



    private void setButtonsColour(int color) {
        findViewById(R.id.pokedex_btn).setBackgroundTintList(ColorStateList.valueOf(color));
        findViewById(R.id.fight_btn).setBackgroundTintList(ColorStateList.valueOf(color));
        findViewById(R.id.big_fight_btn).setBackgroundTintList(ColorStateList.valueOf(color));
        findViewById(R.id.random_fight_btn).setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private int setThemeColour() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    private void dropPokemonTable() {
        PokemonSQLiteHelper dbHelper = new PokemonSQLiteHelper(this);
        dbHelper.dropPokemonTable();
        dbHelper.close();
    }

    private void initializeDB() {
        initializePokemonDB();
        initializeChallengersDB();
        initializeRandomsDB();
    }

    private void initializeChallengersDB() {
        ChallengerSQLiteHelper chaHelper = new ChallengerSQLiteHelper(this);
        if (chaHelper.getAllChallengers()
                     .isEmpty()) {
            chaHelper.insert();
        }
        chaHelper.close();
    }

    private void initializePokemonDB() {
        PokemonSQLiteHelper pkmHelper = new PokemonSQLiteHelper(this);
        if (pkmHelper.getAllPokemonFromDB()
                     .isEmpty()) {
            pkmHelper.insertFirstGen();
        }
        pkmHelper.close();
    }

    private void initializeRandomsDB() {
        RandomSQLiteHelper helper = new RandomSQLiteHelper(this);
        if (helper.getAllRandoms()
                .isEmpty()) {
            helper.insertRandoms();
        }
        helper.close();
    }

    private void setButtonListenerTrainers() {
        Button btn = findViewById(R.id.fight_btn);
        btn.setOnClickListener(clic -> startActivity(new Intent(MainActivity.this, TrainersFightActivity.class)));
    }

    private void setButtonListenerPokedex() {
        Button btn = findViewById(R.id.pokedex_btn);
        btn.setOnClickListener(clic -> startActivity(new Intent(MainActivity.this, PokedexActivity.class)));
    }

    private void setButtonListenerChallengers() {
        Button btn = findViewById(R.id.big_fight_btn);
        btn.setOnClickListener(clic -> startActivity(new Intent(MainActivity.this, ChallengersListActivity.class)));
    }

    private void setButtonListenerRandoms() {
        Button btn = findViewById(R.id.random_fight_btn);
        btn.setOnClickListener(clic -> startActivity(new Intent(MainActivity.this, RandomsListActivity.class)));
    }
}