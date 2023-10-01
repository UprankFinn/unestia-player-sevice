package net.unestia.playerservice.command.type;

import com.mongodb.client.model.Filters;
import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.rank.RankEntity;

import java.text.MessageFormat;
import java.util.UUID;

public class UnbanCommand extends Command {

    private final PlayerService playerService;

    public UnbanCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (args.length == 1) {
            String name = args[0];

            if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "player_does_not_exist"), null));
                return;
            }

            PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
            RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

            if (name.equals(playerEntity.getName())) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "unban_command.can_not_unban_yourself"), null));
                return;
            }

            if (PlayerService.getBanManager().getBan(player.getUUID()) == null) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "unban_command.is_currently_not_banned"), rank.getColor(), player.getName()));
                return;
            }

            PlayerService.getBanManager().removeBanFromCach(player.getUUID());
            if (PlayerService.getBanManager().getCollection().find(Filters.eq("uuid", player.getUUID().toString())).first() != null) {
                PlayerService.getBanManager().getCollection().deleteOne(Filters.eq("uuid", player.getUUID().toString()));
            }

            this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + MessageFormat.format(PlayerService.getTranslationManager().getTranslationKey(playerEntity.getLanguage(), "unban_command"), rank.getColor(), player.getName()));

        }

    }

}
