package org.nativeCInterface;

import org.api.restObjects.enigma.Enigma;

import java.util.Optional;

public interface EnigmaConnector {
    Optional<String> getOutputFromEnigma(final Enigma enigma);
}
