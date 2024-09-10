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
import com.app.pokeapp.data.dto.Challenger;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.data.dto.Random;
import com.app.pokeapp.data.enums.PokemonType;
import com.app.pokeapp.db.ChallengerSQLiteHelper;
import com.app.pokeapp.db.PokemonSQLiteHelper;
import com.app.pokeapp.db.RandomSQLiteHelper;
import com.app.pokeapp.utils.PokemonTypesUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RandomsFightActivity extends AppCompatActivity {

    Pokemon myPokemon = null;
    Pokemon randomPokemon = null;
    Random random = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_fight_activity);
        setRandomPokemon();
    }

    public void setMyPokemonRdm(View view) {
        ImageButton btn = findViewById(R.id.image_my_pk_rdm);
        inflatePopUp(view, btn);
    }

    private void setRandomPokemon() {

        ImageButton btn = findViewById(R.id.image_random_pk);

        setRandom();
        setPokemon();

        setRandomPkmValues(btn, random.pokemon);
    }

    private void setPokemon() {
        PokemonSQLiteHelper pokeDb = new PokemonSQLiteHelper(this);
        randomPokemon = pokeDb.getPokemonByName(random.pokemon.toUpperCase());
        randomPokemon.setStrenght(random.basePower);
        pokeDb.close();
    }

    private void setRandom() {
        String rdmName = StringUtils.trimToEmpty(getIntent().getStringExtra("random"));
        RandomSQLiteHelper db = new RandomSQLiteHelper(this);
        random = db.getRandomByName(rdmName);
        db.close();
    }

    private void setRandomPkmValues(ImageButton btn, String rdmPkmName) {
        // hide switches
        setSwichesListenersForRandom();

        // set move
        setTypes(false, randomPokemon);

        // set move power
        setRandomPower(randomPokemon, 0);
        setOtherPowerIfBothPokemonAreChosen(false);

        // set image
        setImage(btn, rdmPkmName);
    }

    private void setSwichesListenersForRandom() {
        Switch bonus3 = findViewById(R.id.switch_3_bonus_random);
        Switch bonus2 = findViewById(R.id.switch_2_bonus_random);
        Switch bonus1 = findViewById(R.id.switch_1_bonus_random);
        bonus1.setOnCheckedChangeListener((buttonView, isChecked) -> uncheckAndSetRandomPower(bonus1, bonus2, bonus3, random.firstBonus));
        bonus2.setOnCheckedChangeListener((buttonView, isChecked) -> uncheckAndSetRandomPower(bonus2, bonus3, bonus1, random.secondBonus));
        bonus3.setOnCheckedChangeListener((buttonView, isChecked) -> uncheckAndSetRandomPower(bonus3, bonus2, bonus1, random.thirdBonus));
    }

    private void uncheckAndSetRandomPower(Switch activeSwitch, Switch bonusToUncheck, Switch bonusToUncheck2, int activeBonus) {
        Switch bonus3 = findViewById(R.id.switch_3_bonus_random);
        Switch bonus2 = findViewById(R.id.switch_2_bonus_random);
        Switch bonus1 = findViewById(R.id.switch_1_bonus_random);

        bonus1.setOnCheckedChangeListener((buttonView, isChecked) -> doNothing());
        bonus2.setOnCheckedChangeListener((buttonView, isChecked) -> doNothing());
        bonus3.setOnCheckedChangeListener((buttonView, isChecked) -> doNothing());

        bonusToUncheck.setChecked(false);
        bonusToUncheck2.setChecked(false);
        setSwichesListenersForRandom();

        setRandomPower(randomPokemon, !activeSwitch.isChecked() ? 0 : activeBonus);
    }

    private void doNothing() {
    }


    private void setRandomPower(Pokemon randomPokemon, int dicePower) {
        TextView powerOfMove = findViewById(R.id.random_pkm_move_pwr);
        powerOfMove.setTextColor(getResources().getColor(R.color.black));


        int basePower = random.basePower;

        double typedModifier = 1.0;
        if (myPokemon != null) {
            typedModifier = PokemonTypesUtils.calculateTypedModifier(randomPokemon.types, myPokemon.types);
        }

        double finalPower = Math.floor(((basePower + dicePower) * typedModifier));
        if (Double.valueOf(finalPower).intValue() != basePower) {
            powerOfMove.setTextColor(getResources().getColor(R.color.light_purple));
        }
        powerOfMove.setText("" + Double.valueOf(finalPower).intValue()); // todo to int
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
        if (myPokemon != null && randomPokemon != null) {
            setPower(!isMyPokemon, isMyPokemon ? randomPokemon : myPokemon);
        }
    }

    private void setPower(boolean isMyPokemon,
                          Pokemon pokemon) {

        TextView powerOfMove = isMyPokemon ? findViewById(R.id.my_pkm_move_pwr_rdm) : findViewById(R.id.random_pkm_move_pwr);
        Switch evo2 = findViewById(R.id.switch_2_evo_my_rdm);
        Switch evo1 = findViewById(R.id.switch_1_evo_my_rdm);

        powerOfMove.setTextColor(getResources().getColor(R.color.black));


        int basePower = pokemon.strenght;
        int evolutionPower = 0;
        if (evo1.isChecked() && isMyPokemon) {
            evolutionPower = evolutionPower + 3;
        }
        if (evo2.isChecked() && isMyPokemon) {
            evolutionPower = evolutionPower + 2;
        }

        double typedModifier = 1.0;
        if (myPokemon != null && randomPokemon != null) {
            typedModifier = PokemonTypesUtils.calculateTypedModifier(pokemon.types, isMyPokemon ? randomPokemon.types : myPokemon.types);
        }

        double finalPower = Math.floor(((basePower + evolutionPower) * typedModifier));
        if (Double.valueOf(finalPower).intValue() != basePower) {
            powerOfMove.setTextColor(getResources().getColor(R.color.light_purple));
        }
        powerOfMove.setText("" + Double.valueOf(finalPower).intValue()); // todo to int
    }

    private void setTypes(boolean isMyPokemon,
                          Pokemon pokemon) {
        TextView textView;
        if (isMyPokemon) {
            textView = findViewById(R.id.my_pkm_types_rdm);
        } else {
            textView = findViewById(R.id.random_pkm_types);
        }

        String types = pokemon.types.stream().map(PokemonType::name).collect(Collectors.joining("\n"));
        textView.setText(types);
    }

    private void hideSwitchesWhenNeededForMyPkm(Pokemon pokemon) {
        Switch evo2 = findViewById(R.id.switch_2_evo_my_rdm);
        Switch evo1 = findViewById(R.id.switch_1_evo_my_rdm);

        evo2.setVisibility(View.VISIBLE);
        evo1.setVisibility(View.VISIBLE);
        setSwichesListeners(evo1, evo2);


        if (!pokemon.isEvolvedTwoTimes) {
            evo2.setVisibility(View.INVISIBLE);
            if (!pokemon.isEvolved) {
                evo1.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void setSwichesListeners(Switch evo1, Switch evo2) {
        evo1.setOnCheckedChangeListener((buttonView, isChecked) -> setPower(true, myPokemon));
        evo2.setOnCheckedChangeListener((buttonView, isChecked) -> setPower(true, myPokemon));
    }

    private String getTailoredName(String selectedName) {
        return selectedName.toLowerCase()
                .replace(" ", "_")
                .replace(".", "");
    }


    private void inflatePopUp(View view,
                              ImageButton btn) {

        // inflate
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.challengers_pkm_search_pop_up_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, MATCH_PARENT, MATCH_PARENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        // AutoCompleteTextView
        PokemonSQLiteHelper pkmDb = new PokemonSQLiteHelper(this);
        List<String> pkmNames = pkmDb.getAllPokemonFromDB()
                .stream()
                .map(Pokemon::getName)
                .collect(Collectors.toList());
        pkmDb.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, pkmNames);
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

            PokemonSQLiteHelper pokeDb = new PokemonSQLiteHelper(this);
            myPokemon = pokeDb.getPokemonByName(selectedName);
            pokeDb.close();

            // hide switches
            hideSwitchesWhenNeededForMyPkm(myPokemon);

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
