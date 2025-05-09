package org.api;

import jakarta.validation.Valid;
import org.api.restObjects.catalogue.CatalogueRequest;
import org.api.restObjects.manualcyclometer.ManualCyclometerRequest;
import org.db.RotorCharacteristic;
import org.db.RotorCharacteristicService;
import org.nativeCInterface.CyclometerConnector;
import org.nativeCInterface.EnigmaConnector;
import org.nativeCInterface.ManualCyclometerConnector;
import org.nativeCInterface.ffm.CyclometerInterface;
import org.nativeCInterface.ffm.EnigmaInterface;
import org.api.restObjects.cyclometer.CyclometerCycles;
import org.api.restObjects.cyclometer.CyclometerRequest;
import org.api.restObjects.cyclometer.CyclometerResponse;
import org.api.restObjects.enigma.EnigmaRequest;
import org.nativeCInterface.ffm.ManualCyclometerInterface;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class EnigmaController {

    private final RotorCharacteristicService service;
    private final EnigmaConnector enigmaConnector;
    private final CyclometerConnector cyclometerConnector;
    private final ManualCyclometerConnector manualCyclometerConnector;

    public EnigmaController(RotorCharacteristicService rotorCharacteristicService) {
        this.service = rotorCharacteristicService;
        this.enigmaConnector = new EnigmaInterface();
        this.cyclometerConnector = new CyclometerInterface();
        this.manualCyclometerConnector = new ManualCyclometerInterface();

    }

    @PostMapping("/enigma")
    public ResponseEntity<EnigmaRequest> enigma(@Valid @RequestBody EnigmaRequest req) {
        System.out.println("EnigmaController::enigma");
        Optional<org.api.restObjects.enigma.Enigma> output = enigmaConnector.getOutputFromEnigma(req.enigma());

        return output
                .map(enigma -> ResponseEntity.ok(new EnigmaRequest(enigma)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/cyclometer")
    public ResponseEntity<CyclometerResponse> cyclometer(@Valid @RequestBody CyclometerRequest req) {
        System.out.println("EnigmaController::cyclometer");
        Optional<CyclometerCycles> computedCycles = cyclometerConnector.getCyclesFromCyclometer(req);
        return computedCycles
                .map(cycles -> ResponseEntity.ok(new CyclometerResponse(cycles)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/manualcyclometer")
    public ResponseEntity<CyclometerResponse> cyclometer(@Valid @RequestBody ManualCyclometerRequest req) {
        System.out.println("EnigmaController::manualcyclometer");
        Optional<CyclometerCycles> computedCycles = manualCyclometerConnector.getManualCyclesFromCyclometer(req);
        return computedCycles
                .map(cycles -> ResponseEntity.ok(new CyclometerResponse(cycles)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/catalogue")
    public Page<RotorCharacteristic> catalogue(@Valid @RequestBody CatalogueRequest req) {
        System.out.println("EnigmaController::catalog");
        return service.searchConfig(req.cycles(), req.parameters().page());
    }

}
