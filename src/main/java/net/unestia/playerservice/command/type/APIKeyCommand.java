package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;
import java.util.UUID;

public class APIKeyCommand extends Command {

    private final PlayerService playerService;

    public APIKeyCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        UUID uuid = UUID.randomUUID();

        playerEntity.setAPIKey(uuid.toString());
        this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(),"api_key_message"), uuid.toString()));

    }
}
