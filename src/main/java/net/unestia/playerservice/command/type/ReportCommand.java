package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.player.PlayerEntity;

public class ReportCommand extends Command {

    private final PlayerService playerService;

    public ReportCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

    }
}
