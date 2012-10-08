package ru.spb.itolia.perashki.beans;

public enum piroType {
    NEW("piro/new/"),
    GOOD("piro/good/"),
    BEST("piro/best/"),
    RANDOM("piro/random/"),
    ALL("piro/all/");
    private String path;

    private piroType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
};