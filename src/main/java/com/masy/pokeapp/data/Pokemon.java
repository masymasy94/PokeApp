package com.masy.pokeapp.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Pokemon {
    String name;
    int strenght;
    String type;
    String type2;
    boolean isEvolved;
    boolean isEvolvedTwoTimes;

}
