package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.network.PacketPlayerSwitchServer;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.rank.RankEntity;
import net.unestia.playerservice.player.rank.RankEntityManager;

import java.text.MessageFormat;

public class SendCommand extends Command {

    private final PlayerService playerService;

    public SendCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (args.length == 2) {
            String name = args[0];

            if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "player_does_not_exist"), null));
                return;
            }

            if (!(PlayerService.getOnlinePlayers().containsValue(name))) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "player_is_not_online"), null));
                return;
            }

            //PlayerService.getPlayerEntityManager().getPlayer(name).getChannel().writeAndFlush(new PacketPlayerSwitchServer(PlayerService.getPlayerEntityManager().getPlayer(name).getUUID(), args[1]));

            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "send_command"), PlayerService.getRankEntityManager().getRank(PlayerService.getPlayerEntityManager().getPlayer(name).getRankId()).getColor(), name, args[1]));

        }

    }
}
