package org.db;

public class DatabaseConfig {
    public static final Integer PAGE_SIZE = 100;
    public static final long CATALOGUE_SIZE = 1_054_560L;
    public static final long NUM_PAGES = (CATALOGUE_SIZE + PAGE_SIZE - 1) / PAGE_SIZE;
}
