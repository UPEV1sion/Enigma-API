package org.nativeCInterface.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

public class JavaToCFactory {
    public static MemorySegment JavaStringToTerminatedUTF8(String string, Arena arena) {
        byte[] utf8 = string.getBytes(StandardCharsets.UTF_8);

        MemorySegment segment = arena.allocate(JAVA_BYTE, utf8.length + 1);
        segment.asByteBuffer().put(utf8).put((byte) 0);
        return segment;
    }
}
