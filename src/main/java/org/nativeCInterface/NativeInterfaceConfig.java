package org.nativeCInterface;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NativeInterfaceConfig {
    //public static final Path LIBRARY_PATH = Path.of(System.getProperty("user.dir"), "src", "main", "enigma_c", "libenigma.so");
    public static final Path LIBRARY_PATH;

    static {
        String libraryPathFromArg = System.getProperty("enigma.native.path");
        if (libraryPathFromArg != null && !libraryPathFromArg.isEmpty()) {
            // If an argument (system property) is provided, use that path
            LIBRARY_PATH = Paths.get(libraryPathFromArg);
        } else {
            // Otherwise, fallback to the default path relative to the project folder
            LIBRARY_PATH = Path.of(System.getProperty("user.dir"), "src", "main", "enigma_c", "libenigma.so");
        }

        // Optional: Add a log to check the final resolved path
        System.out.println("Using native library path: " + LIBRARY_PATH.toString());
    }

    public static final String ENIGMA_METHOD_NAME = "enigma_encrypt";
    public static final String CYCLOMETER_METHOD_NAME = "cyclometer_create_cycles";
    public static final String MANUAL_CYCLOMETER_METHOD_NAME = "manual_cyclometer_create_cycles";
    public static final int ALPHABET_SIZE = 26;

    static {
        if (!Files.exists(LIBRARY_PATH)) {
            throw new ExceptionInInitializerError("Error: Native library not found at " + LIBRARY_PATH);
        }
    }
}
