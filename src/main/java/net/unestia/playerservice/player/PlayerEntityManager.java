package net.unestia.playerservice.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.Terminal;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEntityManager {

    private final PlayerService playerService;

    private final Gson gson;
    private final Map<UUID, PlayerEntity> players;

    private final MongoCollection<Document> collection;

    public PlayerEntityManager(PlayerService playerService) {
        this.playerService = playerService;

        this.gson = new GsonBuilder().create();
        this.players = new HashMap<>();

        this.collection = PlayerService.getMongoClient().getDatabase("player").getCollection("profiles");
        this.collection.find().forEach((Block<? super Document>) document -> {
            PlayerEntity playerEntity = this.gson.fromJson(this.gson.toJson(document), PlayerEntity.class);
            this.players.put(playerEntity.getUUID(), playerEntity);
        });
    }

    public PlayerEntity getPlayer(UUID uuid) {
        if (PlayerService.getOnlinePlayers().containsKey(uuid)) {
            return this.players.get(uuid);
        } else {
            if (this.collection.find(Filters.eq("uuid", uuid.toString())).first() != null) {
                return this.gson.fromJson(this.gson.toJson(collection.find(Filters.eq("uuid", uuid.toString())).first()), PlayerEntity.class);
            }
        }
        return null;
    }

    public PlayerEntity getPlayer(String name) {
        if (PlayerService.getOnlinePlayers().containsValue(name)) {
            for (PlayerEntity playerEntity : this.players.values()) {
                if (playerEntity.getName().equals(name)) {
                    return playerEntity;
                }
            }
        } else {
            if (this.collection.find(Filters.eq("name", name)).first() != null) {
                return this.gson.fromJson(this.gson.toJson(collection.find(Filters.eq("name", name)).first()), PlayerEntity.class);
            }
        }
        return null;
    }

    public void createPlayer(PlayerEntity playerEntity) {
        if (this.collection.find(Filters.eq("uuid", playerEntity.getUUID().toString())).first() == null) {
            this.collection.insertOne(this.gson.fromJson(this.gson.toJson(playerEntity), Document.class));
        }

    }

    public void addPlayerToCach(UUID uuid, PlayerEntity playerEntity) {
        this.players.put(uuid, playerEntity);
    }

    public Gson getGson() {
        return gson;
    }

    public Map<UUID, PlayerEntity> getPlayers() {
        return players;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
}

