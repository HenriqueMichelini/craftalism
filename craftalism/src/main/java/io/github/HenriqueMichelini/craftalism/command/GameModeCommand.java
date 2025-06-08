package io.github.HenriqueMichelini.craftalism.command;

import io.github.HenriqueMichelini.craftalism.gui.manager.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameModeCommand implements CommandExecutor {
    private final GuiManager guiManager;

    public GameModeCommand(GuiManager guiManager) { this.guiManager = guiManager; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be executed by a player.");
            return true;
        }

        guiManager.openGameModes(player);
        return true;
    }
}
