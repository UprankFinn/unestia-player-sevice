package net.unestia.playerservice.command.type;

import com.mongodb.client.model.Filters;
import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerDisconnect;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.ban.Ban;
import net.unestia.playerservice.player.ban.reason.ReasonType;
import net.unestia.playerservice.player.rank.RankEntity;
import net.unestia.playerservice.util.NameFetcher;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class BanCommand extends Command {

    private final PlayerService playerService;

    public BanCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {

        if (args.length == 2) {
            String name = args[0];
            String reason = args[1];

            if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis player does not exist!");
                return;
            }
            if (!(ReasonType.exists(reason.toUpperCase()))) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + "§cThis ban reason does not exist!");
                return;
            }

            PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
            RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

            if (PlayerService.getBanManager().getBan(player.getUUID()) != null) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + "§cThis player has already been banned!");
                return;
            }

            if (playerEntity.getName().equals(name)) {
                this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + "§cYou can't ban yourself!");
                return;
            }

            Ban ban = new Ban(player.getUUID(), ReasonType.valueOf(reason).getDuration(), reason, System.currentTimeMillis(), "null", playerEntity.getUUID());

            if (PlayerService.getBanManager().getCollection().find(Filters.eq("uuid", playerEntity.getUUID().toString())).first() == null) {

                PlayerService.getBanManager().getCollection().insertOne(PlayerService.getBanManager().getGson().fromJson(PlayerService.getBanManager().getGson().toJson(ban), Document.class));
                PlayerService.getBanManager().addBanToCach(player.getUUID(), ban);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            if (PlayerService.getOnlinePlayers().containsValue(name))
                player.getChannel().writeAndFlush(new PacketPlayerDisconnect(player.getUUID(), "§6§lᴜɴᴇsᴛɪᴀ ɴᴇᴛᴡᴏʀᴋ \n\n§cDu wurdest vom Netzwerk gebannt!\n\n§7Grund: §c" + ReasonType.valueOf(args[1].toUpperCase().replace("_", " ")) + "\n§7Gebannt bis zum: §c" + simpleDateFormat.format(ban.getTime() + ReasonType.valueOf(reason).getDuration())));
            this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + "§7You have " + rank.getColor() + player.getName() + " §7successfully for " + reason.replace("_", " ") + " banned!");

        } else if (args.length == 1) {
            if (args[0].equals("reasons")) {

                this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + "§7There are all punishment reasons:");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                for (ReasonType value : ReasonType.values()) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.BANPREFIX + "§c" + value.getKey() + " §7- " + "§e" + simpleDateFormat.format(value.getDuration()));
                }

            } else {
                this.sendMessage(playerEntity.getUUID(), PlayerService.getUsageMessage(PlayerService.BANPREFIX, "ban <name> <reason>", "ban a player"));
                this.sendMessage(playerEntity.getUUID(), PlayerService.getUsageMessage(PlayerService.BANPREFIX, "ban reasons", "show all ban reasons"));
            }
        } else {
            this.sendMessage(playerEntity.getUUID(), PlayerService.getUsageMessage(PlayerService.BANPREFIX, "ban <name> <reason>", "ban a player"));
            this.sendMessage(playerEntity.getUUID(), PlayerService.getUsageMessage(PlayerService.BANPREFIX, "ban reasons", "show all ban reasons"));
        }

    }

}
