package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;

public class ListCommand extends Command {

    private final PlayerService playerService;

    public ListCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (PlayerService.getOnlinePlayers().size() == 0) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "list_command.no_player_online"), PlayerService.getOnlinePlayers().size()));
        } else if (PlayerService.getOnlinePlayers().size() == 1) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "list_command.one_player_online"), PlayerService.getOnlinePlayers().size()));
        } else {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "list_command.more_players_online"), PlayerService.getOnlinePlayers().size()));
        }
    }
}
