package com.app.pokeapp.data.dto;

import com.app.pokeapp.data.enums.PokemonType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.app.pokeapp.data.enums.PokemonType.*;
import static com.app.pokeapp.data.enums.PokemonType.DRAGO;

public class ElementalEffect {

    public ElementalEffect(Set<PokemonType> strongAgainst,
                           Set<PokemonType> weakAgainst,
                           Set<PokemonType> immuneAgainst,
                           Set<PokemonType> dealsNotingTo) {
        this.strongAgainst = strongAgainst;
        this.weakAgainst   = weakAgainst;
        this.immuneAgainst = immuneAgainst;
        this.dealsNotingTo = dealsNotingTo;
    }

    private Set<PokemonType> strongAgainst = Set.of();
    private Set<PokemonType> weakAgainst = Set.of();
    private Set<PokemonType> immuneAgainst = Set.of();
    private Set<PokemonType> dealsNotingTo = Set.of();

    public Set<PokemonType> getStrongAgainst() {
        return strongAgainst;
    }

    public Set<PokemonType> getWeakAgainst() {
        return weakAgainst;
    }

    public Set<PokemonType> getImmuneAgainst() {
        return immuneAgainst;
    }

    public Set<PokemonType> getDealsNotingTo() {
        return dealsNotingTo;
    }

    private static final ElementalEffect NORMAL   = new ElementalEffect(
            Set.of(),
            Set.of(ROCCIA),
            Set.of(SPETTRO),
            Set.of(SPETTRO));
    private static final ElementalEffect FIGHTING = new ElementalEffect(
            Set.of(NORMALE, ROCCIA, GHIACCIO),
            Set.of(VELENO, VOLANTE, COLEOTTERO, PSICO),
            Set.of(),
            Set.of(SPETTRO));

    private static final ElementalEffect FLYING = new ElementalEffect(
            Set.of(LOTTA, COLEOTTERO, ERBA),
            Set.of(ROCCIA, ELETTRO),
            Set.of(TERRA),
            Set.of());

    private static final ElementalEffect POISON = new ElementalEffect(Set.of(COLEOTTERO, ERBA),
                                                                      Set.of(VELENO, TERRA,
                                                                             ROCCIA, SPETTRO),
                                                                      Set.of(),
                                                                      Set.of());

    private static final ElementalEffect GROUND = new ElementalEffect(
            Set.of(VELENO, ROCCIA, FUOCO, ELETTRO),
            Set.of(COLEOTTERO, ERBA),
            Set.of(ELETTRO),
            Set.of(VOLANTE));

    private static final ElementalEffect ROCK = new ElementalEffect(
            Set.of(VOLANTE, COLEOTTERO, FUOCO, GHIACCIO),
            Set.of(LOTTA, TERRA),
            Set.of(),
            Set.of());

    private static final ElementalEffect BUG = new ElementalEffect(
            Set.of(VELENO, ERBA, PSICO),
            Set.of(LOTTA, VOLANTE, SPETTRO, FUOCO),
            Set.of(),
            Set.of());

    private static final ElementalEffect GHOST = new ElementalEffect(
            Set.of(SPETTRO),
            Set.of(),
            Set.of(NORMALE, LOTTA),
            Set.of(NORMALE, PSICO));

    private static final ElementalEffect FIRE  = new ElementalEffect(
            Set.of(COLEOTTERO, ERBA, GHIACCIO),
            Set.of(ROCCIA, FUOCO, ACQUA, DRAGO),
            Set.of(),
            Set.of());
    private static final ElementalEffect WATER = new ElementalEffect(
            Set.of(TERRA, ROCCIA, FUOCO),
            Set.of(ERBA, ACQUA, DRAGO),
            Set.of(),
            Set.of());
    private static final ElementalEffect GRASS = new ElementalEffect(
            Set.of(TERRA, ROCCIA, ACQUA),
            Set.of(VOLANTE, VELENO, COLEOTTERO, FUOCO, ERBA, DRAGO),
            Set.of(),
            Set.of());

    private static final ElementalEffect ELECTRIC = new ElementalEffect(
            Set.of(VOLANTE, ACQUA),
            Set.of(ERBA, ELETTRO, DRAGO),
            Set.of(),
            Set.of(TERRA));
    private static final ElementalEffect PSYCHIC  = new ElementalEffect(
            Set.of(LOTTA, VELENO),
            Set.of(PSICO),
            Set.of(),
            Set.of());
    private static final ElementalEffect ICE      = new ElementalEffect(
            Set.of(VOLANTE, TERRA, ERBA, DRAGO),
            Set.of(ACQUA, GHIACCIO),
            Set.of(),
            Set.of());
    private static final ElementalEffect DRAGON   = new ElementalEffect(
            Set.of(DRAGO),
            Set.of(),
            Set.of(),
            Set.of());

    public static final Map<PokemonType, ElementalEffect> getElementalEffects() {
        Map<PokemonType, ElementalEffect> map = new HashMap<>();
        map.put(NORMALE, NORMAL);
        map.put(LOTTA, FIGHTING);
        map.put(VOLANTE, FLYING);
        map.put(VELENO, POISON);
        map.put(TERRA, GROUND);
        map.put(ROCCIA, ROCK);
        map.put(COLEOTTERO, BUG);
        map.put(SPETTRO, GHOST);
        map.put(FUOCO, FIRE);
        map.put(ACQUA, WATER);
        map.put(ERBA, GRASS);
        map.put(ELETTRO, ELECTRIC);
        map.put(PSICO, PSYCHIC);
        map.put(GHIACCIO, ICE);
        map.put(DRAGO, DRAGON);
        return map;
    }

}
