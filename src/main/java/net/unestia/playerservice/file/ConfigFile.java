package net.unestia.playerservice.file;

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigFile {

    private Integer nettyport;
    private String mongodb;

    private Integer redisPort;
    private String redisUser;
    private String redisPassword;

    private List<String> blockedWords;
    private List<String> blockedDomains;

    public ConfigFile() throws IOException {
        this.blockedWords = new ArrayList<>();
        this.blockedDomains = new ArrayList<>();
    }

    public Integer getNettyport() {
        return nettyport;
    }

    public String getMongodb() {
        return mongodb;
    }

    public List<String> getBlockedWords() {
        return blockedWords;
    }

    public List<String> getBlockedDomains() {
        return blockedDomains;
    }
}
