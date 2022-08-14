package com.app.pokeapp.data.enums;

import android.support.annotation.ColorRes;

import com.app.pokeapp.R;

public enum PokemonType {
    ACQUA(R.color.super_light_blue),
    ERBA(R.color.light_green),
    VELENO(R.color.light_purple),
    FUOCO(R.color.super_light_red),
    COLEOTTERO(R.color.super_light_brown),
    VOLANTE(R.color.bluish_white),
    NORMALE(R.color.super_light_grey),
    ELETTRO(R.color.yellow),
    TERRA(R.color.brown),
    LOTTA(R.color.purplish_pink),
    PSICO(R.color.dark_pink),
    ROCCIA(R.color.light_brown),
    GHIACCIO(R.color.whitish_blue),
    SPETTRO(R.color.dark_purple),
    DRAGO(R.color.light_orange);

    private final int color;

    PokemonType(@ColorRes int color) {
        this.color = color;
    }

    public @ColorRes int getColor() {
        return color;
    }
}
