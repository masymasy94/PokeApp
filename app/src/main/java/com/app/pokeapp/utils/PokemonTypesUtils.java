package com.app.pokeapp.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.app.pokeapp.data.dto.Pokemon;
import com.app.pokeapp.data.enums.PokemonType;

import java.util.Arrays;
import java.util.List;

public class PokemonTypesUtils {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static PokemonType getValueOrNull(String type){

        return type == null ? null : Arrays.asList(PokemonType.values()).stream()
                .filter(e -> e.name().equals(type.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    public static String getCombinedType(Pokemon pokemon){
        pokemon.types.remove(null);
        return pokemon.types.size() > 1 ? pokemon.types.get(0).name().concat(" - ").concat(pokemon.types.get(1).name()) : pokemon.types.get(0).name();
    }
}
