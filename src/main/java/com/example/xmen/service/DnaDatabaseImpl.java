package com.example.xmen.service;

import com.example.xmen.constant.AnalysisResults;
import com.example.xmen.entity.Analysis;
import com.example.xmen.repository.AnalysisRepository;
import com.example.xmen.service.api.DnaDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DnaDatabaseImpl implements DnaDatabase {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Override
    public boolean saveAnalysis(String[] dna, AnalysisResults result) {
        try {
            Analysis analysis = new Analysis();
            analysis.setDna(Arrays.toString(dna));
            analysis.setResult(result.toString());
            analysisRepository.save(analysis);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Map<String, String> getStats() {
        try {
            HashMap<String, String> statsMap = new HashMap<>();
            long countMutantDna = analysisRepository.countByResult(AnalysisResults.MUTANT.toString());
            long countHumanDna = analysisRepository.countByResult(AnalysisResults.HUMAN.toString());
            statsMap.put("count_mutant_dna", Long.toString(countMutantDna));
            statsMap.put("count_human_dna", Long.toString(countHumanDna));
            if(countHumanDna>0)
                statsMap.put("ratio", String.valueOf((double)countMutantDna/countHumanDna));
            else
                statsMap.put("ratio", null);
            return statsMap;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
