package org.nativeCInterface.ffm;

import org.api.restObjects.enigma.Enigma;
import org.nativeCInterface.NativeInterfaceConfig;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.util.Map;

import static java.lang.foreign.ValueLayout.*;

public class EnigmaFactory {

    private static final MemoryLayout ENIGMA_LAYOUT = MemoryLayout.structLayout(
            MemoryLayout.sequenceLayout(26, JAVA_BYTE).withName("plugboard"),
            MemoryLayout.paddingLayout(6), // Pad to align to 8 bytes
            ADDRESS.withName("message"),
            ADDRESS.withName("rotor_positions"),
            ADDRESS.withName("ring_settings"),
            ADDRESS.withName("rotors"),
            JAVA_INT.withName("type"),
            JAVA_INT.withName("reflector")
    ).withName("EnigmaConfiguration");


    private static void setEnigmaField(final MemorySegment segment, final String fieldName, final Object value) {
        final long offset = ENIGMA_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement(fieldName));

        switch (value) {
            case MemorySegment memSeg -> {
                if (ENIGMA_LAYOUT.select(PathElement.groupElement(fieldName)) instanceof SequenceLayout seq &&
                        seq.elementLayout().equals(JAVA_BYTE)) {
                    segment.asSlice(offset, seq.byteSize()).copyFrom(memSeg);
                } else {
                    segment.set(ADDRESS, offset, memSeg);
                }
            }
            case Integer intVal -> segment.set(JAVA_INT, offset, intVal);
            case Character charVal -> segment.set(JAVA_INT, offset, charVal);
            default -> throw new IllegalArgumentException("Unsupported field type: " + value.getClass());
        }
    }

    private static Map<String, Object> getEnigmaFieldsSegmentMap(final Enigma enigma, final Arena arena) {

        final var model = enigma.model();
        final var plugboardSeg = JavaToCFactory.allocatePaddedASCIIFromJavaString(enigma.plugboard(), NativeInterfaceConfig.ALPHABET_SIZE, arena);
        final var messageSeg = JavaToCFactory.allocateTerminatedASCIIFromString(enigma.input(), arena);
        final var rotorPosSeg = JavaToCFactory.allocateUint8_TArrayFromIntegerArray(enigma.positions(), arena);
        final var ringPosSeg = JavaToCFactory.allocateUint8_TArrayFromIntegerArray(enigma.rings(), arena);
        final var rotorTypeSeg = JavaToCFactory.allocateIntArrayFromIntegerArray(enigma.rotors(), arena);

        return Map.of(
                "plugboard", plugboardSeg,
                "message", messageSeg,
                "rotor_positions", rotorPosSeg,
                "ring_settings", ringPosSeg,
                "rotors", rotorTypeSeg,
                "type", model,
                "reflector", enigma.reflector()
                );
    }

    public static MemorySegment createEnigmaSegment(final Enigma enigma, final Arena arena) {
        final var segment = arena.allocate(ENIGMA_LAYOUT);
        getEnigmaFieldsSegmentMap(enigma, arena)
                .forEach((field, value) -> setEnigmaField(segment, field, value));

        return segment;
    }

}
