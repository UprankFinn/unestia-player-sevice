package net.unestia.playerservice.player.ban.reason;

import lombok.Getter;

import java.sql.Time;
import java.util.concurrent.TimeUnit;


@Getter
public enum ReasonType {

    HACKING("HACKING", TimeUnit.DAYS.toMillis(30)),
    TROLLING("TROLLING", TimeUnit.DAYS.toMillis(5)),
    TEAMING("TEAMING", TimeUnit.DAYS.toMillis(5)),
    NAME("NAME", TimeUnit.DAYS.toMillis(5)),
    SKIN("SKIN", TimeUnit.DAYS.toMillis(5)),
    CHAT("CHAT", TimeUnit.DAYS.toMillis(5)),
    CLAN("CLAN", TimeUnit.DAYS.toMillis(5)),
    PROVOCATION("PROVOCATION", TimeUnit.MILLISECONDS.toMillis(10)),
    BAN_BYPASS("BAN_BYPASS", TimeUnit.MILLISECONDS.toMillis(365)),
    ADMIN_BAN("ADMIN_BAN", TimeUnit.MILLISECONDS.toMillis(Integer.MAX_VALUE));

    private final String key;
    private final long duration;

    ReasonType(String key, long duration) {
        this.key = key;
        this.duration = duration;
    }

    public static boolean exists(String name) {
        for (ReasonType type : ReasonType.values()) {
            if (type.name().equals(name.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

}
