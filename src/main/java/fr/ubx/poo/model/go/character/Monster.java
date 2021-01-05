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

    static public final Boolean DEBUG_PATHFINDING = false;
    private List<Position> path;

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
        return this.canMove(this.getPosition(), direction);
    }

    private boolean canMove(Position form, Direction direction) {
        Position nextPos = direction.nextPosition(form);
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
        List<Position> path = A_Start(this.getPosition(), this.game.getPlayer().getPosition());
        Direction d = null;
        if (path != null) d = Direction.to(this.getPosition(), path.get(1));
        this.requestMove(d != null ? d : Direction.random());
    }

    static class Node {
        private final Node parent;
        public Position p;
        private long gScore;
        private long fScore;

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

    private List<Position> A_Start(Position start, Position goal) {
        Queue<Node> openSet = new PriorityQueue<>(Comparator.comparingLong(Node::getfScore));
        openSet.add(new Node(null, start, 0, d(start, goal))); // d = h is distance

        Map<Node, Position> cameFrom = new HashMap<>(); // paths

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // On goal is reached
            if (current.p.equals(goal)) {
                Node pathHead = cameFrom.keySet().stream()
                        .filter(e -> e.p.equals(this.game.getPlayer().getPosition()))
                        .findAny()
                        .orElse(null);

                if (pathHead == null) return null;

                List<Position> total_path = new ArrayList<>();
                do {
                    total_path.add(pathHead.p);
                    pathHead = pathHead.parent;
                }
                while (pathHead != null);
                Collections.reverse(total_path);
                if (DEBUG_PATHFINDING) this.path = total_path;
                return total_path;
            }

            // Search paths
            for (int i = 0; i < 4; i++) {
                Direction d = Direction.values()[i];
                if (this.canMove(current.p, d)) {
                    Position neighborPos = d.nextPosition(current.p);
                    if (!cameFrom.containsValue(neighborPos)) { // If position is already test
                        Node neighbor = new Node(current, neighborPos);
                        long tentative_gScore = current.getgScore() + d(current.p, neighbor.p);
                        if (tentative_gScore < neighbor.getgScore()) {
                            cameFrom.put(neighbor, current.p);
                            neighbor.setgScore(tentative_gScore);
                            neighbor.setfScore(neighbor.getgScore() + d(neighbor.p, goal)); // d = h is distance
                            if (openSet.stream().noneMatch(e -> e.p.equals(neighbor.p))) openSet.add(neighbor);
                        }
                    }
                }
            }
        }
        return null;
    }

    private long d(Position current, Position goal) { // Distance, Can be buged
        return Math.abs(current.x - goal.x) + Math.abs(current.y - goal.y);
    }

    public void die() {
        this.alive = false;
    }

    // For debug
    public List<Position> getPath() {
        return this.path;
    }

}
