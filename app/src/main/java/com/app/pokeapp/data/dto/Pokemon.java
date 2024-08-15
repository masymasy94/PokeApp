package com.app.pokeapp.data.dto;

import com.app.pokeapp.data.enums.PokemonType;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    public int id;
    public String name;
    public int strenght;
    public List<PokemonType> types = new ArrayList<>();
    public boolean isEvolved;
    public boolean isEvolvedTwoTimes;
    public String move;



    public String getName(){
        return name;
    }
    public void setStrenght(int strenght){this.strenght = strenght;}
}