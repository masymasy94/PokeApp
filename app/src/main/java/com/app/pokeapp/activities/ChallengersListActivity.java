package com.app.pokeapp.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.app.pokeapp.R;
import com.app.pokeapp.data.custom.ChallengerButton;
import com.app.pokeapp.data.dto.Challenger;
import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.db.ChallengerSQLiteHelper;
import com.app.pokeapp.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.N)
public class ChallengersListActivity extends AppCompatActivity {

    List<ChallengerButton>    challengerButtons = new ArrayList<>();
    LinearLayout              ll                = null;
    LinearLayout.LayoutParams lp                = null;


    Pokemon myPokemon    = null;
    Pokemon enemyPokemon = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengers_list_activity);
        setInstanceValues();
        inflateChallengers();
    }

    private void inflateChallengers() {

        List<Challenger> challengers = getAllChallengers();
        challengers.sort(Comparator.comparing(Challenger::getName));
        challengers.forEach(this::addChallengerButton);
        challengerButtons.forEach(btn -> ll.addView(btn, lp));

    }


    private void setInstanceValues() {
        ll = findViewById(R.id.challengers_ll);
        lp = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                              LinearLayout.LayoutParams.WRAP_CONTENT));
        lp.setMargins(50, 20, 50, 20);
    }

    private List<Challenger> getAllChallengers() {
        ChallengerSQLiteHelper db      = new ChallengerSQLiteHelper(this);
        List<Challenger>       allChas = db.getAllChallengers();
        db.close();
        return allChas;
    }

    private void addChallengerButton(Challenger challenger) {
        ChallengerButton btn = new ChallengerButton(this);
        btn.setTextSize(20);
        btn.setBackground(getResources().getDrawable(R.drawable.small_round_corners));
        btn.setText(challenger.name);
        btn.setBackgroundTintList(ColorStateList.valueOf(AndroidUtils.getThemePrimaryColor(this)));
        btn.setChallenger(challenger);
        btn.setOnClickListener(clic -> getListener(challenger));
        challengerButtons.add(btn);
    }

    private void getListener(Challenger challenger) {
        startActivity(
                new Intent(ChallengersListActivity.this, ChallengersFightActivity.class)
                        .putExtra("challenger", challenger.name)
        );
    }


}
