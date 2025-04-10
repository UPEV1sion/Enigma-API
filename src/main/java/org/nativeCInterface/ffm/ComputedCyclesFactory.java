package org.nativeCInterface.ffm;

import org.api.restObjects.cyclometer.CyclometerCycles;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;
import java.util.stream.IntStream;

import static java.lang.foreign.ValueLayout.JAVA_INT;

public class ComputedCyclesFactory {
    private static final int ARRAY_SIZE = 26;
    private static final MemoryLayout ARRAY_LAYOUT = MemoryLayout.sequenceLayout(ARRAY_SIZE, JAVA_INT);
    private static final MemoryLayout CYCLOMETER_LAYOUT = MemoryLayout.structLayout(
            JAVA_INT.withName("cycles_1_4_len"),
            JAVA_INT.withName("cycles_2_5_len"),
            JAVA_INT.withName("cycles_3_6_len"),
            ARRAY_LAYOUT.withName("cycles_1_4"),
            ARRAY_LAYOUT.withName("cycles_2_5"),
            ARRAY_LAYOUT.withName("cycles_3_6")
    ).withName("ComputedCycles");

    private static final VarHandle CYCLES_1_4 = CYCLOMETER_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("cycles_1_4"), MemoryLayout.PathElement.sequenceElement());
    private static final VarHandle CYCLES_2_5 = CYCLOMETER_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("cycles_2_5"), MemoryLayout.PathElement.sequenceElement());
    private static final VarHandle CYCLES_3_6 = CYCLOMETER_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("cycles_3_6"), MemoryLayout.PathElement.sequenceElement());
    private static final VarHandle CYCLES_1_4_LEN = CYCLOMETER_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("cycles_1_4_len"));
    private static final VarHandle CYCLES_2_5_LEN = CYCLOMETER_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("cycles_2_5_len"));
    private static final VarHandle CYCLES_3_6_LEN = CYCLOMETER_LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("cycles_3_6_len"));

    public static MemorySegment createCyclometerCyclesSegment(final Arena arena) {
        return arena.allocate(CYCLOMETER_LAYOUT);
    }

    private static int[] createCycleArray(final VarHandle cycleHandle, final VarHandle cycleLengthHandle, final MemorySegment cycleSeg) {
        final int len = (int) cycleLengthHandle.get(cycleSeg, 0);

        return IntStream.range(0, len)
                .map(i -> (int) cycleHandle.get(cycleSeg, 0 , i))
                .toArray();
    }

    public static CyclometerCycles createCyclometerCycles(final MemorySegment computedCyclesSeg) {
        final int[] firstToThird   = createCycleArray(CYCLES_1_4, CYCLES_1_4_LEN, computedCyclesSeg);
        final int[] secondToFourth = createCycleArray(CYCLES_2_5, CYCLES_2_5_LEN, computedCyclesSeg);
        final int[] thirdToSixth   = createCycleArray(CYCLES_3_6, CYCLES_3_6_LEN, computedCyclesSeg);

        return new CyclometerCycles(firstToThird, secondToFourth, thirdToSixth);
    }

}
