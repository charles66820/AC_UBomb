/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.util.Random;

public enum Direction {
    N {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x, pos.y - delta);
        }
    },
    E {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x + delta, pos.y);
        }
    },
    S {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x, pos.y + delta);
        }
    },
    W {
        @Override
        public Position nextPosition(Position pos, int delta) {
            return new Position(pos.x - delta, pos.y);
        }
    },
    ;

    private static final Random randomGenerator = new Random();

    /**
     * @return a pseudorandom direction
     */
    public static Direction random() {
        return values()[randomGenerator.nextInt(values().length)];
    }

    /**
     * Determine the direction to look at a position
     * @param from The start position
     * @param p The look position
     * @return The direction at `form` position to facing the destination
     */
    static public Direction to(Position from, Position p) {
        if (from.x < p.x) return Direction.E;
        else if (from.x > p.x) return Direction.W;
        else if (from.y < p.y) return Direction.S;
        else if (from.y > p.y) return Direction.N;
        else return null;
    }

    public abstract Position nextPosition(Position pos, int delta);

    final public Position nextPosition(Position pos) {
        return nextPosition(pos, 1);
    }

}