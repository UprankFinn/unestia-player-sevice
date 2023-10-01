package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.Terminal;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketComponentMessage;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.network.basecomponent.BaseComponent;
import net.unestia.playerservice.network.basecomponent.BaseComponentType;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.rank.RankEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendCommand extends Command {

    private final PlayerService playerService;

    public FriendCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (args.length == 0) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§6add <name> §7- send a friend request \n" + PlayerService.FRIENDPREFIX + "§6remove <name> §7- remove friend \n" + PlayerService.FRIENDPREFIX + "§6list §7- get friend list");
            this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§6accept <name> §7- accept a friend request \n" + PlayerService.FRIENDPREFIX + "§6deny <name> §7- deny a friend request \n" + PlayerService.FRIENDPREFIX + "§6jump <name> §7- jump to a friend");

        } else if (args.length == 1) {
            if (args[0].equals("list")) {

                if (playerEntity.getFriends().isEmpty()) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§cYou do not have any friends :c");
                    return;
                }

                this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§7Your friendlist:");

                playerEntity.getFriends().forEach((uuid) -> {
                    PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(uuid);
                    RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + rank.getColor() + player.getName() + " §7- (" + PlayerService.getPlayerStatus(uuid) + "§7)");
                });

            } else if (args[0].equals("requests")) {

                if (playerEntity.getFriendRequests().isEmpty()) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§cYou do not have any friend requests :c");
                    return;
                }

                this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§7Your friend requests:");

                playerEntity.getFriendRequests().forEach((uuid) -> {
                    PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(uuid);
                    RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§7- " + rank.getColor() + player.getName());

                });

            }
        } else if (args.length == 2) {
            if (args[0].equals("add")) {
                String name = args[1];
                if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis player does not exist!");
                    return;
                }
                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
                RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());
                if (player.getFriends().contains(playerEntity.getUUID()) || playerEntity.getFriends().contains(player.getUUID())) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§cYou are already friends!");
                    return;
                }
                if (player.getFriendRequests().contains(playerEntity.getUUID())) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§cYou have already sent a friend request to " + rank.getColor() + player.getName());
                    return;
                }
                if (PlayerService.getOnlinePlayers().containsValue(name)) {
                    this.sendMessage(player.getUUID(), PlayerService.FRIENDPREFIX + "§7You have a friend request from " + rank.getColor() + playerEntity.getName() + " §7received!");

                    List<String> messages = new ArrayList<>();

                    BaseComponent acceptComponent = new BaseComponent("§aaccept", BaseComponentType.RUN_COMMAND, "/friend accept " + name);
                    BaseComponent lineComponent = new BaseComponent(" §7| ", null, null);
                    BaseComponent denyComponent = new BaseComponent("§cdeny", BaseComponentType.RUN_COMMAND, "/friend deny " + name);

                    messages.add(acceptComponent.toString());
                    messages.add(lineComponent.toString());
                    messages.add(denyComponent.toString());

                    player.getChannel().writeAndFlush(new PacketComponentMessage(player.getUUID(), messages));

                }
                player.addFriendRequest(playerEntity.getUUID());
                this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§7You have sent a friend request to " + rank.getColor() + player.getName() + "!");
            } else if (args[0].equals("remove")) {
                String name = args[1];

                if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis player does not exist!");
                    return;
                }
                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
                RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());
                if (!player.getFriends().contains(playerEntity.getUUID()) && !playerEntity.getFriends().contains(player.getUUID())) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "" + rank.getColor() + player.getName() + " §cis not your friend!");
                    return;
                }

                if (PlayerService.getOnlinePlayers().containsValue(name)) {
                    this.sendMessage(player.getUUID(), PlayerService.FRIENDPREFIX + PlayerService.getRankEntityManager().getRank(playerEntity.getRankId()).getColor() + playerEntity.getName() + " §7has ended the Friendship");
                }

                player.removeFriend(playerEntity.getUUID());
                playerEntity.removeFriend(player.getUUID());

                this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§7You have the friendship with " + rank.getColor() + player.getName() + " §7ended!");


            } else if (args[0].equals("accept")) {
                String name = args[1];

                if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis player does not exist!");
                    return;
                }

                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
                RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

                if (!(playerEntity.getFriendRequests().contains(player.getUUID()))) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + rank.getColor() + player.getName() + " §7has not sent you a friend request!");
                    return;
                }

                if (player.getFriends().contains(playerEntity.getUUID()) && playerEntity.getFriends().contains(player.getUUID())) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§cYou are already friends!");
                    return;
                }

                playerEntity.removeFriendRequest(player.getUUID());

                playerEntity.addFriend(player.getUUID());
                player.addFriend(playerEntity.getUUID());

                if (PlayerService.getOnlinePlayers().containsValue(name)) {
                    this.sendMessage(player.getUUID(), PlayerService.FRIENDPREFIX + PlayerService.getRankEntityManager().getRank(playerEntity.getRankId()).getColor() + playerEntity.getName() + " §7has accepted your friend request!");
                }

                this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§7You have accepted the friend request from " + rank.getColor() + player.getName());

            } else if (args[0].equals("deny")) {
                String name = args[1];

                if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis player does not exist!");
                    return;
                }

                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
                RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

                if (!(playerEntity.getFriendRequests().contains(player.getUUID()))) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§cYou have no friend request from " + rank.getColor() + player.getName() + " §creceived!");
                    return;
                }

                if (playerEntity.getFriends().contains(player.getUUID())) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§cYou are already friends!");
                    return;
                }

                if (PlayerService.getOnlinePlayers().containsValue(name)) {
                    this.sendMessage(player.getUUID(), PlayerService.FRIENDPREFIX + PlayerService.getRankEntityManager().getRank(playerEntity.getRankId()).getColor() + playerEntity.getName() + " §7has rejected your friend request!");
                }

                this.sendMessage(playerEntity.getUUID(), PlayerService.FRIENDPREFIX + "§7You declined friend request from " + rank.getColor() + player.getName() + "!");

            }

        }

    }

}
