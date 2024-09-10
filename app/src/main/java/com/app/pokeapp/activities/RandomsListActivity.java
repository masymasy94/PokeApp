package com.app.pokeapp.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.app.pokeapp.R;
import com.app.pokeapp.data.custom.RandomButton;
import com.app.pokeapp.data.dto.Random;
import com.app.pokeapp.db.RandomSQLiteHelper;
import com.app.pokeapp.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.N)
public class RandomsListActivity extends AppCompatActivity {

    List<RandomButton>    randomButtons = new ArrayList<>();
    LinearLayout              ll                = null;
    LinearLayout.LayoutParams lp                = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.randoms_list_activity);
        setInstanceValues();
        inflateChallengers();
    }

    private void inflateChallengers() {

        List<Random> randoms = getAllRandoms();
        randoms.sort(Comparator.comparing(Random::getName));
        randoms.forEach(this::addRandomButton);
        randomButtons.forEach(btn -> ll.addView(btn, lp));

    }


    private void setInstanceValues() {
        ll = findViewById(R.id.randoms_ll);
        lp = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                              LinearLayout.LayoutParams.WRAP_CONTENT));
        lp.setMargins(50, 20, 50, 20);
    }

    private List<Random> getAllRandoms() {
        RandomSQLiteHelper db      = new RandomSQLiteHelper(this);
        List<Random>       allRandoms = db.getAllRandoms();
        db.close();
        return allRandoms;
    }

    private void addRandomButton(Random random) {
        RandomButton btn = new RandomButton(this);
        btn.setTextSize(20);
        btn.setBackground(getResources().getDrawable(R.drawable.small_round_corners));
        btn.setText(random.name);
        btn.setBackgroundTintList(ColorStateList.valueOf(AndroidUtils.getThemePrimaryColor(this)));
        btn.setRandom(random);
        btn.setOnClickListener(clic -> getListener(random));
        randomButtons.add(btn);
    }

    private void getListener(Random random) {
        startActivity(
                new Intent(RandomsListActivity.this, RandomsFightActivity.class)
                        .putExtra("random", random.name)
        );
    }


}
