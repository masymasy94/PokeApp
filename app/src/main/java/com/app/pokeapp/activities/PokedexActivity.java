package com.app.pokeapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.db.PokemonSQLiteHelper;

import java.util.Comparator;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PokedexActivity extends AppCompatActivity {

    LinearLayout ll = null;
    LinearLayout.LayoutParams lp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedex_activity);
        setInstanceValues();

        showPokemon();
    }

    private void setInstanceValues() {
        ll = (LinearLayout)findViewById(R.id.pokedex_ll);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showPokemon() {
        List<Pokemon> allPk = getAllPokemon();
        allPk.sort(Comparator.comparing(Pokemon::getName));
        allPk.forEach(this::addPokemonButton);

        if (allPk.isEmpty()){
            showNoPokemonFoundMessage();
        }
    }

    private void showNoPokemonFoundMessage() {
        TextView empty = new TextView(this);
        empty.setGravity(Gravity.CENTER);
        empty.setText("No pokemon found in database!");
        ll.addView(empty, lp);
    }

    private List<Pokemon> getAllPokemon() {
        PokemonSQLiteHelper db = new PokemonSQLiteHelper(this);
        List<Pokemon> allPk = db.getAllPokemon();
        db.close();
        return allPk;
    }

    private void addPokemonButton(Pokemon pokemon){
        Button btn = new Button(this);
        btn.setText(pokemon.name);
        btn.setBackgroundTintList(getResources().getColorStateList(R.color.super_light_red));
        ll.addView(btn, lp);
    }

}
