package com.app.pokeapp.data.custom;

import android.content.Context;
import android.util.AttributeSet;
import com.app.pokeapp.data.dto.Pokemon;

public class PokemonButton extends android.support.v7.widget.AppCompatButton {
    public PokemonButton(Context context) {
        super(context);
    }

    public PokemonButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PokemonButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Pokemon pokemon;

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }
}
