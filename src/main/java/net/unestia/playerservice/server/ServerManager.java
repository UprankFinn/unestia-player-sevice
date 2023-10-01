package net.unestia.playerservice.server;

import net.unestia.playerservice.PlayerService;

import java.util.HashMap;
import java.util.Map;

public class ServerManager {

    private final PlayerService playerService;

    private final Map<String, String> serverContainers;

    public ServerManager(PlayerService playerService) {
        this.playerService = playerService;

        this.serverContainers = new HashMap<>();
    }

    public Map<String, String> getServerContainers() {
        return serverContainers;
    }
}
