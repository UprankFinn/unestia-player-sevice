package net.unestia.playerservice.command;

import net.unestia.playerservice.PlayerService;
import net.unestia.playerservice.command.type.*;
import net.unestia.playerservice.network.PacketCommand;
import net.unestia.playerservice.network.PacketPlayerMessage;
import net.unestia.playerservice.network.command.CommandType;
import net.unestia.playerservice.player.PlayerEntity;

public class CommandManager {

    private final PlayerService playerService;

    private final AlertCommand alertCommand;
    private final APIKeyCommand apiKeyCommand;
    private final BanCommand banCommand;
    private final CoinsCommand coinsCommand;
    private final FriendCommand friendCommand;
    private final HubCommand hubCommand;
    private final KickCommand kickCommand;
    private final ListCommand listCommand;
    private final MaintenanceCommand maintenanceCommand;
    private final NotifyCommand notifyCommand;
    private final PartyCommand partyCommand;
    private final RankCommand rankCommand;
    private final SendCommand sendCommand;
    private final StarsCommand starsCommand;
    private final TeamChatCommand teamChatCommand;
    private final UnbanCommand unbanCommand;
    private final WhereamiCommand whereamiCommand;

    public CommandManager(PlayerService playerService) {
        this.playerService = playerService;

        this.alertCommand = new AlertCommand(PlayerService.getInstance());
        this.apiKeyCommand = new APIKeyCommand(PlayerService.getInstance());
        this.banCommand = new BanCommand(PlayerService.getInstance());
        this.coinsCommand = new CoinsCommand(PlayerService.getInstance());
        this.friendCommand = new FriendCommand(PlayerService.getInstance());
        this.hubCommand = new HubCommand(PlayerService.getInstance());
        this.kickCommand = new KickCommand(PlayerService.getInstance());
        this.listCommand = new ListCommand(PlayerService.getInstance());
        this.maintenanceCommand = new MaintenanceCommand(PlayerService.getInstance());
        this.notifyCommand = new NotifyCommand(PlayerService.getInstance());
        this.partyCommand = new PartyCommand(PlayerService.getInstance());
        this.rankCommand = new RankCommand(PlayerService.getInstance());
        this.sendCommand = new SendCommand(PlayerService.getInstance());
        this.starsCommand = new StarsCommand(PlayerService.getInstance());
        this.teamChatCommand = new TeamChatCommand(PlayerService.getInstance());
        this.unbanCommand = new UnbanCommand(PlayerService.getInstance());
        this.whereamiCommand = new WhereamiCommand(PlayerService.getInstance());

        PlayerService.getSocketServer().registerListener(PacketCommand.class, (channelHandlerContext, packetCommand) -> {

            PlayerEntity playerEntity = PlayerService.getPlayerEntityManager().getPlayer(packetCommand.getUUID());
            if (playerEntity == null) {
                channelHandlerContext.channel().writeAndFlush(new PacketPlayerMessage(packetCommand.getUUID(), PlayerService.PREFIX + "§cYou are not registered. Please contact an staffmember!"));
            }

            CommandType commandType = packetCommand.getCommandType();

            if (commandType.equals(CommandType.ALERT_COMMAND)) {
                alertCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.API_COMMAND)) {
                apiKeyCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.BAN_COMMAND)) {
                banCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.COINS_COMMAND)) {
                coinsCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.FRIEND_COMMAND)) {
                friendCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.HUB_COMMAND)) {
                hubCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.KICK_COMMAND)) {
                kickCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.LIST_COMMAND)) {
                listCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.MAINTENANCE_COMMAND)) {
                maintenanceCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.NOTIFY_COMMAND)) {
                notifyCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.PARTY_COMMAND)) {
                partyCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.RANK_COMMAND)) {
                rankCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.SEND_COMMAND)) {
                sendCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.STARS_COMMAND)) {
                starsCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.TEAMCHAT_COMMAND)) {
                teamChatCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.UNBAN_COMMAND)) {
                unbanCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            } else if (commandType.equals(CommandType.WHEREAMI_COMMAND)) {
                whereamiCommand.receiveCommand(channelHandlerContext.channel(), packetCommand.getArgs(), playerEntity);
            }

        });
    }

}
