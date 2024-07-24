package com.app.pokeapp.data.custom;

import android.content.Context;
import android.util.AttributeSet;
import com.app.pokeapp.data.dto.Challenger;

public class ChallengerButton extends android.support.v7.widget.AppCompatButton {
    public ChallengerButton(Context context) {
        super(context);
    }

    public ChallengerButton(Context context,
                            AttributeSet attrs) {
        super(context, attrs);
    }

    public ChallengerButton(Context context,
                            AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Challenger challenger;

    public void setChallenger(Challenger challenger) {
        this.challenger = challenger;
    }

    public Challenger getChallenger() {
        return challenger;
    }
}