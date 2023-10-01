package net.unestia.playerservice.terminal;

import net.unestia.playerservice.Terminal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TerminalCommandManager {

    private final Terminal terminal;

    private final Map<String, TerminalCommand> commands;

    public TerminalCommandManager(Terminal terminal) {
        this.terminal = terminal;
        this.commands = new HashMap<>();
    }

    public void executeCommand(String input) {
        String command = input.split(" ")[0];
        String[] args = input.split(" ").length == 1 ? new String[0] : Arrays.copyOfRange(input.split(" "), 1, input.split(" ").length);
        TerminalCommand commandClass = this.commands.get(command);
        if (commandClass == null) commandClass = this.commands.get("help");
        commandClass.execute(command, args);
    }

    public void registerCommand(String name, TerminalCommand command) {
        this.commands.put(name.toLowerCase(), command);
    }

    public void unregisterCommand(String name) {
        this.commands.remove(name.toLowerCase());
    }

    public Map<String, TerminalCommand> getCommands() {
        return this.commands;
    }

}
