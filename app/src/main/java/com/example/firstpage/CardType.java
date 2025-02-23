package com.example.firstpage;

public enum CardType {
    HEAL,       // +25 HP (banana_cards)
    ENERGY,     // +1 Energy (almond_cards)
    SHIELD,     // +40 Shields (peas_cards)
    POISON,     // 3 DPS for 5 sec (15 total damage) (meat_cards)
    SLASH,      // 120 burst damage (blocked by shields) (cheese_cards)
    PIERCING,    // 50 damage that ignores shields (shrimp_cards)
    NORMAL_ATTACK // 20 damage no energy cost
}