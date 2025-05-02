package org.nativeCInterface.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static java.lang.foreign.ValueLayout.*;

public class JavaToCFactory {

    public static MemorySegment allocateTerminatedASCIIFromString(final String string, Arena arena) {
        byte[] stringBytes = string.getBytes(StandardCharsets.US_ASCII);

        MemorySegment segment = arena.allocate(JAVA_BYTE, stringBytes.length + 1);
        segment.asByteBuffer().clear();
        segment.asByteBuffer().put(stringBytes).put((byte) 0);
        return segment;
    }

    public static MemorySegment allocateTerminatedASCIIArrayFromStringArray(final String[] stringArr, final Arena arena) {
        int length = stringArr.length;

        // Allocate memory for the addresses of the strings + the NULL terminator
        MemorySegment segment = arena.allocate(
                ValueLayout.ADDRESS.byteSize() * (length + 1), // Size for holding addresses
                ValueLayout.ADDRESS.byteSize() // Align to the address size
        );

        // Write the addresses of the strings into the segment
        for (int i = 0; i < length; i++) {
            MemorySegment stringSegment = JavaToCFactory.allocateTerminatedASCIIFromString(stringArr[i], arena);
            segment.setAtIndex(ValueLayout.ADDRESS, i, MemorySegment.ofAddress(stringSegment.address()));
        }

        // Write the NULL terminator at the end of the segment
        segment.setAtIndex(ValueLayout.ADDRESS, length, MemorySegment.NULL);

        return segment;
    }

    public static MemorySegment allocatePaddedASCIIFromJavaString(final String string, int totalLength, Arena arena) {
        byte[] stringBytes = string.getBytes(StandardCharsets.US_ASCII);

        if (stringBytes.length > totalLength) {
            throw new IllegalArgumentException("Input exceeds total length");
        }

        byte[] padded = new byte[totalLength];
        System.arraycopy(stringBytes, 0, padded, 0, stringBytes.length); // pad with zeros

        MemorySegment segment = arena.allocate(JAVA_BYTE, totalLength);
        segment.asByteBuffer().put(padded);

        return segment;
    }

    public static MemorySegment allocateUint8_TArrayFromStringArray(final String[] arr, int expectedLength, final Arena arena) {
        if (arr.length != expectedLength) {
            throw new IllegalArgumentException("Input array length is not as expected: " + arr.length);
        }
        byte[] bytes = new byte[arr.length];

        for (int i = 0; i < arr.length; i++) {
            int val = Integer.parseInt(arr[i].trim());

            if (val < 0 || val > 255) {
                throw new IllegalArgumentException("Value out of range for uint8_t: " + val);
            }
            bytes[i] = (byte) (val & 0xFF);
        }
        return arena.allocateFrom(JAVA_BYTE, bytes);
    }

    public static MemorySegment allocateIntArrayFromStringArray(final String[] arr, final int expectedLength, final Arena arena) {
        if (arr.length != expectedLength) {
            throw new IllegalArgumentException("Expected length: " + expectedLength + ", but got: " + arr.length);
        }
        return arena.allocateFrom(JAVA_INT,
                Stream.of(arr)
                        .mapToInt(Integer::parseInt)
                        .toArray());
    }
}



