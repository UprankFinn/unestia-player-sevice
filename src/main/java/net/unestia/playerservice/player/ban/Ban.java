package net.unestia.playerservice.player.ban;

import java.util.UUID;

public class Ban {

    private final UUID uuid;
    private long duration;
    private final String reason;
    private final long time;
    private final String server;
    private final UUID banner;

    public Ban(UUID uuid, long duration, String reason, long time, String server, UUID banner) {
        this.uuid = uuid;
        this.duration = duration;
        this.reason = reason;
        this.time = time;
        this.server = server;
        this.banner = banner;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public String getServer() {
        return server;
    }

    public UUID getBanner() {
        return banner;
    }
}
