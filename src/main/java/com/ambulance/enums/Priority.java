package com.ambulance.enums;

public enum Priority {
    CRITICAL(1), HIGH(2), MODERATE(3), NORMAL(4);
    
    private final int level;
    Priority(int level) { this.level = level; }
    public int getLevel() { return level; }
}
