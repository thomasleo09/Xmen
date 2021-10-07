package com.example.xmen.controller;

import com.example.xmen.constant.ResourceMapping;
import com.example.xmen.dto.IndividualInformation;
import com.example.xmen.service.api.DnaAnalysis;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DnaAnalysisController {

    private final DnaAnalysis dnaAnalysis;

    @PostMapping(path = ResourceMapping.GET_DNA_INFORMATION,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus dnaMutantInformation(@RequestBody IndividualInformation individualInformation) {
        return dnaAnalysis.isMutant(individualInformation.getDna()) ? HttpStatus.OK : HttpStatus.FORBIDDEN;
    }

}
