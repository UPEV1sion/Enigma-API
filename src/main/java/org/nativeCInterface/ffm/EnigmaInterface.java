package org.nativeCInterface.ffm;

import org.nativeCInterface.EnigmaConnector;
import org.nativeCInterface.NativeInterfaceConfig;
import org.springframework.stereotype.Component;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

import static java.lang.foreign.ValueLayout.ADDRESS;
import static java.lang.foreign.ValueLayout.JAVA_INT;

@Component
public class EnigmaInterface implements EnigmaConnector {

    private static final MethodHandle ENIGMA_ENCRYPT;

    static {
        final Linker linker = Linker.nativeLinker();
        final var symbolLookup = SymbolLookup.libraryLookup(NativeInterfaceConfig.LIBRARY_PATH, Arena.global());

        final var enigmaAddress = symbolLookup.find(NativeInterfaceConfig.ENIGMA_METHOD_NAME)
                                              .orElseThrow(() -> new ExceptionInInitializerError(
                                                      "Error: Function '"
                                                              + NativeInterfaceConfig.ENIGMA_METHOD_NAME
                                                              + "' not found in library"));
        ENIGMA_ENCRYPT = linker.downcallHandle(enigmaAddress, FunctionDescriptor.of(JAVA_INT, ADDRESS, ADDRESS));
    }

    @Override
    public Optional<org.api.restObjects.enigma.Enigma> getOutputFromEnigma(
            final org.api.restObjects.enigma.Enigma enigma) {
        final int byteSize = enigma.input().length() + 1;

        try (Arena arena = Arena.ofConfined()) {

            final var enigmaSeg = EnigmaFactory.createEnigmaSegment(enigma, arena);
            final var outputSeg = arena.allocate(byteSize);
            final int ret = (int) ENIGMA_ENCRYPT.invoke(enigmaSeg, outputSeg);

            if (ret == 0) {
                final String outputString = outputSeg.reinterpret(byteSize).getString(0);
                return Optional.of(enigma.withOutput(outputString));
            } else {
                return Optional.empty();
            }
        } catch (Throwable e) {
            throw new RuntimeException("Error within Enigma encryption", e);
        }
    }
}
