package com.app.pokeapp.data.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.app.pokeapp.data.dto.Challenger;
import com.app.pokeapp.data.dto.Random;

public class RandomButton extends android.support.v7.widget.AppCompatButton {
    public RandomButton(Context context) {
        super(context);
    }

    public RandomButton(Context context,
                        AttributeSet attrs) {
        super(context, attrs);
    }

    public RandomButton(Context context,
                        AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Random random;

    public void setRandom(Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }
}