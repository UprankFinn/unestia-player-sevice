package net.unestia.playerservice.command;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.util.UUID;

public abstract class Command {

    public abstract void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity);

    public void sendMessage(UUID uuid, String message) {
        PlayerService.getPlayerEntityManager().getPlayer(uuid).getChannel().writeAndFlush(new PacketPlayerMessage(uuid, message));
    }

}
