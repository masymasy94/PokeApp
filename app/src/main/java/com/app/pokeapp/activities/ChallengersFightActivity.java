package com.app.pokeapp.activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.data.enums.PokemonType;
import com.app.pokeapp.db.PokemonSQLiteHelper;
import com.app.pokeapp.utils.PokemonTypesUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChallengersFightActivity extends AppCompatActivity {

    Pokemon myPokemon    = null;
    Pokemon challengerPokemon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenger_fight_activity);
        setChallengerPokemon();
    }

    public void setMyPokemonCh(View view) {
        ImageButton btn = findViewById(R.id.image_my_pk_ch);
        inflatePopUp(view, btn);
    }

    private void setChallengerPokemon() {

        ImageButton btn = findViewById(R.id.image_challenger_pk);

        // set pokemon
        String challPkmName = StringUtils.trimToEmpty(getIntent().getStringExtra("chPkmName"));
        PokemonSQLiteHelper pokeDb  = new PokemonSQLiteHelper(this);
        challengerPokemon = pokeDb.getPokemonByName(challPkmName.toUpperCase());
        pokeDb.close();

        setChallengerPkmValues(btn, challPkmName);
    }

    private void setChallengerPkmValues(ImageButton btn, String challPkmName) {
        // hide switches
        hideSwitchesWhenNeeded(false, challengerPokemon);

        // set move
        setTypes(false, challengerPokemon);

        // set move power
        setPower(false, challengerPokemon);
        setOtherPowerIfBothPokemonAreChosen(false);

        // set image
        setImage(btn, challPkmName);
    }


    //------------------------------------------------------------------------------

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

    private void setOtherPowerIfBothPokemonAreChosen(boolean isMyPokemon) {
        if (myPokemon != null && challengerPokemon != null) {
            setPower(!isMyPokemon, isMyPokemon ? challengerPokemon: myPokemon);
        }
    }

    private void setPower(boolean isMyPokemon,
                          Pokemon pokemon) {
        TextView powerOfMove;
        Switch evo1;
        Switch evo2;
        if (isMyPokemon) {
            powerOfMove = findViewById(R.id.my_pkm_move_pwr_ch);
            evo2 = findViewById(R.id.switch_2_evo_my_ch);
            evo1 = findViewById(R.id.switch_1_evo_my_ch);
        } else {
            powerOfMove = findViewById(R.id.challenger_pkm_move_pwr);
            evo2 = findViewById(R.id.switch_2_evo_challenger);
            evo1 = findViewById(R.id.switch_1_evo_challenger);
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
        if (myPokemon != null && challengerPokemon != null) {
            typedModifier = PokemonTypesUtils.calculateTypedModifier(pokemon.types, isMyPokemon ? challengerPokemon.types : myPokemon.types);
        }

        double finalPower = Math.floor( (( basePower + evolutionPower) * typedModifier));
        if (Double.valueOf(finalPower).intValue()!=basePower){
            powerOfMove.setTextColor(getResources().getColor(R.color.light_purple));
        }
        powerOfMove.setText(""+Double.valueOf(finalPower).intValue()); // todo to int
    }

    private void setTypes(boolean isMyPokemon,
                          Pokemon pokemon) {
        TextView textView;
        if (isMyPokemon) {
            textView = findViewById(R.id.my_pkm_types_ch);
        } else {
            textView = findViewById(R.id.challenger_pkm_types);
        }

        String types = pokemon.types.stream().map(PokemonType::name).collect(Collectors.joining("\n"));
        textView.setText(types);
    }

    private void hideSwitchesWhenNeeded(boolean isMyPokemon,
                                        Pokemon pokemon) {
        Switch evo2;
        Switch evo1;
        if (isMyPokemon) {
            evo2 = findViewById(R.id.switch_2_evo_my_ch);
            evo1 = findViewById(R.id.switch_1_evo_my_ch);

        } else {
            evo2 = findViewById(R.id.switch_2_evo_challenger);
            evo1 = findViewById(R.id.switch_1_evo_challenger);
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
        evo1.setOnCheckedChangeListener((buttonView, isChecked) -> setPower(isMyPokemon, isMyPokemon? myPokemon : challengerPokemon));
        evo2.setOnCheckedChangeListener((buttonView, isChecked) -> setPower(isMyPokemon, isMyPokemon? myPokemon : challengerPokemon));
    }

    private String getTailoredName(String selectedName) {
        return selectedName.toLowerCase()
                .replace(" ", "_")
                .replace(".", "");
    }



    private void inflatePopUp(View view,
                              ImageButton btn) {

        // inflate
        LayoutInflater inflater    = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View              popupView   = inflater.inflate(R.layout.challengers_pkm_search_pop_up_window, null);
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
        AutoCompleteTextView autoTextView = popupView.findViewById(R.id.edit_query_ch);
        autoTextView.setAdapter(adapter);
        autoTextView.setThreshold(0);
        autoTextView.setOnDismissListener(
                () -> setPokemonValuesAndClosePopUp(btn, autoTextView, pkmNames, popupWindow));


        // close button
        Button closeBtn = popupView.findViewById(R.id.close_challengers_popup_btn);
        closeBtn.setOnClickListener(unused -> popupWindow.dismiss());
    }

    private void setPokemonValuesAndClosePopUp(ImageButton btn,
                                               AutoCompleteTextView autoTextView,
                                               List<String> pkmNames,
                                               PopupWindow popupWindow) {

        String selectedName = autoTextView.getText()
                .toString()
                .replace("\n", "");

        if (pkmNames.contains(selectedName)) {

            PokemonSQLiteHelper pokeDb  = new PokemonSQLiteHelper(this);
            myPokemon = pokeDb.getPokemonByName(selectedName);
            pokeDb.close();

            // swiches listeners


            // hide switches
            hideSwitchesWhenNeeded(true, myPokemon);

            // set move
            setTypes(true, myPokemon);

            // set move power
            setPower(true, myPokemon);
            setOtherPowerIfBothPokemonAreChosen(true);

            // set image
            setImage(btn, selectedName);
            popupWindow.dismiss();
        }
    }

}
