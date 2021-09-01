package me.AmazeMC.mobevent.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class QueueManager {

    private ArrayList<Player> queue = new ArrayList<Player>();

    /**
     * Get the size of the queue
     * @return
     */
    public int getCount() {
        return queue.size();
    }

    /**
     * Checks if the exists in the queue
     * @param p
     * @return boolean
     */
    public boolean exists(Player p) {
        return queue.contains(p);
    }

    /**
     * Get a player from the queue
     * @param i
     * @return
     */
    public Player get(int i) {
        return queue.get(i);
    }

    /**
     * Gets the queue list
     * @return
     */
    public ArrayList<Player> getList() {
        return queue;
    }

    /**
     * Clears the queue
     */
    public void clear() {
        queue.clear();
    }

    /**
     * Adds player into the queue
     * @param p
     */
    public void join(Player p) {
        if (queue.contains(p)) return;
        queue.add(p);
    }

    /**
     * Removes player out of the queue
     * @param p
     */
    public void leave(Player p) {
        if (!queue.contains(p)) return;
        queue.remove(p);
    }
}
