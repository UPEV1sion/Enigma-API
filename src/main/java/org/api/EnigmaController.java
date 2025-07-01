package org.api;

import jakarta.validation.Valid;
import org.api.restObjects.catalogue.CatalogueRequest;
import org.api.restObjects.catalogue.CatalogueResponse;
import org.api.restObjects.enigma.EnigmaResponse;
import org.api.restObjects.manualcyclometer.ManualCyclometerRequest;
import org.db.RotorCharacteristic;
import org.db.RotorCharacteristicService;
import org.nativeCInterface.EnigmaConnector;
import org.nativeCInterface.ManualCyclometerConnector;
import org.nativeCInterface.ffm.EnigmaInterface;
import org.api.restObjects.cyclometer.CyclometerCycles;
import org.api.restObjects.cyclometer.CyclometerResponse;
import org.api.restObjects.enigma.EnigmaRequest;
import org.nativeCInterface.ffm.ManualCyclometerInterface;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api")
public class EnigmaController {

    private final RotorCharacteristicService service;
    private final EnigmaConnector enigmaConnector;
    private final ManualCyclometerConnector manualCyclometerConnector;

    public EnigmaController(RotorCharacteristicService rotorCharacteristicService) {
        this.service = rotorCharacteristicService;
        this.enigmaConnector = new EnigmaInterface();
        this.manualCyclometerConnector = new ManualCyclometerInterface();
    }

    private CatalogueResponse<RotorCharacteristic> convertPageToDTO(Page<RotorCharacteristic> page) {
        return new CatalogueResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @PostMapping("/enigma")
    public ResponseEntity<?> enigma(@Valid @RequestBody EnigmaRequest req) {
        if (req.enigma() == null) {
            return ResponseEntity.badRequest().body("Empty 'enigma' object.");
        }

        Optional<String> output = Optional.empty();
        // Prüfen, ob input nicht leer ist
        if (req.enigma().input() != null && !req.enigma().input().isBlank()) {
//            Enigma enigma_normalized = enigmaService.normalizeEnigma(req.enigma());
//            output = enigmaConnector.getOutputFromEnigma(enigma_normalized);

            output = enigmaConnector.getOutputFromEnigma(req.enigma());
        }
        return output
                .map(str -> ResponseEntity.ok(new EnigmaResponse(str)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @PostMapping("/cyclometer")
    public ResponseEntity<?> cyclometer(@Valid @RequestBody ManualCyclometerRequest req) {
        if (req.enigma() == null) {
            return ResponseEntity.badRequest().body("Empty 'enigma' object.");
        }
        if (req.parameters() == null) {
            return ResponseEntity.badRequest().body("Empty 'parameters' object.");
        }
//        ManualCyclometerRequest normalizedRequest = enigmaService.normalizeManualCyclometerRequest(req);
//        Optional<CyclometerCycles> computedCycles = manualCyclometerConnector.getManualCyclesFromCyclometer(normalizedRequest);

        Optional<CyclometerCycles> computedCycles = manualCyclometerConnector.getManualCyclesFromCyclometer(req);
        return computedCycles
                .map(cycles -> ResponseEntity.ok(new CyclometerResponse(cycles)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/catalogue")
    public ResponseEntity<?> catalogue(@Valid @RequestBody CatalogueRequest req) {

        if (req.cycles() == null) {
            return ResponseEntity.badRequest().body("Empty 'cycles' object.");
        }
        if (req.parameters() == null) {
            return ResponseEntity.badRequest().body("Empty 'parameters' object.");
        }

        Page<RotorCharacteristic> page = service.searchConfig(req.cycles(), req.parameters());
        return ResponseEntity.ok(convertPageToDTO(page));
    }
}
