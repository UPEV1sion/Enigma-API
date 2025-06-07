package org.api;

import jakarta.validation.Valid;
import org.api.restObjects.catalogue.CatalogueRequest;
import org.api.restObjects.enigma.Enigma;
import org.api.restObjects.enigma.EnigmaResponse;
import org.api.restObjects.manualcyclometer.ManualCyclometerRequest;
import org.api.restObjects.catalogue.PageDTO;
import org.db.RotorCharacteristic;
import org.db.RotorCharacteristicService;
import org.nativeCInterface.CyclometerConnector;
import org.nativeCInterface.EnigmaConnector;
import org.nativeCInterface.ManualCyclometerConnector;
import org.nativeCInterface.ffm.CyclometerInterface;
import org.nativeCInterface.ffm.EnigmaInterface;
import org.api.restObjects.cyclometer.CyclometerCycles;
import org.api.restObjects.cyclometer.CyclometerResponse;
import org.api.restObjects.enigma.EnigmaRequest;
import org.nativeCInterface.ffm.ManualCyclometerInterface;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EnigmaController {

    private final EnigmaService enigmaService;
    private final RotorCharacteristicService service;
    private final EnigmaConnector enigmaConnector;
    private final CyclometerConnector cyclometerConnector;
    private final ManualCyclometerConnector manualCyclometerConnector;




    public EnigmaController(RotorCharacteristicService rotorCharacteristicService, EnigmaService enigmaService) {
        this.enigmaService = enigmaService;
        this.service = rotorCharacteristicService;
        this.enigmaConnector = new EnigmaInterface();
        this.cyclometerConnector = new CyclometerInterface();
        this.manualCyclometerConnector = new ManualCyclometerInterface();

    }

    @PostMapping("/enigma")
    public ResponseEntity<EnigmaResponse> enigma(@Valid @RequestBody EnigmaRequest req) {
        Optional<String> output = Optional.empty();

        // Prüfen, ob input nicht leer ist
        if (req.enigma() != null && req.enigma().input() != null && !req.enigma().input().isBlank()) {
            Enigma enigma_normalized = enigmaService.normalizeEnigma(req.enigma());
            output = enigmaConnector.getOutputFromEnigma(enigma_normalized);
        }
        return output
                .map(str -> ResponseEntity.ok(new EnigmaResponse(str)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }



//    @PostMapping("/cyclometer")
//    public ResponseEntity<CyclometerResponse> cyclometer(@Valid @RequestBody CyclometerRequest req) {
//        CyclometerRequest normalizedRequest = enigmaService.normalizeCyclometerRequest(req);
//        Optional<CyclometerCycles> computedCycles = cyclometerConnector.getCyclesFromCyclometer(normalizedRequest);
//        return computedCycles
//                .map(cycles -> ResponseEntity.ok(new CyclometerResponse(cycles)))
//                .orElseGet(() -> ResponseEntity.badRequest().build());
//    }

    @PostMapping("/cyclometer")
    public ResponseEntity<CyclometerResponse> cyclometer(@Valid @RequestBody ManualCyclometerRequest req) {
        ManualCyclometerRequest normalizedRequest = enigmaService.normalizeManualCyclometerRequest(req);
        Optional<CyclometerCycles> computedCycles = manualCyclometerConnector.getManualCyclesFromCyclometer(normalizedRequest);
        return computedCycles
                .map(cycles -> ResponseEntity.ok(new CyclometerResponse(cycles)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/catalogue")
    public PageDTO<RotorCharacteristic> catalogue(@Valid @RequestBody CatalogueRequest req) {
        // Fetch the Page of RotorCharacteristics from the service
        Page<RotorCharacteristic> page = service.searchConfig(req.cycles(), req.parameters());

        // Convert the Page to a PageDTO for stable JSON serialization
        return convertPageToDTO(page);
    }
    private PageDTO<RotorCharacteristic> convertPageToDTO(Page<RotorCharacteristic> page) {
        return new PageDTO<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
