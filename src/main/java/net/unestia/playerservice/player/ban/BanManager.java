package net.unestia.playerservice.player.ban;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager {

    private final PlayerService playerService;

    private final Gson gson;
    private final Map<UUID, Ban> bans;

    private final MongoCollection<Document> collection;

    public BanManager(PlayerService playerService) {
        this.playerService = playerService;

        this.gson = new GsonBuilder().create();
        this.bans = new HashMap<>();

        this.collection = PlayerService.getMongoClient().getDatabase("player").getCollection("bans");
        this.collection.find().forEach((Block<? super Document>) document -> {
            Ban ban = this.gson.fromJson(this.gson.toJson(document), Ban.class);
            this.bans.put(ban.getUUID(), ban);
        });
    }

    public Ban getBan(UUID uuid) {
        if (PlayerService.getOnlinePlayers().containsKey(uuid)) {
            return this.bans.get(uuid);
        } else {
            if (this.collection.find(Filters.eq("uuid", uuid.toString())).first() != null) {
                return this.gson.fromJson(this.gson.toJson(collection.find(Filters.eq("uuid", uuid.toString())).first()), Ban.class);
            }
        }
        return null;
    }

    public void deleteBan(UUID uniqueId) {
        if (this.collection.find(Filters.eq("uuid", uniqueId.toString())).first() != null) {
            this.collection.deleteOne(Filters.eq("uuid", uniqueId.toString()));
            this.bans.remove(uniqueId);
        }
    }

    public void addBanToCach(UUID uuid, Ban ban) {
        this.bans.put(uuid, ban);
    }

    public void removeBanFromCach(UUID uuid) {
        this.bans.remove(uuid);
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }

    public Gson getGson() {
        return gson;
    }
}
