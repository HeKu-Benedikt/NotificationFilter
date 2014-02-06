package com.btw.filter.db;

/**
 * Created by bbille on 05.02.14.
 */
public class WhitelistEntry {

    private int primaryKey;
    private String notificationPackage;
    private String whitelistEntry;

    public WhitelistEntry() {
    }

    public WhitelistEntry(int primaryKey, String notificationPackage, String whitelistEntry) {
        this.primaryKey = primaryKey;
        this.notificationPackage = notificationPackage;
        this.whitelistEntry = whitelistEntry;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getNotificationPackage() {
        return notificationPackage;
    }

    public void setNotificationPackage(String notificationPackage) {
        this.notificationPackage = notificationPackage;
    }

    public String getWhitelistEntry() {
        return whitelistEntry;
    }

    public void setWhitelistEntry(String whitelistEntry) {
        this.whitelistEntry = whitelistEntry;
    }
}
