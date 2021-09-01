package me.AmazeMC.mobevent.filemanager;

import me.AmazeMC.mobevent.MobEvent;

public class FileManager {

    /**
     * Saves all the config files
     */
    public static void reloadAll() {
        MobEvent.getInstance().reloadConfig();
        MobEvent.getInstance().getDataFile().reloadFile();
        MobEvent.getInstance().getWavesFile().reloadFile();
        MobEvent.getInstance().getMessagesFile().reloadFile();
        MobEvent.getInstance().getEventGUIFile().reloadFile();

        MobEvent.getInstance().saveDefaultConfig();
        MobEvent.getInstance().getDataFile().saveConfig();
        MobEvent.getInstance().getWavesFile().saveConfig();
        MobEvent.getInstance().getMessagesFile().saveConfig();
        MobEvent.getInstance().getEventGUIFile().saveConfig();
    }
}
