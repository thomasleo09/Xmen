package com.example.xmen.service.api;

import com.example.xmen.constant.AnalysisResults;

import java.util.Map;

public interface DnaDatabase {
    boolean saveAnalysis(String[] dna, AnalysisResults result);
    Map<String, String> getStats();
}
