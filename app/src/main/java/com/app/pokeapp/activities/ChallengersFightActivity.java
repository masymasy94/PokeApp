package com.app.pokeapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.Pokemon;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChallengersFightActivity extends AppCompatActivity {

    Pokemon myPokemon    = null;
    Pokemon enemyPokemon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenger_fight_activity);

        getIntent().getStringExtra("MyStudentObjectAsString");
    }


}
