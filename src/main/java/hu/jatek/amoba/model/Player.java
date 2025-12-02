package hu.jatek.amoba.model;

import java.util.Objects;

public final class Player {

    private final String name;
    private final char symbol;
    private final boolean human;

    public Player(String name, char symbol, boolean human) {
        this.name = Objects.requireNonNull(name);
        this.symbol = symbol;
        this.human = human;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isHuman() {
        return human;
    }

    @Override
    public String toString() {
        return "Player{name='%s', symbol=%s, human=%s}"
                .formatted(name, symbol, human);
    }
}
