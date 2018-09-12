package com.alevel.jdbctest.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class Game {
    private final Long id;
    private final Long winner_id;
    private final Long score;
    private final List<Long> players = new LinkedList<>();

    public Game(Long id, Long winner_id, Long score) {
        this.id = id;
        this.winner_id = winner_id;
        this.score = score;
    }

    public Game(Long id, Long winner_id, Long score, long pl_id) {
        this.id = id;
        this.winner_id = winner_id;
        this.score = score;
        this.players.add(pl_id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) &&
                Objects.equals(winner_id, game.winner_id) &&
                Objects.equals(score, game.score) &&
                Objects.equals(players, game.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, winner_id, score, players);
    }

    public Long getId() {
        return id;
    }

    public Long getWinner_id() {
        return winner_id;
    }

    public Long getScore() {
        return score;
    }

    public List<Long> getPlayers() {
        return players;
    }

}
