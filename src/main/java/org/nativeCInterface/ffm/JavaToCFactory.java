package org.nativeCInterface.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

public class JavaToCFactory {
    public static MemorySegment JavaStringToTerminatedUTF8(String string, Arena arena) {
        byte[] utf8 = string.getBytes(StandardCharsets.UTF_8);

        MemorySegment segment = arena.allocate(JAVA_BYTE, utf8.length + 1);
        segment.asByteBuffer().put(utf8).put((byte) 0);
        return segment;
    }

    public static MemorySegment JavaStringArrayToTerminatedUTF8(final String[] stringArr, final Arena arena) {
        int length = stringArr.length;
        MemorySegment[] segArr = new MemorySegment[length + 1];//NULL-Termination
        for (int i = 0; i < length; i++) {
            segArr[i] = JavaToCFactory.JavaStringToTerminatedUTF8(stringArr[i], arena);
        }
        segArr[length] = MemorySegment.NULL;

        MemorySegment segment = arena.allocate(
                ValueLayout.ADDRESS.byteSize() * segArr.length,
                ValueLayout.ADDRESS.byteSize());

        //Write the Addresses
        for (int i = 0; i < length + 1; i++) {
            segment.setAtIndex(ValueLayout.ADDRESS, i, MemorySegment.ofAddress(segArr[i].address()));
        }
        return segment;
    }


    public static MemorySegment JavaStringArrToUint8_t(String[] stringArr, Arena arena) {
        byte[] uint8_arr = new byte[stringArr.length];
        for (int i = 0; i < stringArr.length; i++) {
            int val = Integer.parseInt(stringArr[i]);
            if (val < 0 || val > 255) {
                throw new IllegalArgumentException("Value out of range for uint8_t: " + val);
            }
            uint8_arr[i] = (byte) (val & 0xFF); // Store as signed byte, mask to 8-bit
        }
        MemorySegment segment = arena.allocate(ValueLayout.JAVA_BYTE.byteSize() * uint8_arr.length);

        for (int i = 0; i < uint8_arr.length; i++) {
            segment.setAtIndex(ValueLayout.JAVA_BYTE, i, uint8_arr[i]);
        }
        return segment;
    }


    public static MemorySegment allocateUint8tArrayFromStringArray(final Arena arena, final String[] arr) {
        byte[] uint8Array = new byte[arr.length];

        for (int i = 0; i < arr.length; i++) {
            int val = Integer.parseInt(arr[i]);
            if (val < 0 || val > 255) {
                throw new IllegalArgumentException("Invalid uint8_t value: " + val);
            }
            uint8Array[i] = (byte) (val & 0xFF);
        }
        return arena.allocateFrom(ValueLayout.JAVA_BYTE, uint8Array);
    }
}
