package io.github.HenriqueMichelini.craftalism.gui.components;

import io.github.HenriqueMichelini.craftalism.Craftalism;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.function.BiConsumer;

public class GameModesGUI extends BaseGUI {
    private final BiConsumer<Player, String> onGameModeSelect;

    private enum GameMode {
        SANDBOX("Sandbox mode", Material.GRASS_BLOCK, 1),
        SANDBOX_TEAMS("Sandbox teams mode", Material.OAK_SAPLING, 2),
        TAX_HELL("Tax Hell mode", Material.REDSTONE_BLOCK, 3),
        TAX_HELL_TEAMS("Tax Hell Teams mode", Material.NETHER_BRICK, 4),
        EXPAND_CONQUER("Expand and Conquer mode", Material.DIAMOND_BLOCK, 5),
        EXPAND_CONQUER_TAX("Expand and Conquer Tax Edition", Material.EMERALD_BLOCK, 6);

        final String displayName;
        final Material material;
        final int slot;

        GameMode(String displayName, Material material, int slot) {
            this.displayName = displayName;
            this.material = material;
            this.slot = slot;
        }
    }

    public GameModesGUI(
            Craftalism plugin,
            BiConsumer<Player, String> onGameModeSelect
    ) {
        super("Game Modes", 6, plugin);
        this.onGameModeSelect = onGameModeSelect;
        addAllGameModeButtons();
    }

    private void addAllGameModeButtons() {
        for (GameMode mode : GameMode.values()) {
            addGameModeButton(mode);
        }
    }

    private void addGameModeButton(GameMode mode) {
        GuiItem button = ButtonFactory.createCachedButton(
                mode.name().toLowerCase(),
                mode.material,
                Component.text(mode.displayName),
                Collections.singletonList(Component.text("Click to select")),
                event -> onGameModeSelect.accept(event.getPlayer(), mode.name())
        );
        gui.setItem(mode.slot, button);
    }
}

//  1: Sandbox:         Atingir X valor, todos juntos.
//  2: Sandbox Teams:   Atingir X valor, criação de times.
//  3: Tax Hell:        Sobreviver aos impostos, todos juntos.
//  4: Tax Hell Teams:  Sobreviver aos impostos, criação de times.
//  5: Expand and Conquer: Atingir X valor, expandir, todos juntos.
//  6: Expand and Conquer Tax Edition: Sobreviver aos impostos, expandir, todos juntos.