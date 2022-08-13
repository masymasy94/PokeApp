package com.app.pokeapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.pokeapp.R;
import com.app.pokeapp.data.custom.PokemonButton;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.db.PokemonSQLiteHelper;
import com.app.pokeapp.utils.PokemonTypesUtils;

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
        List<Pokemon> allPk = db.getAllPokemonFromDB();
        db.close();
        return allPk;
    }

    private void addPokemonButton(Pokemon pokemon){
        PokemonButton btn = new PokemonButton(this);
        btn.setText(pokemon.name);
        btn.setBackgroundTintList(getResources().getColorStateList(R.color.super_light_red));
        btn.setPokemon(pokemon);
        btn.setId(pokemon.id);
        btn.setOnClickListener(getOpenPokemonPopUpOnClickListener(pokemon));
        ll.addView(btn, lp);
    }

    private View.OnClickListener getOpenPokemonPopUpOnClickListener(Pokemon pokemon) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pokemon_pop_up_window, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

                TextView pokemonName = (TextView) popupView.findViewById(R.id.pokemon_name);
                pokemonName.setText(pokemon.name);
                TextView pokemonType = (TextView) popupView.findViewById(R.id.pokemon_type);
                pokemonType.setText(pokemon.types.get(0).name().toUpperCase());
                //todo - colore sfondo

                if (pokemon.types.size()>1) {
                    TextView pokemonType2 = (TextView) popupView.findViewById(R.id.pokemon_second_type);
                    pokemonType2.setText(pokemon.types.get(1).name().toUpperCase());
                    pokemonType2.setBackgroundTintList(getResources().getColorStateList(R.color.super_light_blue));
                    //todo - colore sfondo
                }

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                setClosePopUpButton(popupView, popupWindow);

            }
        };
    }

    private void setClosePopUpButton(View popupView, PopupWindow popupWindow) {
        Button closeBtn = (Button) popupView.findViewById(R.id.close_popup_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View popupView) {
                popupWindow.dismiss();
            }
        });
    }





}
