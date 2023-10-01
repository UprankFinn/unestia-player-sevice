package net.unestia.playerservice.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.party.Party;
import net.unestia.playerservice.player.rank.RankEntity;

import java.util.*;

public class PlayerEntity {

    private final UUID uuid;
    private String name;

    private final long firstJoinDate;
    private long lastJoinDate;

    private String language;

    private Integer rankId;
    private long duration;

    private String teamSpeakId;

    private final List<UUID> friends;
    private final List<UUID> friendRequests;

    private final String clan;

    private Integer coins;
    private Integer stars;

    private String apiKey;
    private Boolean notification;

    //====================================

    private Channel channel;
    private Party party;
    private String server;

    public PlayerEntity(UUID uuid, String name, long firstJoinDate, long lastJoinDate, String language, Integer rankId, long duration, String teamSpeakId, String clan, Integer coins, Integer stars, String apiKey, Boolean notification) {
        this.uuid = uuid;
        this.name = name;

        this.firstJoinDate = firstJoinDate;
        this.lastJoinDate = lastJoinDate;

        this.language = language;

        this.rankId = rankId;
        this.duration = duration;

        this.teamSpeakId = teamSpeakId;

        this.friends = new ArrayList<>();
        this.friendRequests = new ArrayList<>();

        this.clan = clan;

        this.coins = coins;
        this.stars = stars;

        this.apiKey = apiKey;
        this.notification = notification;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("name", name));
    }

    public long getFirstJoinDate() {
        return firstJoinDate;
    }

    public long getLastJoinDate() {
        return lastJoinDate;
    }

    public void setLastJoinDate(long lastJoinDate) {
        this.lastJoinDate = lastJoinDate;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("lastJoinDate", lastJoinDate));
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("language", language));
    }

    public Integer getRankId() {
        return rankId;
    }

    public void setRank(Integer rankId) {
        this.rankId = rankId;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("rankId", rankId));
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("duration", duration));
    }

    public String getTeamSpeakId() {
        return teamSpeakId;
    }

    public void setTeamSpeakId(String teamSpeakId) {
        this.teamSpeakId = teamSpeakId;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("teamSpeakId", teamSpeakId));
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public List<UUID> getFriendRequests() {
        return friendRequests;
    }

    public String getClan() {
        return clan;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("coins", coins));
    }

    public void addCoins(Integer coins) {
        this.coins += coins;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("coins", this.coins));

        System.out.println("this.getCoins() " + this.coins);
        System.out.println(this.coins+=coins);

    }

    public void removeCoins(Integer coins) {
        this.coins -= coins;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("coins", this.coins));
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("stars", stars));
    }

    public void addStars(Integer stars) {
        this.stars += stars;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("stars", this.stars));
    }

    public void removeStars(Integer stars) {
        this.stars -= stars;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("stars", this.stars));
    }

    public void addFriend(UUID uuid) {
        this.friends.add(uuid);
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("friends", friends));
    }

    public void removeFriend(UUID uuid) {
        this.friends.remove(uuid);
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("friends", friends));
    }

    public void addFriendRequest(UUID uuid) {
        this.friendRequests.add(uuid);
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("friendRequests", friendRequests));
    }

    public void removeFriendRequest(UUID uuid) {
        this.friendRequests.remove(uuid);
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("friendRequests", friendRequests));
    }

    public String getAPIKey() {
        return this.apiKey;
    }

    public void setAPIKey(String apiKey) {
        this.apiKey = apiKey;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("apiKey", apiKey));
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
        PlayerService.getMongoClient().getDatabase("player").getCollection("profiles").updateOne(
                Filters.eq("uuid", this.uuid.toString()), Updates.set("notification", notification));
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Boolean hasPermission(String permission) {
        RankEntity rank = PlayerService.getRankEntityManager().getRank(rankId);
        if (rank.getPermission().contains(permission)) {
            return true;
        } else {
            return false;
        }
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Boolean isInTeam() {
        if (rankId == 1 || rankId == 2 || rankId == 3 || rankId == 4 || rankId == 5 || rankId == 6) {
            return true;
        } else {
            return false;
        }
    }

}
