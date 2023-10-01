package net.unestia.playerservice.player.rank;

import java.util.ArrayList;
import java.util.List;

public class RankEntity {

    private final Integer id;
    private final String name;

    private final String color;
    private final String prefix;

    private final Integer teamSpeakId;
    private final long discordId;

    private final List<String> permission;

    public RankEntity(Integer id, String name, String color, String prefix, Integer teamSpeakId, long discordId) {
        this.id = id;
        this.name = name;

        this.color = color;
        this.prefix = prefix;

        this.teamSpeakId = teamSpeakId;
        this.discordId = discordId;

        this.permission = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getPrefix() {
        return prefix;
    }

    public Integer getTeamSpeakId() {
        return teamSpeakId;
    }

    public long getDiscordId() {
        return discordId;
    }

    public List<String> getPermission() {
        return permission;
    }

}
