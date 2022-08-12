package com.app.pokeapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.pokeapp.R;
import com.app.pokeapp.db.PokemonSQLiteHelper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropPokemonTable(); // TODO remove
        initializeDb();
        setButtonListenerPokedex();
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
        Button btn = (Button)findViewById(R.id.pokedex_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PokedexActivity.class));
            }
        });
    }


}