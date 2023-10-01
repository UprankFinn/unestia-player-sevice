package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketAlert;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;
import java.util.UUID;

public class AlertCommand extends Command {

    private final PlayerService playerService;

    public AlertCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            stringBuilder.append(args[i]).append(" ");
        }

        if (args.length == 0 || stringBuilder.isEmpty()) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "please_write_a_message"), null));
            return;
        }

        PlayerService.getSocketServer().sendPacket(new PacketAlert(stringBuilder.toString()));

    }

}
