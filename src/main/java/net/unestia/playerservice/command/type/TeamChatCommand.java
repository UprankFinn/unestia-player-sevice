package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;

import java.text.MessageFormat;
import java.util.UUID;

public class TeamChatCommand extends Command {

    private final PlayerService playerService;

    public TeamChatCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {
        if (args.length < 1) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; ++i) {
            stringBuilder.append(args[i]).append(" ");
        }

        if (args.length == 0 || stringBuilder.isEmpty()) {
            this.sendMessage(playerEntity.getUUID(), "§8[§6TeamChat§8]§r " + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "please_write_a_message"), null));
            return;
        }

        PlayerService.getOnlinePlayers().forEach((uuid, name) -> {
            PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(uuid);

            if (player.getNotification() && player.isInTeam()) {
                this.sendMessage(player.getUUID(), "§8[§6TeamChat§8]§r " + PlayerService.getRankEntityManager().getRank(playerEntity.getRankId()).getColor() + playerEntity.getName() + " §8» §f" + stringBuilder.toString().replace("&", "§"));
            }
        });

    }

}
