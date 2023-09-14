package com.app.pokeapp.data.dto;

import com.app.pokeapp.data.enums.PokemonType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class ResultElements {

    private Map<PokemonType, BigDecimal> elements;
    private Set<PokemonType> immunities;

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