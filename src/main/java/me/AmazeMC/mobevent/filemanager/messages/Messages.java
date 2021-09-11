package me.AmazeMC.mobevent.filemanager.messages;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public enum Messages {
    EVENT_FULL("event-full"),
    EVENT_JOIN("event-join"),
    EVENT_LEAVE("event-leave"),
    EVENT_ALREADY_JOIN("event-already-join"),
    EVENT_ALREADY_LEAVE("event-already-leave"),
    EVENT_JOIN_ALREADY_STARTED("event-join-already-started"),
    EVENT_STARTING("event-starting"),
    EVENT_STARTING_COUNTDOWN("event-starting-countdown"),
    EVENT_STARTED("event-started"),
    SPAWN_SELECTED("spawn-selected"),
    NO_SPAWN_SELECTED("no-spawn-selected"),
    FINISHED_SETUP("finished-setup"),
    STARTED_SETUP("started-setup"),
    NEXT_PHASE_PLAYER("next-phase-player"),
    NEXT_PHASE_MOB("next-phase-mob"),
    NEXT_PHASE_REGION_1("next-phase-region-1"),
    NEXT_PHASE_REGION_2("next-phase-region-2"),
    PLAYER_SETUP_MODE("player-setup-mode"),
    EVENT_FORCESTART_FAILED("event-forcestart-failed"),
    EVENT_FORCESTOP_FAILED("event-forcestop-failed"),
    COMMANDS_NOT_ALLOWED("commands-not-allowed"),
    FORCESTART_NOT_FULL("forcestart-not-full"),
    FORCESTART("forcestart"),
    FORCESTOP("forcestop"),
    CLEARED_WAVE("cleared-wave"),
    CLEARED_ALL_WAVES("cleared-all-wave"),
    TELEPORTED_BACK("teleported-back"),
    WAVE_SPAWNING("wave-spawning"),
    WAVE_SPAWNED("wave-spawned"),
    SOMEONE_LEFT_QUEUE("someone-left-queue"),
    WAVE_DECIDING_TIME("wave-deciding-time"),
    WAVE_CONTINUE("wave-continue"),
    WAVE_QUIT("wave-quit"),
    WAVE_CLEARED_GLOBAL("wave-cleared-global"),
    PLAYER_DIED("player-died"),
    PLAYER_DIED_OTHERS("player-died-others"),
    EVENT_QUIT_SELF("event-quit-self"),
    DROPPED_ITEMS("dropped-items"),

    STATUS_STARTED("status-started"),
    STATUS_NOT_STARTED("status-not-started"),
    STATUS_COOLDOWN("status-cooldown"),

    ANNOUNCE_END_COOLDOWN("announce-end-cooldown"),
    QUEUE_FULL_COOLDOWN("queue-full-cooldown"),
    STARTED_COOLDOWN("started-cooldown"),
    ENDED_COOLDOWN("ended-cooldown"),
    NO_COOLDOWN("no-cooldown"),
    ALREADY_COOLDOWN("already-cooldown"),

    REWARDS_GIVE("rewards-give"),
    REWARDS_GIVE_LIST("rewards-give-list"),
    BROADCAST_JOIN_MESSAGE("broadcast-join-message"),
    REMINDER_ANNOUNCE("reminder-announce"),
    EVENT_RELOADED("event-reloaded"),
    EVENT_RELOADED_QUEUE("event-reloaded-queue"),

    RELOADED_FILES("reloaded-files"),
    NO_PERMISSION("no-permission"),
    NO_PLAYER("no-player");

    private HashMap<String, Object> messages = new HashMap<String, Object>();

    private String id;

    /**
     * Class constructor
     * @param id
     */
    Messages(String id) {
        this.id = id;
    }

    /**
     * Adds config value into hashmap
     */
    public void set(FileConfiguration fileConfig) {
        messages.put(id, fileConfig.getString(id));
    }

    /**
     * Gets config value out of hashmap
     * @return
     */
    public String get() {
        return (String) messages.get(id);
    }
}
