package com.app.pokeapp.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import com.app.pokeapp.R;
import com.app.pokeapp.data.dto.ElementalEffect;
import com.app.pokeapp.data.enums.PokemonType;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.app.pokeapp.data.enums.PokemonType.*;
import static com.app.pokeapp.data.enums.PokemonType.TERRA;

public class PokemonTypesUtils {


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static PokemonType getValueOrNull(String type) {

        return type == null ? null : Arrays.stream(values())
                                           .filter(e -> e.name()
                                                         .equals(type.toUpperCase()))
                                           .findFirst()
                                           .orElse(null);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ResultElements getElementalEffectsForType(List<PokemonType> types) {

        Map<PokemonType, BigDecimal> resultsMap = ElementalEffect.getElementalEffects()
                                                                 .keySet()
                                                                 .stream()
                                                                 .collect(Collectors.toMap(pokemonType -> pokemonType, unsused -> BigDecimal.ONE));

        Set<PokemonType> immunities = new HashSet<>();
        types.forEach(pokemonType -> {

            ElementalEffect elements = ElementalEffect.getElementalEffects().get(pokemonType);
            elements.getStrongAgainst()
                    .forEach(str -> modifyMapValue(resultsMap, str, 0.5));
            elements.getWeakAgainst()
                    .forEach(weak -> modifyMapValue(resultsMap, weak, -0.5));
            elements.getDealsNotingTo()
                    .forEach(ghh -> resultsMap.put(ghh, BigDecimal.ZERO));

            immunities.addAll(new HashSet<>(elements.getImmuneAgainst()));
        });

        return new ResultElements(resultsMap, immunities);
    }

    private static void modifyMapValue(Map<PokemonType, BigDecimal> resultsMap,
                                             PokemonType str,
                                             double val) {
        resultsMap.put(str, Objects.requireNonNull(resultsMap.get(str))
                                   .add(BigDecimal.valueOf(val)));
    }

    public static class ResultElements {
        Map<PokemonType, BigDecimal> elements;
        Set<PokemonType> immunities;

        public ResultElements(Map<PokemonType, BigDecimal> elements,
                              Set<PokemonType> immunities) {
            this.elements   = elements;
            this.immunities = immunities;
        }

        public Map<PokemonType, BigDecimal> getElements() {
            return elements;
        }

        public Set<PokemonType> getImmunities() {
            return immunities;
        }
    }

}
