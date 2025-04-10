package org.nativeCInterface;

import java.nio.file.Files;
import java.nio.file.Path;

public class NativeInterfaceConfig {
    public static final Path LIBRARY_PATH = Path.of(System.getProperty("user.dir"), "src", "main", "enigma_c", "libenigma.so");
    public static final String ENIGMA_METHOD_NAME = "enigma_encrypt";
    public static final String CYCLOMETER_METHOD_NAME = "cyclometer_create_cycles";

    static {
        if (!Files.exists(LIBRARY_PATH)) {
            throw new ExceptionInInitializerError("Error: Native library not found at " + LIBRARY_PATH);
        }
    }
}
