package com.app.pokeapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import com.app.pokeapp.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChallengersFightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengers_fight_activity);
    }
}
