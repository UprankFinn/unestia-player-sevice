package net.unestia.playerservice.party;

import net.unestia.playerservice.player.ban.Ban;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {

    private UUID owner;
    private Boolean isPublic;
    private List<UUID> moderators;
    private List<UUID> members;
    private List<UUID> requests;
    private String server;

    public Party(UUID owner, Boolean isPublic, String server) {
        this.owner = owner;
        this.isPublic = isPublic;
        this.moderators = new ArrayList<>();
        this.members = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.server = server;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public void setModerators(List<UUID> moderators) {
        this.moderators = moderators;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public void setRequests(List<UUID> requests) {
        this.requests = requests;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public UUID getOwner() {
        return owner;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public List<UUID> getModerators() {
        return moderators;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public List<UUID> getRequests() {
        return requests;
    }

    public String getServer() {
        return server;
    }
}
