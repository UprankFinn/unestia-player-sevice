package net.unestia.playerservice.player.rank;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.unestia.playerservice.PlayerService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RankEntityManager {

    private final PlayerService playerService;

    private final LoadingCache<Integer, RankEntity> ranks;

    private final Gson gson;
    private final MongoCollection collection;

    public RankEntityManager(PlayerService playerService) {
        this.playerService = playerService;
        this.gson = new GsonBuilder().create();

        this.collection = PlayerService.getMongoClient().getDatabase("player").getCollection("ranks");

        this.ranks = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<>() {
            @Override
            public RankEntity load(Integer id) throws Exception {
                return gson.fromJson(gson.toJson(collection.find(Filters.eq("id", id)).first()), RankEntity.class);
            }
        });
    }

    public RankEntity getRank(Integer id) {
        try {
            return this.ranks.get(id);
        } catch (ExecutionException e) {
            return null;
        }
    }

    public RankEntity getRank(String name) {
        try {
            for (RankEntity rank : this.ranks.asMap().values()) {
                if (rank.getName().equals(name)) {
                    return this.ranks.get(rank.getId());
                }
            }

        } catch (ExecutionException e) {
            return null;
        }
        return null;
    }

}
