package com.example.xmen.service;

import com.example.xmen.service.api.DnaAnalysis;
import org.springframework.stereotype.Service;


@Service
public class DnaAnalysisImpl implements DnaAnalysis {

    @Override
    public boolean isMutant(String[] dna) {
        return false;
    }
}
