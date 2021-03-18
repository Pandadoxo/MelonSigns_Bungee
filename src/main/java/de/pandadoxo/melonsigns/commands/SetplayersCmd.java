// -----------------------
// Coded by Pandadoxo
// on 18.03.2021 at 09:08 
// -----------------------

package de.pandadoxo.melonsigns.commands;

import de.pandadoxo.melonsigns.Main;
import de.pandadoxo.melonsigns.core.PingTask;
import de.pandadoxo.melonsigns.core.Server;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SetplayersCmd extends Command {

    BaseComponent[] SYNTAX = TextComponent.fromLegacyText(Main.PREFIX + "§7Falscher Syntax! Benutze: §e/setplayers <Server> <Anzahl>");

    public SetplayersCmd(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!Main.getDoxperm().has(p, "melonensigns.setplayers", true)) {
            return;
        }

        if (args.length != 2) {
            p.sendMessage(SYNTAX);
            return;
        }

        Server server = Main.getServerConfig().getServer(args[0]);
        if (server == null) {
            p.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§7Dieser Server ist nicht registriert"));
            return;
        }

        try {
            int number = Integer.parseInt(args[1]);
            server.setMaxplayers(number);
            p.sendMessage(TextComponent.fromLegacyText(Main.PREFIX + "§7Die Maximale Spieleranzahl von §e" + server.getName() + " §7wurde auf §b" + number + " " +
                    "§7gesetzt"));
            new PingTask().run();
            Main.getFilesUtil().save();
            return;
        } catch (NumberFormatException ex) {
            p.sendMessage(SYNTAX);
        }
    }
}
