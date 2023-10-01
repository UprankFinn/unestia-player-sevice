package net.unestia.playerservice.packet;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.Terminal;
import net.unestia.playerservice.network.*;
import net.unestia.playerservice.network.coins.PacketPlayerAddCoins;
import net.unestia.playerservice.network.coins.PacketPlayerRemoveCoins;
import net.unestia.playerservice.network.conainer.PacketContainerCreate;
import net.unestia.playerservice.network.stars.PacketPlayerAddStars;
import net.unestia.playerservice.network.stars.PacketPlayerRemoveStars;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.ban.reason.ReasonType;
import net.unestia.playerservice.player.rank.RankEntity;

import java.text.SimpleDateFormat;

public class PacketReader {

    private final PlayerService playerService;

    public PacketReader(PlayerService playerService) {
        this.playerService = playerService;

        PlayerService.getSocketServer().registerListener(PacketProxyRegister.class, (channelHandlerContext, packetProxyRegister) -> {

            if (!(PlayerService.getChannels().containsKey(packetProxyRegister.getUUID()))) {
                PlayerService.getChannels().put(packetProxyRegister.getUUID(), channelHandlerContext.channel());
            }

            PlayerService.getChannels().forEach((uuid, channel) -> {

                if (PlayerService.isMaintenance()) {
                    channel.writeAndFlush(new PacketMaintenanceUpdate(true));
                } else {
                    channel.writeAndFlush(new PacketMaintenanceUpdate(false));
                }

            });

        });
        PlayerService.getSocketServer().registerListener(PacketProxyUnregister.class, (channelHandlerContext, packetProxyUnregister) -> {

            if (PlayerService.getChannels().containsKey(packetProxyUnregister.getUUID()))
                PlayerService.getChannels().remove(packetProxyUnregister.getUUID());

            PlayerService.getChannels().forEach((uuid, channel) -> {
                System.out.println(uuid.toString());
            });

        });

        PlayerService.getSocketServer().registerListener(PacketPlayerLogin.class, (channelHandlerContext, packetPlayerLogin) -> {

            PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID());
            RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

            Terminal.info("player login " + packetPlayerLogin.getName());

            if (PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID()) == null) {
                PlayerService.getPlayerEntityManager().createPlayer(new PlayerEntity(packetPlayerLogin.getUUID(), packetPlayerLogin.getName(), System.currentTimeMillis(), System.currentTimeMillis(),
                        "en_US", 0, 0L, null, "null", 1000, 0, "", false));
            }

            PlayerService.getPlayerEntityManager().addPlayerToCach(packetPlayerLogin.getUUID(), new Gson().fromJson(new Gson().toJson(PlayerService.getPlayerEntityManager().getCollection().find(Filters.eq("uuid", packetPlayerLogin.getUUID().toString())).first()), PlayerEntity.class));

            PlayerService.getOnlinePlayers().put(packetPlayerLogin.getUUID(), packetPlayerLogin.getName());

            if (player.getDuration() != 0L) {
                if (player.getDuration() <= System.currentTimeMillis()) {
                    player.setRank(10);
                    player.setDuration(0L);
                }
            }

            if (!(PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID()).isInTeam())) {
                if (PlayerService.isMaintenance()) {
                    channelHandlerContext.channel().writeAndFlush(new PacketPlayerDisconnect(packetPlayerLogin.getUUID(), " §6§lᴜɴᴇsᴛɪᴀ ɴᴇᴛᴡᴏʀᴋ \n\n§cWe are currently in Maintenance Mode"));
                }
                return;
            }

            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID()).setChannel(channelHandlerContext.channel());
            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID()).setParty(null);

            if (PlayerService.getBanManager().getBan(packetPlayerLogin.getUUID()) != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID()).getChannel().writeAndFlush(new PacketPlayerDisconnect(packetPlayerLogin.getUUID(), "§6§lᴜɴᴇsᴛɪᴀ ɴᴇᴛᴡᴏʀᴋ \n\n§cDu wurdest vom Netzwerk gebannt!\n\n§7Grund: §c" + PlayerService.getBanManager().getBan(packetPlayerLogin.getUUID()).getReason().replace("_", " ") + "\n§7Gebannt bis zum: §c" + simpleDateFormat.format(PlayerService.getBanManager().getBan(packetPlayerLogin.getUUID()).getTime() + ReasonType.valueOf(PlayerService.getBanManager().getBan(packetPlayerLogin.getUUID()).getReason()).getDuration())));
            }

            PlayerService.getChannels().forEach((uuid, channel) -> {
                channel.writeAndFlush(new PacketPlayerCount(PlayerService.getOnlinePlayers().size()));
            });

            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID()).getFriends().forEach((uuid) -> {

                if (!(PlayerService.getOnlinePlayers().containsKey(uuid))) return;

                PlayerEntity friend = PlayerService.getPlayerEntityManager().getPlayer(uuid);
                PlayerEntity join = PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogin.getUUID());

                RankEntity rankFriend = PlayerService.getRankEntityManager().getRank(friend.getRankId());

                friend.getChannel().writeAndFlush(new PacketPlayerMessage(friend.getUUID(), PlayerService.FRIENDPREFIX + rankFriend.getColor() + join.getName() + " §7is now §aonline"));
            });
            player.setParty(null);
            player.setServer("Lobby");

        });

        PlayerService.getSocketServer().registerListener(PacketPlayerSwitchServer.class, (channelHandlerContext, packetPlayerSwitchServer) -> {
            if (packetPlayerSwitchServer.getContainer() == null) {
                PlayerService.getPlayerEntityManager().getPlayer(packetPlayerSwitchServer.getUUID()).setServer(packetPlayerSwitchServer.getServer());
            } else {
                PlayerService.getPlayerEntityManager().getPlayer(packetPlayerSwitchServer.getUUID()).setServer(packetPlayerSwitchServer.getServer() + ":" + packetPlayerSwitchServer.getContainer());
            }
        });

        PlayerService.getSocketServer().registerListener(PacketPlayerLogout.class, (channelHandlerContext, packetPlayerLogout) -> {
            Terminal.info("player logout " + packetPlayerLogout.getUUID());

            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogout.getUUID()).getFriends().forEach((uuid) -> {
                if (!(PlayerService.getOnlinePlayers().containsKey(uuid))) return;

                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(uuid);
                PlayerEntity quit = PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogout.getUUID());

                RankEntity rank = PlayerService.getRankEntityManager().getRank(quit.getRankId());
                System.out.println(player.getChannel().isOpen() || player.getChannel().isActive() || player.getChannel().isRegistered());
                player.getChannel().writeAndFlush(new PacketPlayerMessage(player.getUUID(), PlayerService.FRIENDPREFIX + rank.getColor() + quit.getName() + " §7is now §coffline"));
            });

            PlayerService.getChannels().forEach((uuid, channel) -> {
                channel.writeAndFlush(new PacketPlayerCount(PlayerService.getChannels().size()));
            });
            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerLogout.getUUID()).setChannel(null);
            PlayerService.getOnlinePlayers().remove(packetPlayerLogout.getUUID());
        });

        PlayerService.getSocketServer().registerListener(PacketPlayerAddCoins.class, (channelHandlerContext, packetPlayerAddCoins) -> {
            if (PlayerService.getPlayerEntityManager().getPlayer(packetPlayerAddCoins.getUUID()) == null) return;
            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerAddCoins.getUUID()).addCoins(packetPlayerAddCoins.getCoins());
            System.out.println(packetPlayerAddCoins.getCoins());
        });

        PlayerService.getSocketServer().registerListener(PacketPlayerRemoveCoins.class, (channelHandlerContext, packetPlayerRemoveCoins) -> {
            if (PlayerService.getPlayerEntityManager().getPlayer(packetPlayerRemoveCoins.getUUID()) == null) return;
            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerRemoveCoins.getUUID()).removeCoins(packetPlayerRemoveCoins.getCoins());
        });

        PlayerService.getSocketServer().registerListener(PacketPlayerAddStars.class, (channelHandlerContext, packetPlayerAddStars) -> {
            if (PlayerService.getPlayerEntityManager().getPlayer(packetPlayerAddStars.getUUID()) == null) return;
            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerAddStars.getUUID()).addStars(packetPlayerAddStars.getStars());
        });

        PlayerService.getSocketServer().registerListener(PacketPlayerRemoveStars.class, (channelHandlerContext, packetPlayerRemoveStars) -> {
            if (PlayerService.getPlayerEntityManager().getPlayer(packetPlayerRemoveStars.getUUID()) == null) return;
            PlayerService.getPlayerEntityManager().getPlayer(packetPlayerRemoveStars.getUUID()).removeStars(packetPlayerRemoveStars.getStars());
        });

        PlayerService.getSocketServer().registerListener(PacketContainerCreate.class, (channelHandlerContext, packetContainerCreate) -> {
            if (!(PlayerService.getServerManager().getServerContainers().containsKey(packetContainerCreate.getServerName()) && !(PlayerService.getServerManager().getServerContainers().containsValue(packetContainerCreate.getContainerName())))) {
                PlayerService.getServerManager().getServerContainers().put(packetContainerCreate.getServerName(), packetContainerCreate.getContainerName());
            }
        });

        PlayerService.getSocketServer().registerListener(PacketContainerCreate.class, (channelHandlerContext, packetContainerDelete) -> {
            if (PlayerService.getServerManager().getServerContainers().containsKey(packetContainerDelete.getServerName()) && PlayerService.getServerManager().getServerContainers().containsValue(packetContainerDelete.getContainerName())) {
                PlayerService.getServerManager().getServerContainers().remove(packetContainerDelete.getServerName(), packetContainerDelete.getContainerName());
            }
        });

    }

}
