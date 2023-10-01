package net.unestia.playerservice;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import io.netty.channel.Channel;
import net.unestia.playerservice.command.CommandManager;
import net.unestia.playerservice.network.*;
import net.unestia.playerservice.packet.PacketReader;
import net.unestia.playerservice.party.PartyManager;
import net.unestia.playerservice.player.PlayerEntityManager;
import net.unestia.playerservice.player.ban.BanManager;
import net.unestia.playerservice.player.rank.RankEntityManager;
import net.unestia.playerservice.server.ServerManager;
import net.unestia.playerservice.translation.TranslationManager;
import net.unestia.protocol.Protocol;
import net.unestia.protocol.socket.server.SocketServer;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerService {

    private static PlayerService instance;

    private static MongoClient mongoClient;

    private static SocketServer socketServer;

    private static PartyManager partyManager;
    private static PlayerEntityManager playerEntityManager;
    private static RankEntityManager rankEntityManager;
    private static BanManager banManager;
    private static ServerManager serverManager;
    private static TranslationManager translationManager;

    private static Map<UUID, String> onlinePlayers;
    private static Map<UUID, String> currentServer;

    public static final String PREFIX = "§8[§6UnestiaMC§8] §r";
    public static final String CLANPREFIX = "§8[§6Clan§8] §r";
    public static final String FRIENDPREFIX = "§8[§6Friend§8] §r";
    public static final String REPORTPREFIX = "§8[§6Report§8] §r";
    public static final String BANPREFIX = "§8[§6Ban§8] §r";
    public static final String PARTYPREFIX = "§8[§6Party§8] §r";

    public static Boolean maintenance;

    private static Terminal terminal;

    private static boolean running;
    private static Map<UUID, Channel> channels = new HashMap<>();

    public PlayerService() {
        instance = this;

        Logger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
        Logger.getLogger("com.mongodb.diagnostics.logging.JULLogger").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.insert").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.query").setLevel(Level.OFF);
        Logger.getLogger("org.mongodb.driver.update").setLevel(Level.OFF);
    }

    public static void main(String[] args) throws IOException {

        terminal = new Terminal();
        Terminal.setActive();

        //new ConfigFile();

        onlinePlayers = new HashMap<>();
        currentServer = new HashMap<>();

        socketServer = (SocketServer) Protocol.createServer(9046);

        mongoClient = new MongoClient(new MongoClientURI("mongodb://root:BYmZsan2sPqDL4V#SuRHkAeMuN#yerCE@45.142.112.175:27017/admin"));

        NetworkUtil.registerPackets(socketServer);
        maintenance = true;

        partyManager = new PartyManager(PlayerService.getInstance());
        playerEntityManager = new PlayerEntityManager(PlayerService.getInstance());
        rankEntityManager = new RankEntityManager(PlayerService.getInstance());
        banManager = new BanManager(PlayerService.getInstance());
        serverManager = new ServerManager(PlayerService.getInstance());
        translationManager = new TranslationManager();

        new PacketReader(PlayerService.getInstance());
        new CommandManager(PlayerService.getInstance());

        Runtime.getRuntime().addShutdownHook(new Thread(PlayerService::shutdown));

    }

    public static void shutdown() {

        Terminal.setRunning(false);
        Terminal.setWaiting(false);

        //new TeamSpeakBot(PlayerService.getInstance()).onDisable();

        mongoClient.close();
    }

    public static PlayerService getInstance() {
        return instance;
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static SocketServer getSocketServer() {
        return socketServer;
    }

    public static PartyManager getPartyManager() {
        return partyManager;
    }

    public static PlayerEntityManager getPlayerEntityManager() {
        return playerEntityManager;
    }

    public static RankEntityManager getRankEntityManager() {
        return rankEntityManager;
    }

    public static BanManager getBanManager() {
        return banManager;
    }

    public static ServerManager getServerManager() {
        return serverManager;
    }

    public static TranslationManager getTranslationManager() {
        return translationManager;
    }

    public static Map<UUID, String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public static Map<UUID, String> getCurrentServer() {
        return currentServer;
    }

    public static String getPlayerStatus(UUID uuid) {
        if (onlinePlayers.containsKey(uuid)) {
            if (PlayerService.getPlayerEntityManager().getPlayer(uuid).getServer().contains("lobby")) {
                return "§aOnline §eauf Lobby";
            } else if (PlayerService.getPlayerEntityManager().getPlayer(uuid).getServer().contains("skyblock")) {
                return "§aOnline §eauf Skyblock";
            } else {
                return "§aOnline §eauf " + (PlayerService.getPlayerEntityManager().getPlayer(uuid).getServer() == null ? "null" : "" + PlayerService.getPlayerEntityManager().getPlayer(uuid).getServer());
            }
        } else {
            return "§cOffline";
        }
    }

    public static String getUsageMessage(String prefix, String command, String help) {
        return prefix + "§cuse: §e" + command + " §7- " + help;
    }

    public static void setMaintenance(Boolean maintenance) {
        PlayerService.maintenance = maintenance;
    }

    public static Boolean isMaintenance() {
        return maintenance;
    }

    public static Map<UUID, Channel> getChannels() {
        return channels;
    }
}
