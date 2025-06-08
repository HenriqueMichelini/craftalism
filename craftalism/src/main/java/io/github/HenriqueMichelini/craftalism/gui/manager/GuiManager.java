package io.github.HenriqueMichelini.craftalism.gui.manager;

import io.github.HenriqueMichelini.craftalism.Craftalism;
import io.github.HenriqueMichelini.craftalism.gui.components.GameModeConfigGUI;
import io.github.HenriqueMichelini.craftalism.gui.components.GameModeLobbyGUI;
import io.github.HenriqueMichelini.craftalism.gui.components.GameModesGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {
    private GameModesGUI gameModesGUI;
    private Craftalism plugin;

    private final Map<String, GameModeConfigGUI> gameModeConfigGui = new HashMap<>();
    private final Map<String, GameModeLobbyGUI> gameModeLobbyGui = new HashMap<>();


    public GuiManager(Craftalism plugin) {
        this.plugin = plugin;
        initializeGUIs();
    }

    private void initializeGUIs() {
        this.gameModesGUI = new GameModesGUI(
                plugin,
                this::handleGameModeSelection
        );
    }

    private void handleGameModeSelection(Player player, String gameMode) {
        GameModeConfigGUI gameModeConfigGUI = gameModeConfigGui.get(gameMode);
//        if (gameModeConfigGUI != null) {
//            gameModeConfigGUI.open(player);
//        } else {
//            plugin.getLogger().warning("Attempted to open invalid category: " + gameMode);
//            player.sendMessage(Component.text("Invalid category!", NamedTextColor.RED));
//        }
    }

    public void openGameModes(Player player) {
        gameModesGUI.open(player);
    }
}
