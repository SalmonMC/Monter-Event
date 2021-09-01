package me.AmazeMC.mobevent;

import me.AmazeMC.mobevent.commands.AdminEventCommand;
import me.AmazeMC.mobevent.commands.AdminEventCompleter;
import me.AmazeMC.mobevent.commands.EventCommand;
import me.AmazeMC.mobevent.filemanager.data.DataFile;
import me.AmazeMC.mobevent.filemanager.gui.EventGUIFile;
import me.AmazeMC.mobevent.filemanager.messages.MessagesFile;
import me.AmazeMC.mobevent.filemanager.waves.WavesFile;
import me.AmazeMC.mobevent.listeners.*;
import me.AmazeMC.mobevent.managers.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MobEvent extends JavaPlugin {

    public String _PREFIX;

    private static MobEvent plugin;

    private MessagesFile messagesFile;
    private EventGUIFile eventGUIFile;
    private DataFile dataFile;
    private WavesFile wavesFile;

    private QueueManager queueManager;
    private EventManager eventManager;
    private SetupManager setupManager;
    private WaveManager waveManager;
    private RewardManager rewardManager;
    private CooldownManager cooldownManager;
    private ReminderManager reminderManager;

    /**
     * Executed when plugin is enabled
     */
    @Override
    public void onEnable() {
        plugin = this;

        registerFiles();
        registerListeners();
        registerCommands();
        registerManagers();

        saveDefaultConfig();
        _PREFIX = getConfig().getString("event.prefix");

        MobEvent.getInstance().getReminderManager().startReminder();
    }

    /**
     * Executed when plugin is disabled
     */
    @Override
    public void onDisable() {
        if (!this.getEventManager().getList().isEmpty()) {
            for (Player p : this.getEventManager().getList().keySet()) {
                p.teleport(this.getEventManager().getList().get(p));
            }
        }

        if (!this.getWaveManager().getEntities().isEmpty()) {
            for (Entity ent : this.getWaveManager().getEntities()) {
                ent.remove();
            }
        }
    }

    /**
     * Registers the yaml files
     */
    private void registerFiles() {
        messagesFile = new MessagesFile();
        eventGUIFile = new EventGUIFile();
        dataFile = new DataFile();
        wavesFile = new WavesFile();
    }

    /**
     * Registers the listeners
     */
    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new GUIListener(), this);
        pm.registerEvents(new QueueListener(), this);
        pm.registerEvents(new EventListener(), this);
        pm.registerEvents(new SetupListener(), this);
        pm.registerEvents(new WaveListener(), this);
    }

    /**
     * Registers the commands
     */
    private void registerCommands() {
        getCommand("event").setExecutor(new EventCommand());

        getCommand("adminevent").setExecutor(new AdminEventCommand());
        getCommand("adminevent").setTabCompleter(new AdminEventCompleter());
    }

    /**
     * Registers the managers
     */
    private void registerManagers() {
        queueManager = new QueueManager();
        eventManager = new EventManager();
        setupManager = new SetupManager();
        waveManager = new WaveManager();
        rewardManager = new RewardManager();
        cooldownManager = new CooldownManager();
        reminderManager = new ReminderManager();
    }

    /**
     * Constructor for MessageFile
     * @return
     */
    public MessagesFile getMessagesFile() {
        return messagesFile;
    }

    /**
     * Constructor for GUIFile
     * @return
     */
    public EventGUIFile getEventGUIFile() {
        return eventGUIFile;
    }

    /**
     * Constructor for DataFile
     * @return
     */
    public DataFile getDataFile() {
        return dataFile;
    }

    /**
     * Constructor for WavesFile
     * @return
     */
    public WavesFile getWavesFile() {
        return wavesFile;
    }

    /**
     * Constructor for QueueManager
     * @return
     */
    public QueueManager getQueueManager() {
        return queueManager;
    }

    /**
     * Constructor for EventManager
     * @return
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Constructor for SetupManager
     */
    public SetupManager getSetupManager() {
        return setupManager;
    }

    /**
     * Constructor for WaveManager
     */
    public WaveManager getWaveManager() {
        return waveManager;
    }

    /**
     * Constructor for RewardManager
     */
    public RewardManager getRewardManager() {
        return rewardManager;
    }

    /**
     * Constructor for CooldownManager
     */
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }


    /**
     * Constructor for ReminderManager
     */
    public ReminderManager getReminderManager() {
        return reminderManager;
    }

    /**
     * Instance constructor
     * @return MobEvent
     */
    public static MobEvent getInstance() {
        return plugin;
    }
}
