package com.alevel.jdbctest.player;

import java.util.Objects;

public class Player {
    private final Long id;
    private final String nickname;
    private final String rank;
    private final Long score;

    public Player(String nickname) {
        this.nickname = nickname;
        this.rank = null;
        this.id = null;
        this.score = 0L;
    }

    public Player(Long id, String nickname, String rank, Long score) {
        this.id = id;
        this.nickname = nickname;
        this.rank = rank;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRank() {
        return rank;
    }

    public Long getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id &&
                score == player.score &&
                Objects.equals(nickname, player.nickname) &&
                Objects.equals(rank, player.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, rank, score);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", rank='" + rank + '\'' +
                ", score=" + score +
                '}';
    }

}
