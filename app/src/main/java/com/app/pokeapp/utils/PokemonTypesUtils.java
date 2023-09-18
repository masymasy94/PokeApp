package com.app.pokeapp.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import com.app.pokeapp.data.dto.ElementalEffect;
import com.app.pokeapp.data.dto.ResultElements;
import com.app.pokeapp.data.enums.PokemonType;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.app.pokeapp.data.enums.PokemonType.*;

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
    public static ResultElements getElementalEffectsForTypes(List<PokemonType> types) {

        Map<PokemonType, BigDecimal> resultsMap = ElementalEffect.getElementalEffects()
                                                                 .keySet()
                                                                 .stream()
                                                                 .collect(Collectors.toMap(pokemonType -> pokemonType,
                                                                                           unsused -> BigDecimal.ONE));

        Set<PokemonType> immunities = new HashSet<>();
        types.forEach(pokemonType -> {

            ElementalEffect elements = ElementalEffect.getElementalEffects()
                                                      .get(pokemonType);
            Objects.requireNonNull(elements)
                   .getStrongAgainst()
                   .forEach(str -> modifyMapValue(resultsMap, str, 0.5));
            elements.getWeakAgainst()
                    .forEach(weak -> modifyMapValue(resultsMap, weak, -0.5));
            elements.getDealsNotingTo()
                    .forEach(dn -> resultsMap.put(dn, BigDecimal.ZERO));

            immunities.addAll(elements.getImmuneAgainst());
        });

        return new ResultElements(resultsMap, immunities);
    }

    private static void modifyMapValue(Map<PokemonType, BigDecimal> resultsMap,
                                       PokemonType str,
                                       double val) {
        resultsMap.put(str, Objects.requireNonNull(resultsMap.get(str))
                                   .add(BigDecimal.valueOf(val)));
    }


    //LIST HAVE MAXIMUM 2 TYPES
    @SuppressLint("NewApi")
    public static double calculateTypedModifier(List<PokemonType> attackerTypes,
                                             List<PokemonType> defenderTypes) {

        // attacker type immune to defender
        ResultElements result = getElementalEffectsForTypes(attackerTypes);


        Map<PokemonType, BigDecimal> resultForDefender =
                result.getElements()
                      .entrySet()
                      .stream()
                      .filter(e -> defenderTypes.contains(e.getKey()))
                      .filter(e -> e.getValue().compareTo(BigDecimal.ONE) != 0)
                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // defender has immune type
        if (resultForDefender.entrySet()
                             .stream()
                             .anyMatch(e -> e.getValue()
                                              .equals(BigDecimal.ZERO))) {
            return 0;
        }

        AtomicReference<Double> modifier = new AtomicReference<>(1.0);
        resultForDefender.forEach((type, mod) -> {
            if (mod.compareTo(BigDecimal.ONE) > 0) {
                modifier.set(modifier.get() + (mod.doubleValue()-1));
            }
        });
        if (resultForDefender.entrySet().stream().anyMatch(e -> e.getValue().compareTo(BigDecimal.ONE) < 0)){
         modifier.set(modifier.get()-0.5);
        }
        return modifier.get();
    }


}
