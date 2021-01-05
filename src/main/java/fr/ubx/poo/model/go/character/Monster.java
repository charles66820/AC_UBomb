package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.Door;

import java.util.*;

public class Monster extends Character implements Movable {

    private final long moveFrequency; // In ns
    private long lastCallTime;

    public Monster(Game game, Position position, double moveFrequencyInMs) {
        super(game, position);
        this.moveFrequency = (long) (1000000L * moveFrequencyInMs);
    }

    public void update(long now) {
        // Init lastCallTime
        if (this.lastCallTime == 0) this.lastCallTime = now;

        // Request monster move after a moment
        if ((now - lastCallTime) >= moveFrequency) {
            if (this.game.isSmartAI()) this.smartAI();
            else this.randomAI();
            this.lastCallTime = now;
        }

        // On move
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);

                // Hit the player if is on same position
                Player player = this.game.getPlayer();
                if (this.getPosition().equals(player.getPosition())) {
                    player.removeLives(1);
                    this.game.getPlayer().setInvulnerable(true);
                    this.game.getPlayer().setLastTimeInvulnerable(now);
                }
            }
            moveRequested = false;
        }

    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor decor = this.game.getWorld().get(nextPos);
        // Collision with monster
        for (Monster monster : this.game.getWorld().getMonsters())
            if (nextPos.equals(monster.getPosition())) return false;
        return (this.game.getWorld().isInside(nextPos)) && ((decor == null) || (decor.isTraversable() && !(decor instanceof Door)));
    }

    public void randomAI() {
        this.requestMove(Direction.random());
    }

    public void smartAI() {
        Map<Node, Position> path = A_Start(this.getPosition(), this.game.getPlayer().getPosition());
    }

    static class Node {
        private Node parent;
        public Position p;
        private long gScore; // For infinity
        private long fScore; // For infinity

        Node(Node parent, Position p) {
            this(parent, p, Long.MAX_VALUE, Long.MAX_VALUE);
        }

        Node(Node parent, Position p, long g, long f) {
            this.parent = parent;
            this.p = p;
            this.gScore = g;
            this.fScore = f;
        }

        public long getgScore() {
            return gScore;
        }

        public void setgScore(long gScore) {
            this.gScore = gScore;
        }

        public long getfScore() {
            return fScore;
        }

        public void setfScore(long fScore) {
            this.fScore = fScore;
        }
    }

    private Map<Node, Position> A_Start(Position start, Position goal) { // List<Position>
        Queue<Node> openSet = new PriorityQueue<>(Comparator.comparingLong(Node::getfScore));
        openSet.add(new Node(null, start, 0, h(start, goal)));

        Map<Node, Position> cameFrom = new HashMap<>();

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.p.equals(goal)) {
                return cameFrom;
            }

            openSet.remove(current);
            for (int i = 0; i < 3; i++) {
                Position neighborPos = Direction.values()[i].nextPosition(current.p); // ADD if direction can be trow
                Node neighbor = new Node(current, neighborPos);
                long tentative_gScore = current.getgScore() + d(current.p, neighbor.p);
                if (tentative_gScore < neighbor.getgScore()) {
                    cameFrom.put(neighbor, current.p);
                    neighbor.setgScore(tentative_gScore);
                    neighbor.setfScore(neighbor.getgScore() + h(neighbor.p, goal));
                    if (openSet.stream().noneMatch(e -> e == neighbor)) openSet.add(neighbor);
                }
            }

        }
        return null;
    }

    private long d(Position current, Position goal) { // Distance, Can be buged
        return Math.abs(current.x - goal.x) + Math.abs(current.y - goal.y);
    }

    private long h(Position current, Position goal) { // TODO: ??????
        return Math.abs(current.x - goal.x) + Math.abs(current.y - goal.y);
    }

    public void die() {
        this.alive = false;
    }

}
