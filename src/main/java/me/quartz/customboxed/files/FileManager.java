package me.quartz.customboxed.files;

import me.quartz.customboxed.CustomBoxed;

public class FileManager {

    private final CustomFile mapsFile;

    public FileManager() {
        CustomBoxed.getInstance().saveDefaultConfig();
        this.mapsFile = new CustomFile("plots");
    }

    public CustomFile getMapsFile() {
        return mapsFile;
    }

}
