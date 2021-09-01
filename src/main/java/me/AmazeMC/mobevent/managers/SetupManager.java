package me.AmazeMC.mobevent.managers;

import me.AmazeMC.mobevent.MobEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SetupManager {

    /**
     * Phases
     *
     * 0 = Player spawn points
     * 1 = Mob spawn points
     */

    private HashMap<Player, Integer> editor = new HashMap<Player, Integer>();

    /**
     * Starts the editor for the player
     * @param p
     */
    public void startSetup(Player p) {
        editor.put(p, 0);

        // Clears the whole file
        MobEvent.getInstance().getDataFile().getFile().set("mobevent", null);
        MobEvent.getInstance().getDataFile().saveConfig();
    }

    /**
     * Gets the phase of the player
     * @param p
     * @return
     */
    public int getPhase(Player p) {
        return editor.get(p);
    }

    /**
     * Sets the player into the next phase
     * @param p
     */
    public void nextPhase(Player p) {
        int currentPhase = editor.get(p);
        editor.put(p, (currentPhase + 1));
    }

    /**
     * Checks if the exists in the editor
     * @param p
     * @return boolean
     */
    public boolean exists(Player p) {
        return editor.containsKey(p);
    }

    /**
     * Removes player out of the editor
     * @param p
     */
    public void remove(Player p) {
        if (!editor.containsKey(p)) return;
        editor.remove(p);
    }
}
