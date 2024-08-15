package com.app.pokeapp.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.data.enums.PokemonType;
import com.app.pokeapp.db.PokemonSQLiteHelper;
import com.app.pokeapp.utils.PokemonTypesUtils;

import java.util.List;
import java.util.stream.Collectors;

import static android.widget.LinearLayout.LayoutParams.*;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TrainersFightActivity extends AppCompatActivity {

    Pokemon myPokemon    = null;
    Pokemon enemyPokemon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainers_fight_activity);
    }

    public void setMyPokemon(View view) {
        ImageButton btn = findViewById(R.id.image_my_pk);
        inflatePopUp(view, btn, true);
    }

    public void setEnemyPokemon(View view) {
        ImageButton btn = findViewById(R.id.image_enemy_pk);
        inflatePopUp(view, btn, false);
    }


    private void inflatePopUp(View view,
                              ImageButton btn,
                              boolean isMyPokemon) {

        // inflate
        LayoutInflater    inflater    = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View              popupView   = inflater.inflate(R.layout.trainers_fight_pop_up_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, MATCH_PARENT, MATCH_PARENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        // AutoCompleteTextView
        PokemonSQLiteHelper pkmDb = new PokemonSQLiteHelper(this);
        List<String> pkmNames = pkmDb.getAllPokemonFromDB()
                                     .stream()
                                     .map(Pokemon::getName)
                                     .collect(Collectors.toList());
        pkmDb.close();

        ArrayAdapter<String> adapter      = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, pkmNames);
        AutoCompleteTextView autoTextView = popupView.findViewById(R.id.edit_query);
        autoTextView.setAdapter(adapter);
        autoTextView.setThreshold(0);
        autoTextView.setOnDismissListener(
                () -> setPokemonValuesAndClosePopUp(btn, autoTextView, pkmNames, popupWindow, isMyPokemon));


        // close button
        Button closeBtn = popupView.findViewById(R.id.close_trainers_popup_btn);
        closeBtn.setOnClickListener(unused -> popupWindow.dismiss());
    }

    private void setPokemonValuesAndClosePopUp(ImageButton btn,
                                               AutoCompleteTextView autoTextView,
                                               List<String> pkmNames,
                                               PopupWindow popupWindow,
                                               boolean isMyPokemon) {

        String selectedName = autoTextView.getText()
                                          .toString()
                                          .replace("\n", "");

        if (pkmNames.contains(selectedName)) {

            PokemonSQLiteHelper pokeDb  = new PokemonSQLiteHelper(this);
            Pokemon             pokemon = pokeDb.getPokemonByName(selectedName);
            pokeDb.close();
            setPokemon(isMyPokemon, pokemon);

            // hide switches
            hideSwitchesWhenNeeded(isMyPokemon, pokemon);

            // set move
            setTypes(isMyPokemon, pokemon);

            // set move power
            setPower(isMyPokemon, pokemon);
            setOtherPowerIfBothPokemonAreChosen(isMyPokemon);

            // set image
            setImage(btn, selectedName);
            popupWindow.dismiss();
        }
    }

    private void setOtherPowerIfBothPokemonAreChosen(boolean isMyPokemon) {
        if (myPokemon != null && enemyPokemon != null) {
            setPower(!isMyPokemon, isMyPokemon ? enemyPokemon: myPokemon);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setPower(boolean isMyPokemon,
                          Pokemon pokemon) {
        TextView powerOfMove;
        Switch evo1;
        Switch evo2;
        if (isMyPokemon) {
            powerOfMove = findViewById(R.id.my_pkm_move_pwr);
            evo2 = findViewById(R.id.switch_2_evo_my);
            evo1 = findViewById(R.id.switch_1_evo_my);
        } else {
            powerOfMove = findViewById(R.id.enemy_pkm_move_pwr);
            evo2 = findViewById(R.id.switch_2_evo_enemy);
            evo1 = findViewById(R.id.switch_1_evo_enemy);
        }
        powerOfMove.setTextColor(getResources().getColor(R.color.black));


        int basePower = pokemon.strenght;
        int evolutionPower = 0;
        if (evo1.isChecked()) {
            evolutionPower = evolutionPower + 3;
        }
        if (evo2.isChecked()) {
            evolutionPower = evolutionPower + 2;
        }

        double typedModifier = 1.0;
        if (myPokemon != null && enemyPokemon != null) {
            typedModifier = PokemonTypesUtils.calculateTypedModifier(pokemon.types, isMyPokemon ? enemyPokemon.types : myPokemon.types);
        }

        double finalPower = Math.floor( (( basePower + evolutionPower) * typedModifier));
        if (Double.valueOf(finalPower).intValue()!=basePower){
            powerOfMove.setTextColor(getResources().getColor(R.color.light_purple));
        }
        powerOfMove.setText(""+Double.valueOf(finalPower).intValue()); // todo to int
    }

    private void hideSwitchesWhenNeeded(boolean isMyPokemon,
                           Pokemon pokemon) {
        Switch evo2;
        Switch evo1;
        if (isMyPokemon) {
            evo2 = findViewById(R.id.switch_2_evo_my);
            evo1 = findViewById(R.id.switch_1_evo_my);

        } else {
            evo2 = findViewById(R.id.switch_2_evo_enemy);
            evo1 = findViewById(R.id.switch_1_evo_enemy);
        }
        evo2.setVisibility(View.VISIBLE);
        evo1.setVisibility(View.VISIBLE);
        setSwichesListeners(evo1, evo2, isMyPokemon);


        if (!pokemon.isEvolvedTwoTimes){
            evo2.setVisibility(View.INVISIBLE);
            if (!pokemon.isEvolved) {
                evo1.setVisibility(View.INVISIBLE);
            }

        }
    }
    private void setSwichesListeners(Switch evo1, Switch evo2,
                                     boolean isMyPokemon) {
        evo1.setOnCheckedChangeListener((buttonView, isChecked) -> setPower(isMyPokemon, isMyPokemon? myPokemon : enemyPokemon));
        evo2.setOnCheckedChangeListener((buttonView, isChecked) -> setPower(isMyPokemon, isMyPokemon? myPokemon : enemyPokemon));
    }

    private void setTypes(boolean isMyPokemon,
                          Pokemon pokemon) {
        TextView textView;
        if (isMyPokemon) {
            textView = findViewById(R.id.my_pkm_types);
        } else {
            textView = findViewById(R.id.enemy_pkm_types);
        }

        String types = pokemon.types.stream().map(PokemonType::name).collect(Collectors.joining("\n"));
        textView.setText(types);
    }

    private void setPokemon(boolean isMyPokemon,
                            Pokemon pokemon) {
        if (isMyPokemon) {
            myPokemon = pokemon;
        } else {
            enemyPokemon = pokemon;
        }
    }

    private void setImage(ImageButton btn,
                          String selectedName) {
        int resourceId = getResources().getIdentifier(getTailoredName(selectedName), "drawable",
                                                      getPackageName());
        if (resourceId > 0) {
            btn.setImageDrawable(getResources().getDrawable(resourceId));
        } else {
            btn.setImageDrawable(getResources().getDrawable(R.color.bluish_white)); // todo remove
        }
    }

    private String getTailoredName(String selectedName) {
        return selectedName.toLowerCase()
                           .replace(" ", "_")
                           .replace(".", "");
    }

}
