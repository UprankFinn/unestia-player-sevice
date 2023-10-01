package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerDisconnect;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.rank.RankEntity;

import java.text.MessageFormat;
import java.util.UUID;

public class KickCommand extends Command {

    private final PlayerService playerService;

    public KickCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (args.length < 2) {
            return;
        }

        String name = args[0];

        if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "player_does_not_exist"), null));
            return;
        }


        PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
        RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

        if (!(PlayerService.getOnlinePlayers().containsValue(name))) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PARTYPREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(player.getLanguage(), "player_is_not_online"), null));
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            stringBuilder.append(args[i]).append(" ");
        }

        player.getChannel().writeAndFlush(new PacketPlayerDisconnect(player.getUUID(), "\n§6§lUnestia Network\n\n§cYou were kicked from the network!\n§7Reason: §e" + stringBuilder.toString().replace("§", "&")));
        this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(player.getLanguage(), "kick_command.player"), rank.getColor(), player.getName(), stringBuilder.toString().replace("§", "&")));
    }


}
