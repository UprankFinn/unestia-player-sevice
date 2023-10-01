package net.unestia.playerservice.party;

import net.unestia.playerservice.PlayerService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyManager {

    private final PlayerService playerService;
    private final Map<UUID, Party> parties;

    public PartyManager(PlayerService playerService) {
        this.playerService = playerService;
        this.parties = new HashMap<>();
    }

    public void createParty(UUID owner, Boolean isPublic, String server) {
        Party party = new Party(owner, isPublic, server);
        this.parties.put(owner, party);
    }

    public void deleteParty(UUID owner) {
        this.parties.remove(owner);
    }

    public Party getParty(UUID owner) {
        if (this.parties.containsKey(owner)) return this.parties.get(owner);
        return null;
    }

    public Party getPartyOfPlayer(UUID uuid) {
        for (Party party : this.parties.values()) {
            if(party.getMembers().equals(uuid) || party.getModerators().equals(uuid) || party.getOwner().equals(uuid)) return party;
        }
        return null;
    }

    public void promote(UUID owner, UUID promote) {
        Party party = this.parties.get(owner);
        if (party.getMembers().contains(promote)) {
            party.getModerators().add(promote);
            party.getMembers().remove(promote);
        }
    }

    public void demote(UUID owner, UUID demote) {
        Party party = this.parties.get(owner);
        if (party.getModerators().contains(demote)) {
            party.getMembers().add(demote);
            party.getModerators().remove(demote);
        }
    }

    public Map<UUID, Party> getParties() {
        return parties;
    }
}
