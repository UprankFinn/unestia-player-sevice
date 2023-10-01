package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;

public class WhereamiCommand extends Command {

    private final PlayerService playerService;

    public WhereamiCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "whereami_command"), PlayerService.getCurrentServer().get(playerEntity.getUUID())));

    }
}
