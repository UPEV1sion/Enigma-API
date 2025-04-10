package org.nativeCInterface.ffm;

import org.api.restObjects.enigma.Enigma;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class EnigmaFactory {

    private static final MemoryLayout ENIGMA_LAYOUT = MemoryLayout.structLayout(
            ADDRESS.withName("plugboard"),
            ADDRESS.withName("message"),
            ADDRESS.withName("rotor_positions"),
            ADDRESS.withName("ring_settings"),
            ADDRESS.withName("rotors"),
            JAVA_INT.withName("type"),
            JAVA_INT.withName("reflector")
    ).withName("EnigmaConfiguration");

    private static MemorySegment allocateIntArrayFromStringArray(final Arena arena, final String[] arr) {
        return arena.allocateFrom(JAVA_INT,
                                  Stream.of(arr)
                                        .mapToInt(Integer::parseInt)
                                        .toArray());
    }

    private static void setEnigmaField(final MemorySegment segment, final String fieldName, final Object value) {
        final long offset = ENIGMA_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement(fieldName));

        switch (value) {
            case MemorySegment memSeg -> segment.set(ADDRESS, offset, memSeg);
            case Integer intVal -> segment.set(JAVA_INT, offset, intVal);
            case Character charVal -> segment.set(JAVA_INT, offset, charVal);
            default -> throw new IllegalArgumentException("Unsupported field type: " + value.getClass());
        }
    }

    private static Map<String, Object> getEnigmaFieldsSegmentMap(final Enigma enigma, final Arena arena) {
        final var plugboardSeg = arena.allocateFrom(enigma.plugboard());
        final var messageSeg = arena.allocateFrom(enigma.input());
        final var rotorPosSeg = allocateIntArrayFromStringArray(arena, enigma.positions());
        final var ringPosSeg = allocateIntArrayFromStringArray(arena, enigma.rings());
        final var rotorTypeSeg = allocateIntArrayFromStringArray(arena, enigma.rotors());

        return Map.of(
                "plugboard", plugboardSeg,
                "message", messageSeg,
                "rotor_positions", rotorPosSeg,
                "ring_settings", ringPosSeg,
                "rotors", rotorTypeSeg,
                "type", Integer.parseInt(enigma.model()),
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
