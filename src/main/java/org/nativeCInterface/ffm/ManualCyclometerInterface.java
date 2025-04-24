package org.nativeCInterface.ffm;

import org.api.restObjects.cyclometer.CyclometerCycles;
import org.api.restObjects.manualcyclometer.ManualCyclometerRequest;
import org.nativeCInterface.ManualCyclometerConnector;
import org.nativeCInterface.NativeInterfaceConfig;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Optional;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class ManualCyclometerInterface implements ManualCyclometerConnector {

    private static final MethodHandle MANUAL_CYCLOMETER_CREATE_CYCLES;

    static {
        final Linker linker = Linker.nativeLinker();
        final var symbolLookup = SymbolLookup.libraryLookup(NativeInterfaceConfig.LIBRARY_PATH, Arena.global());

        final var cyclometerAddress = symbolLookup.find(NativeInterfaceConfig.MANUAL_CYCLOMETER_METHOD_NAME)
                .orElseThrow(() -> new ExceptionInInitializerError(
                        "Error: Function '"
                                + NativeInterfaceConfig.MANUAL_CYCLOMETER_METHOD_NAME
                                + "' not found in library"));
        final var cyclometerDescriptor = FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_INT, ADDRESS, ADDRESS);
        MANUAL_CYCLOMETER_CREATE_CYCLES = linker.downcallHandle(cyclometerAddress, cyclometerDescriptor);
    }

    @Override
    public Optional<CyclometerCycles> getManualCyclesFromCyclometer(final ManualCyclometerRequest req) {

        try (Arena arena = Arena.ofConfined()) {
            final var enigmaSeg = EnigmaFactory.createEnigmaSegment(req.enigma(), arena);
            final var computedCyclesSeg = ComputedCyclesFactory.createCyclometerCyclesSegment(arena);
            final int dailyKeyCount = Integer.parseInt(req.parameters().daily_key_count());
            final var manual_keys_seg = JavaToCFactory.JavaStringArrayToTerminatedUTF8(req.parameters().manual_keys(), arena);
            System.out.println(Arrays.toString(req.parameters().manual_keys()));

            final int ret = (int) MANUAL_CYCLOMETER_CREATE_CYCLES.invoke(enigmaSeg, dailyKeyCount, manual_keys_seg, computedCyclesSeg);
            if (ret == 0) {
                final CyclometerCycles computedCycles = ComputedCyclesFactory.createCyclometerCycles(computedCyclesSeg);
                return Optional.of(computedCycles);
            } else {
                return Optional.empty();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
