package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketMaintenanceUpdate;
import net.unestia.playerservice.network.PacketPlayerDisconnect;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;
import java.util.UUID;

public class MaintenanceCommand extends Command {

    private final PlayerService playerService;

    public MaintenanceCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (PlayerService.isMaintenance()) {

            PlayerService.getChannels().forEach((uuid, channels) -> {
                channels.writeAndFlush(new PacketMaintenanceUpdate(false));
            });


            PlayerService.setMaintenance(false);

            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "maintenance_command.disabled"), null));

        } else {
            PlayerService.getChannels().forEach((uuid, channels) -> {
                channels.writeAndFlush(new PacketMaintenanceUpdate(true));

                PlayerService.getOnlinePlayers().forEach((uuid1, s) -> {

                    if (!(PlayerService.getPlayerEntityManager().getPlayer(uuid1).isInTeam())) {
                        channels.writeAndFlush(new PacketPlayerDisconnect(uuid1, " §6§lᴜɴᴇsᴛɪᴀ ɴᴇᴛᴡᴏʀᴋ \n\n§cWe are currently in Maintenance Mode"));
                    }
                });
            });
            PlayerService.setMaintenance(true);
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "maintenance_command.enabled"), null));

        }

    }

}
