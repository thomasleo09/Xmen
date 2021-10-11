package com.example.xmen.controller;

import com.example.xmen.constant.AnalysisResults;
import com.example.xmen.constant.DnaConstants;
import com.example.xmen.constant.ResourceMapping;
import com.example.xmen.dto.IndividualInformation;
import com.example.xmen.entity.Analysis;
import com.example.xmen.service.api.DnaAnalysis;
import com.example.xmen.service.api.DnaDatabase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@AllArgsConstructor
public class DnaAnalysisController {

    private final DnaAnalysis dnaAnalysis;

    private final DnaDatabase dnaDatabase;

    @GetMapping(path = ResourceMapping.GET_DNA_STATISTICS,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> analysisStats() {
        return dnaDatabase.getStats();
    }

    @PostMapping(path = ResourceMapping.GET_DNA_INFORMATION,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus dnaMutantInformation(@RequestBody IndividualInformation individualInformation) {

        return isValidDna(individualInformation.getDna()) ?
                (dnaAnalysis.isMutant(individualInformation.getDna()) ?
                        (dnaDatabase.saveAnalysis(individualInformation.getDna(), AnalysisResults.MUTANT) ?
                                HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR) :
                        (dnaDatabase.saveAnalysis(individualInformation.getDna(), AnalysisResults.HUMAN) ?
                                HttpStatus.FORBIDDEN : HttpStatus.INTERNAL_SERVER_ERROR)) :
                HttpStatus.BAD_REQUEST;
    }

    private boolean isValidDna(String[] dna) {
        AtomicBoolean invalidSize = new AtomicBoolean(false);
        AtomicBoolean invalidNitrogenousBase = new AtomicBoolean(false);
        Arrays.stream(dna).forEach(dnaString -> invalidSize.set(invalidSize.get() || (dnaString.split("").length != dna.length)));
        Arrays.stream(dna).forEach(dnaString -> Arrays.stream(dnaString.split(""))
                .forEach(nitrogenousBase -> invalidNitrogenousBase.set(invalidNitrogenousBase.get() || !Arrays.asList(DnaConstants.NITROGENOUS_BASES).contains(nitrogenousBase))));
        return !invalidSize.get() && !invalidNitrogenousBase.get();
    }
}
