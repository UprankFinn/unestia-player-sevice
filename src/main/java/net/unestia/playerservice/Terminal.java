package net.unestia.playerservice;

import io.netty.util.ResourceLeakDetector;
import net.unestia.playerservice.terminal.TerminalCommandManager;
import net.unestia.playerservice.util.AnsiUtil;
import org.fusesource.jansi.AnsiConsole;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Terminal {

    private static SimpleFormatter simpleFormatter;
    private static SimpleDateFormat simpleDateFormat;
    private static Logger logger;
    private static LineReader lineReader;

    private static boolean running;
    private static boolean waiting;

    private static TerminalCommandManager terminalCommandManager;

    public Terminal() throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        AnsiConsole.systemInstall();
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        simpleFormatter = new SimpleFormatter();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        terminalCommandManager = new TerminalCommandManager(this);

        TerminalBuilder terminalBuilder = TerminalBuilder.builder();
        terminalBuilder.encoding(Charset.defaultCharset());
        terminalBuilder.system(true);

        LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder();
        lineReaderBuilder.terminal(terminalBuilder.build());
        lineReaderBuilder.completer(new Completers.TreeCompleter(new Completers.TreeCompleter.Node[0]));
        lineReaderBuilder.parser(new DefaultParser());
        lineReader = lineReaderBuilder.build();

        logger = Logger.getLogger("playerservice");
        logger.setUseParentHandlers(false);;

    }

    public static void setActive() {
        running = true;
        waiting = false;

        new Thread(() -> {
            while (running) {
                waiting = true;
                String line = null;

                try {
                    line = lineReader.readLine(AnsiUtil.format("&bPlayerService &8> &r"), null, (MaskingCallback) null, null);
                } catch (UserInterruptException var4) {
                    System.exit(0);
                }

                waiting = false;
                if (line != null && !line.isEmpty()) {
                    terminalCommandManager.executeCommand(line.trim());
                }
            }
        }).start();

    }

    public SimpleFormatter getSimpleFormatter() {
        return simpleFormatter;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public LineReader getLineReader() {
        return lineReader;
    }

    public static void setRunning(boolean running) {
        Terminal.running = running;
    }

    public static void setWaiting(boolean waiting) {
        Terminal.waiting = waiting;
    }

    public static boolean isRunning() {
        return running;
    }

    public static boolean isWaiting() {
        return waiting;
    }

    public static TerminalCommandManager getTerminalCommandManager() {
        return terminalCommandManager;
    }

    public static void info(String message) {
        message = AnsiUtil.format(message);
        if (lineReader == null) {
            System.out.println(message);
        } else {
            if (waiting) {
                lineReader.callWidget("clear");
            }

            lineReader.getTerminal().writer().print(simpleFormatter.format(new LogRecord(java.util.logging.Level.INFO, message)));
            if (waiting) {
                lineReader.callWidget("redraw-line");
            }

            if (waiting) {
                lineReader.callWidget("redisplay");
            }

            lineReader.getTerminal().writer().flush();
            if (logger != null) {
                logger.info(message);
            }

        }
    }

    public static void warning(String message) {
        message = AnsiUtil.format(message);
        if (lineReader == null) {
            System.out.println(message);
        } else {
            if (waiting) {
                lineReader.callWidget("clear");
            }

            lineReader.getTerminal().writer().print(simpleFormatter.format(new LogRecord(java.util.logging.Level.WARNING, message)));
            if (waiting) {
                lineReader.callWidget("redraw-line");
            }

            if (waiting) {
                lineReader.callWidget("redisplay");
            }

            lineReader.getTerminal().writer().flush();
            if (logger != null) {
                logger.warning(message);
            }

        }
    }

    public static void severe(String message) {
        message = AnsiUtil.format(message);
        if (lineReader == null) {
            System.out.println(message);
        } else {
            if (waiting) {
                lineReader.callWidget("clear");
            }

            lineReader.getTerminal().writer().print(simpleFormatter.format(new LogRecord(java.util.logging.Level.SEVERE, message)));
            if (waiting) {
                lineReader.callWidget("redraw-line");
            }

            if (waiting) {
                lineReader.callWidget("redisplay");
            }

            lineReader.getTerminal().writer().flush();
            if (logger != null) {
                logger.severe(message);
            }

        }
    }

}
