package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;

public class NotifyCommand extends Command {

    private final PlayerService playerService;

    public NotifyCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (!(playerEntity.isInTeam())) {
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cYou can not §aenable§c/§cdisable §cthe team notifications!");
            return;
        }

        if (playerEntity.getNotification()) {
            playerEntity.setNotification(false);
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "notify_command.disabled"), null));
        } else {
            playerEntity.setNotification(true);
            this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "notify_command.enabled"), null));
        }


    }
}
