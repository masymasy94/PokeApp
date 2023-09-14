package com.app.pokeapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.app.pokeapp.R;
import com.app.pokeapp.data.custom.PokemonButton;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.data.dto.ResultElements;
import com.app.pokeapp.data.enums.PokemonType;
import com.app.pokeapp.db.PokemonSQLiteHelper;
import com.app.pokeapp.utils.AndroidUtils;
import com.app.pokeapp.utils.PokemonTypesUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint({"ResourceType", "SetTextI18n"})
public class PokedexActivity extends AppCompatActivity {

    List<PokemonButton>       pokemonButtons = new ArrayList<>();
    LinearLayout              ll             = null;
    LinearLayout.LayoutParams lp             = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokedex_activity);
        setInstanceValues();
        showPokemon();
    }

    private void setInstanceValues() {
        ll = findViewById(R.id.pokedex_ll);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                           LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showPokemon() {
        List<Pokemon> allPk = getAllPokemon();
        allPk.sort(Comparator.comparing(Pokemon::getName));
        allPk.forEach(this::addPokemonButton);
        if (allPk.isEmpty()) {
            showNoPokemonFoundMessage();
        }

        addAllPokemonButtonsToLayout();
    }

    private void addAllPokemonButtonsToLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(lp);
        layoutParams.setMargins(20, 20, 20, 20);
        pokemonButtons.forEach(btn -> ll.addView(btn, layoutParams));
    }


    private void showNoPokemonFoundMessage() {
        TextView empty = new TextView(this);
        empty.setGravity(Gravity.CENTER);
        empty.setText("No pokemon found in database!");
        ll.addView(empty, lp);
    }

    private List<Pokemon> getAllPokemon() {
        PokemonSQLiteHelper db    = new PokemonSQLiteHelper(this);
        List<Pokemon>       allPk = db.getAllPokemonFromDB();
        db.close();
        return allPk;
    }

    private int getThemePrimaryColor(Context context) {
        int        colorAttr = android.R.attr.colorPrimary;
        TypedValue outValue  = new TypedValue();
        context.getTheme()
               .resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }

    private void addPokemonButton(Pokemon pokemon) {
        PokemonButton btn = new PokemonButton(this);

        btn.setTextSize(20);
        btn.setBackground(getResources().getDrawable(R.drawable.small_round_corners));
        btn.setText(pokemon.name);
        btn.setBackgroundTintList(ColorStateList.valueOf(getThemePrimaryColor(this)));
        btn.setPokemon(pokemon);
        btn.setId(pokemon.id);
        btn.setOnClickListener(getOpenPokemonPopUpOnClickListener(pokemon));

        pokemonButtons.add(btn);
    }

    @SuppressLint("InflateParams")
    private View.OnClickListener getOpenPokemonPopUpOnClickListener(Pokemon pokemon) {
        return view -> {

            LayoutInflater inflater  = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View           popupView = inflater.inflate(R.layout.pokemon_pop_up_window, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.MATCH_PARENT, true);


            ImageView pokemonImage = popupView.findViewById(R.id.pokemon_image);
            int resourceId = getResources().getIdentifier(pokemon.name.toLowerCase(), "drawable",
                                                          getPackageName());
            if (resourceId > 0) {
                pokemonImage.setImageDrawable(getResources().getDrawable(resourceId));
            } else {
                pokemonImage.setImageTintList(getResources().getColorStateList(pokemon.types.get(0)
                                                                                            .getColor()));
            }


            TextView pokemonName = popupView.findViewById(R.id.pokemon_name);
            pokemonName.setText(pokemon.name);
            TextView pokemonType = popupView.findViewById(R.id.pokemon_type);
            pokemonType.setText(pokemon.types.get(0)
                                             .name()
                                             .toUpperCase());
            pokemonType.setBackgroundTintList(getResources().getColorStateList(pokemon.types.get(0)
                                                                                            .getColor()));


            TextView pokemonMove = popupView.findViewById(R.id.pokemon_move);
            pokemonMove.setText(StringUtils.trimToEmpty(pokemon.move)
                                           .toUpperCase());
            pokemonMove.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            pokemonMove.setBackgroundTintList(getResources().getColorStateList(pokemon.types.get(0)
                                                                                            .getColor()));

            TextView pokemonMovePower = popupView.findViewById(R.id.move_power);
            pokemonMovePower.setText("" + pokemon.strenght);
            pokemonMovePower.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            pokemonMovePower.setBackgroundTintList(getResources().getColorStateList(pokemon.types.get(0)
                                                                                                 .getColor()));

            if (pokemon.types.size() > 1) {
                TextView pokemonType2 = popupView.findViewById(R.id.pokemon_second_type);
                pokemonType2.setText(pokemon.types.get(1)
                                                  .name()
                                                  .toUpperCase());
                pokemonType2.setBackgroundTintList(getResources().getColorStateList(pokemon.types.get(1)
                                                                                                 .getColor()));
            }


            setupEvolutionSwitches(popupView, pokemonMovePower, pokemon);

            TextView elements_tv = popupView.findViewById(R.id.popup_elements_tv);
            elements_tv.setGravity(Gravity.CENTER);
            elements_tv.setTextSize(20);
            elements_tv.setTextColor(getResources().getColor(R.color.black));
            elements_tv.setLayoutParams(lp);
            String elements = getElements(pokemon.types);
            elements_tv.setText(elements);


            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            AndroidUtils.hideKeyboard(this);
            setClosePopUpButton(popupView, popupWindow);

        };
    }

    private String getElements(List<PokemonType> types) {
        List<String> str   = new ArrayList<>();
        List<String> weak  = new ArrayList<>();
        List<String> noDmg = new ArrayList<>();

        ResultElements resultElements = PokemonTypesUtils.getElementalEffectsForType(types);
        resultElements.getElements()
                      .forEach((type, modifier) -> sortByModifier(type, modifier, str, noDmg, weak));

        return getResultingElements(str, weak, noDmg, resultElements.getImmunities());

    }

    private static void sortByModifier(PokemonType type,
                                       BigDecimal modifier,
                                       List<String> str,
                                       List<String> noDmg,
                                       List<String> weak) {
        if (modifier.compareTo(BigDecimal.ONE) > 0) {
            str.add(type.name()
                        .toLowerCase()
                        .concat(" x ")
                        .concat(modifier.toPlainString()));
        } else if (modifier.equals(BigDecimal.ZERO)) {
            noDmg.add(type.name()
                          .toLowerCase());
        } else if (modifier.compareTo(BigDecimal.ONE) < 0) {
            weak.add(type.name()
                         .toLowerCase()
                         .concat(" x ")
                         .concat(modifier.toPlainString()));
        }
    }

    private static String getResultingElements(List<String> str,
                                               List<String> weak,
                                               List<String> noDmg,
                                               Set<PokemonType> immunities) {
        String result = "";
        if (!str.isEmpty()) {
            result = result.concat("VANTAGGI\n")
                           .concat(String.join("\n", str))
                           .concat("\n\n");
        }

        if (!weak.isEmpty()) {
            result = result.concat("SVANTAGGI\n")
                           .concat(String.join("\n", weak))
                           .concat("\n\n");
        }

        if (!noDmg.isEmpty()) {
            result = result.concat("DANNO NULLO\n")
                           .concat(String.join("\n", noDmg))
                           .concat("\n\n");
        }

        if (!immunities.isEmpty()) {
            String imm = immunities.stream()
                                   .map(s -> s.name()
                                              .toLowerCase())
                                   .collect(Collectors.joining("\n"));
            result = result.concat("IMMUNITA'\n")
                           .concat(imm)
                           .concat("\n\n");
        }
        return result;
    }

    private void setupEvolutionSwitches(View popupView,
                                        TextView pokemonMovePower,
                                        Pokemon pokemon) {
        Switch switchSecondEvolution = popupView.findViewById(R.id.switch_second_evolution);
        Switch switchFirstEvolution  = popupView.findViewById(R.id.switch_first_evolution);
        switchSecondEvolution.setVisibility(View.INVISIBLE);
        switchFirstEvolution.setVisibility(View.INVISIBLE);
        if (pokemon.isEvolved) {
            switchFirstEvolution.setVisibility(View.VISIBLE);

            if (pokemon.isEvolvedTwoTimes) {
                switchSecondEvolution.setVisibility(View.VISIBLE);
            }
        }
        setSwichesListeners(switchFirstEvolution, pokemonMovePower, switchSecondEvolution);
    }

    private void setSwichesListeners(Switch switchFirstEvolution,
                                     TextView pokemonMovePower,
                                     Switch switchSecondEvolution) {
        switchFirstEvolution.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                pokemonMovePower.setText("" + (Integer.parseInt((String) pokemonMovePower.getText()) + 3));
            } else {
                pokemonMovePower.setText("" + (Integer.parseInt((String) pokemonMovePower.getText()) - 3));
            }

        });
        switchSecondEvolution.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                pokemonMovePower.setText("" + (Integer.parseInt((String) pokemonMovePower.getText()) + 2));
            } else {
                pokemonMovePower.setText("" + (Integer.parseInt((String) pokemonMovePower.getText()) - 2));
            }

        });
    }

    private void setClosePopUpButton(View popupView,
                                     PopupWindow popupWindow) {
        Button closeBtn = popupView.findViewById(R.id.close_popup_btn);
        closeBtn.setOnClickListener(popupView1 -> popupWindow.dismiss());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                                                 .getActionView();
        searchView.setIconifiedByDefault(true);
        setSearchViewOnQueryTextListener(searchView);

        return true;
    }

    private void setSearchViewOnQueryTextListener(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String searched) {
                ll.removeAllViews();

                pokemonButtons.stream()
                              .filter(btn -> containsPokemonName(btn, searched))
                              .forEach(btn -> ll.addView(btn));
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String searched) {
                return false;
            }
        });
    }

    private boolean containsPokemonName(PokemonButton btn,
                                        String searched) {
        return StringUtils.containsIgnoreCase(getPokemonName(btn).toLowerCase(), searched.toLowerCase());
    }

    private String getPokemonName(PokemonButton btn) {
        return StringUtils.trimToEmpty(btn.getPokemon()
                                          .getName());
    }


}
