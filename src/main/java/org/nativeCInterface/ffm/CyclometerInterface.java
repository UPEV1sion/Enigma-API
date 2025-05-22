package org.nativeCInterface.ffm;

import org.api.restObjects.cyclometer.CyclometerCycles;
import org.api.restObjects.cyclometer.CyclometerRequest;
import org.nativeCInterface.CyclometerConnector;
import org.nativeCInterface.NativeInterfaceConfig;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

public class CyclometerInterface implements CyclometerConnector {

    private static final MethodHandle CYCLOMETER_CREATE_CYCLES;

    static {
        final Linker linker = Linker.nativeLinker();
        final var symbolLookup = SymbolLookup.libraryLookup(NativeInterfaceConfig.LIBRARY_PATH, Arena.global());

        final var cyclometerAddress = symbolLookup.find(NativeInterfaceConfig.CYCLOMETER_METHOD_NAME)
                                                  .orElseThrow(() -> new ExceptionInInitializerError(
                                                          "Error: Function '"
                                                                  + NativeInterfaceConfig.CYCLOMETER_METHOD_NAME
                                                                  + "' not found in library"));
        final var cyclometerDescriptor = FunctionDescriptor.of(JAVA_INT, ADDRESS, JAVA_INT, ADDRESS);
        CYCLOMETER_CREATE_CYCLES = linker.downcallHandle(cyclometerAddress, cyclometerDescriptor);
    }

    @Override
    public Optional<CyclometerCycles> getCyclesFromCyclometer(final CyclometerRequest req) {

        try (Arena arena = Arena.ofConfined()) {

            final var enigmaSeg = EnigmaFactory.createEnigmaSegment(req.enigma(), arena);
            final var computedCyclesSeg = ComputedCyclesFactory.createCyclometerCyclesSegment(arena);
            final int dailyKeyCount = req.parameters().daily_key_count();
            final int ret = (int) CYCLOMETER_CREATE_CYCLES.invoke(enigmaSeg, dailyKeyCount, computedCyclesSeg);

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
