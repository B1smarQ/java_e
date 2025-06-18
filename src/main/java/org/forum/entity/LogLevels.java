package org.forum.entity;

public enum LogLevels {
    DEBUG("DEBUG"),
    ERROR("ERROR"),
    INFO("INFO"),
    WARNING("WARNING");

    private final String logLevel;

    LogLevels(String level){
        this.logLevel = level;
    }

    public String getLogLevel(){
        return logLevel;
    }

}
