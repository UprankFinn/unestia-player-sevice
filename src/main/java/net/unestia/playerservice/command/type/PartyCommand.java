package net.unestia.playerservice.command.type;

import io.netty.channel.Channel;
import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.Terminal;
import net.unestia.playerservice.command.Command;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.party.Party;
import net.unestia.playerservice.player.PlayerEntity;
import net.unestia.playerservice.player.rank.RankEntity;
import net.unestia.playerservice.player.rank.RankEntityManager;

import java.util.Arrays;
import java.util.UUID;

public class PartyCommand extends Command {

    private final PlayerService playerService;

    public PartyCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    // - /party invite <name>
    // - /party kick <name>
    // - /party leave
    // - /party promote <name>
    // - /party demote <name>
    // - /party jump

    @Override
    public void receiveCommand(Channel channel, String[] args, PlayerEntity playerEntity) {
        if (args.length == 0) {
        } else if (args.length == 2) {

            if (args[0].equals("invite")) {
                String name = args[1];

                if (PlayerService.getPlayerEntityManager().getPlayer(name) == null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PREFIX + "§cThis player does not exist!");
                    return;
                }

                if (!(PlayerService.getOnlinePlayers().containsValue(name))) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PARTYPREFIX + "§cThis player is not online!");
                    return;
                }

                if (name.equals(playerEntity.getName())) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PARTYPREFIX + "§cYou can't invite yourself to the party!");
                    return;
                }

                PlayerEntity player = PlayerService.getPlayerEntityManager().getPlayer(name);
                RankEntity rank = PlayerService.getRankEntityManager().getRank(player.getRankId());

                if (player.getParty() != null) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PARTYPREFIX + "§cThis player is already been in a party!");
                    return;
                }

                Party party = new Party(playerEntity.getUUID(), false, "null");
                playerEntity.setParty(party);

                PlayerService.getPartyManager().getParties().put(playerEntity.getUUID(), party);

                for (Party partys : PlayerService.getPartyManager().getParties().values()) {
                    if (partys.getModerators().contains(player.getUUID()) || partys.getMembers().contains(player.getUUID())) {
                        this.sendMessage(playerEntity.getUUID(), PlayerService.PARTYPREFIX + "§cThis player is already in another party!");
                        return;
                    }
                }

                if (party.getRequests().contains(player.getUUID())) {
                    this.sendMessage(playerEntity.getUUID(), PlayerService.PARTYPREFIX + "§cThe player has already been invited to the party!");
                    return;
                }

                party.getRequests().add(player.getUUID());

                this.sendMessage(playerEntity.getUUID(), PlayerService.PARTYPREFIX + "§7You have " + rank.getColor() + player.getName() + " §7invited to the party!");
                this.sendMessage(player.getUUID(), PlayerService.PARTYPREFIX + "§7You have been invited by " + PlayerService.getRankEntityManager().getRank(playerEntity.getRankId()).getColor() + playerEntity.getName() + " §7to the party!");

            }

        }
    }

}
