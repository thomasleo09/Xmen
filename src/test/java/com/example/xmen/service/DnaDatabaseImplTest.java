package com.example.xmen.service;

import com.example.xmen.constant.AnalysisResults;
import com.example.xmen.entity.Analysis;
import com.example.xmen.repository.AnalysisRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class DnaDatabaseImplTest {

    @InjectMocks
    DnaDatabaseImpl dnaDatabase;

    @Mock
    AnalysisRepository analysisRepository;

    private final String[] dnaHumanArrayTest = new String[]{"CGAG", "AGGC", "TACT", "GGAA"};

    @Test
    public void givenRightDnaStringAndResult_whenCallSaveAnalysis_thenShouldReturnTrueAndCallSave() {
        //setup
        Analysis analysis = new Analysis();
        analysis.setDna(Arrays.toString(dnaHumanArrayTest));
        analysis.setResult(AnalysisResults.HUMAN.toString());
        Mockito.when(analysisRepository.save(analysis)).thenReturn(analysis);
        //execution
        boolean saveAnalysisTest = dnaDatabase.saveAnalysis(dnaHumanArrayTest, AnalysisResults.HUMAN);
        //validation
        assertThat(saveAnalysisTest, is(true));
        verify(analysisRepository, times(1)).save(analysis);
    }

    @Test
    public void givenIncorrectDbConnectionThrowException_whenCallSaveAnalysis_thenShouldReturnFalseAndNotCallSave() {
        //setup
        Analysis analysis = new Analysis();
        analysis.setDna(Arrays.toString(dnaHumanArrayTest));
        analysis.setResult(AnalysisResults.HUMAN.toString());
        Mockito.when(analysisRepository.save(Mockito.any())).thenThrow(new IllegalArgumentException());
        //execution
        boolean saveAnalysisTest = dnaDatabase.saveAnalysis(dnaHumanArrayTest, AnalysisResults.HUMAN);
        //validation
        assertThat(saveAnalysisTest, is(false));
    }

    @Test
    public void whenCallGetStats_thenShouldReturnMapWithCountsAndRatio() {
        //setup
        Mockito.when(analysisRepository.countByResult(AnalysisResults.MUTANT.toString())).thenReturn(3L);
        Mockito.when(analysisRepository.countByResult(AnalysisResults.HUMAN.toString())).thenReturn(6L);
        //execution
        Map<String, String> mapTest = dnaDatabase.getStats();
        //validation
        assertThat(mapTest, equalTo(getMapTest()));
    }

    @Test
    public void givenZeroCountHumanDna_whenCallGetStats_thenShouldReturnMapWithCountsAndNullRatio() {
        //setup
        Mockito.when(analysisRepository.countByResult(AnalysisResults.MUTANT.toString())).thenReturn(3L);
        Mockito.when(analysisRepository.countByResult(AnalysisResults.HUMAN.toString())).thenReturn(0L);
        //execution
        Map<String, String> mapTest = dnaDatabase.getStats();
        //validation
        assertThat(mapTest, equalTo(getZeroMapTest()));
    }

    @Test
    public void givenException_whenCallGetStats_thenShouldReturnNull() {
        //setup
        Mockito.when(analysisRepository.countByResult(AnalysisResults.MUTANT.toString())).thenThrow(new IllegalArgumentException());
        //execution
        Map<String, String> mapTest = dnaDatabase.getStats();
        //validation
        assertThat(mapTest, equalTo(null));
    }

    private HashMap<String, String> getMapTest(){
        HashMap<String, String> statsMap = new HashMap<>();
        statsMap.put("count_mutant_dna", "3");
        statsMap.put("count_human_dna", "6");
        statsMap.put("ratio", "0.5");
        return statsMap;
    }

    private HashMap<String, String> getZeroMapTest(){
        HashMap<String, String> statsMap = new HashMap<>();
        statsMap.put("count_mutant_dna", "3");
        statsMap.put("count_human_dna", "0");
        statsMap.put("ratio", null);
        return statsMap;
    }
}
