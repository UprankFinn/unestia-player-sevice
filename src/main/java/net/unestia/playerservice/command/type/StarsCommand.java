package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;

public class StarsCommand extends Command {

    private final PlayerService playerService;

    public StarsCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {
        if (playerEntity.getCoins() == 1) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "stars_command.one_star"), playerEntity.getStars()));
        } else {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "stars_command.more_stars"), playerEntity.getStars()));
        }
    }
}
