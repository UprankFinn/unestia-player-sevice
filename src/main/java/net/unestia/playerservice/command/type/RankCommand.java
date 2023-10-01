package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.rank.RankEntity;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RankCommand extends Command {

    private final PlayerService playerService;

    public RankCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        RankEntity rank = PlayerService.getRankEntityManager().getRank(playerEntity.getRankId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        if (rank == null) {
            channel.writeAndFlush(new PacketPlayerMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cYou don´t have an active rank!"));
        }

        if (!(playerEntity.isInTeam())) {

            if (args.length == 0) {

                if (playerEntity.getDuration() == 0L) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§7Your rank is " + rank.getColor() + rank.getName() + " §7and is §epermanently");
                } else {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§7Your rank is " + rank.getColor() + rank.getName() + " §7and runs until §e" + simpleDateFormat.format(playerEntity.getDuration()));
                }

            } else if (args.length == 1) {
                String name = args[0];

                if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis player does not exist!");
                    return;
                }

                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
                RankEntity rankEntity = PlayerService.getRankEntityManager().getRank(player.getRankId());

                if (player.getDuration() == 0L) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + rankEntity.getColor() + name + " §7has the rank " + rankEntity.getColor() + rankEntity.getName() + " §7and is §epermanently");
                } else {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + rankEntity.getColor() + name + " §7has the rank " + rankEntity.getColor() + rankEntity.getName() + " §7and runs until §e" + simpleDateFormat.format(player.getDuration()));
                }
            }
        } else {

            if (args.length == 0) {
                if (playerEntity.getDuration() == 0L) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§7Your rank is " + rank.getColor() + rank.getName() + " §7and is §epermanently");
                } else {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§7Your rank is " + rank.getColor() + rank.getName() + " §7and runs until §e" + simpleDateFormat.format(playerEntity.getDuration()));
                }
            } else if (args.length == 3) {
                if (PlayerService.getRankEntityManager().getRank(args[1]) == null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis rank does not exists!");
                    return;
                }

                RankEntity rankEntity = PlayerService.getRankEntityManager().getRank(args[1]);

                int digits = 0;
                for (int i = 0; i < args[2].length(); i++) {
                    if (args[2].charAt(i) >= 48 && args[2].charAt(i) <= 57) digits++;
                }

                if (args[2].length() - digits != 1) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cuse: /rank <name> <rank> / <duration:h,d,w,m,y>");
                    return;
                }

                long duration = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
                switch (args[2].substring(args[2].length() - 1)) {
                    case "h": {
                        duration = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(Integer.parseInt(args[2].substring(0, args[2].length() - 1)));
                        break;
                    }
                    case "d": {
                        duration = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Integer.parseInt(args[2].substring(0, args[2].length() - 1)));
                        break;
                    }
                    case "w": {
                        duration = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Integer.parseInt(args[2].substring(0, args[2].length() - 1)) * 7L);
                        break;
                    }
                    case "m": {
                        duration = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Integer.parseInt(args[2].substring(0, args[2].length() - 1)) * 31L);
                        break;
                    }
                    case "y": {
                        duration = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Integer.parseInt(args[2].substring(0, args[2].length() - 1)) * 365L);
                        break;
                    }
                }

                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(args[0]);
                player.setRank(PlayerService.getRankEntityManager().getRank(args[1]).getId());
                player.setDuration(duration);

                this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§7Rank set " + duration);

            }

        }

    }
}
