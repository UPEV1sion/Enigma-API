package org.nativeCInterface.ffm;


import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class ManualKeysFactory {

   // private static final MemoryLayout MEMORY_LAYOUT = MemoryLayout.sequenceLayout()

    public static MemorySegment createManualKeysSegment(final String[] manualKeys, final Arena arena) {
        int length = manualKeys.length;
        MemorySegment[] manualKeysAddr = new MemorySegment[length + 1];//NULL-Termination
        for (int i = 0; i < length; i++) {
            manualKeysAddr[i] = JavaToCFactory.JavaStringToTerminatedUTF8(manualKeys[i], arena);
        }
        manualKeysAddr[length] = MemorySegment.NULL;

        MemorySegment segment = arena.allocate(
                ValueLayout.ADDRESS.byteSize() * manualKeysAddr.length,
                ValueLayout.ADDRESS.byteSize());

        //Write the Addresses
        for (int i = 0; i < length + 1; i++) {
            segment.setAtIndex(ValueLayout.ADDRESS, i, MemorySegment.ofAddress(manualKeysAddr[i].address()));
        }
        return segment;
    }
}
