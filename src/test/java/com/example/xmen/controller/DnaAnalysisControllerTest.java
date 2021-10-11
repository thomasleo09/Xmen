package com.example.xmen.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.example.xmen.constant.AnalysisResults;
import com.example.xmen.dto.IndividualInformation;
import com.example.xmen.service.api.DnaAnalysis;
import com.example.xmen.service.api.DnaDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class DnaAnalysisControllerTest {

    @InjectMocks
    private DnaAnalysisController dnaAnalysisController;

    @Mock
    private DnaAnalysis dnaAnalysis;

    @Mock
    private DnaDatabase dnaDatabase;

    private final String[] dnaArrayTest = new String[]{"CGAG","AGGC","TGCT","GGAA"};
    private final String[] dnaArrayInvalidSize = new String[]{"CGAG","AGG","TGCT","GGAAA"};
    private final String[] dnaArrayInvalidNitrogenousBase = new String[]{"CGAX","AGGC","TGCT","GGAA"};

    @Test
    public void givenRightDnaString_whenCallDnaMutantInformation_thenShouldCallIsMutantOfDnaAnalysis() {
        //execution
        dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayTest));
        //validation
        verify(dnaAnalysis, times(1)).isMutant(dnaArrayTest);
    }

    @Test
    public void whenCallAnalysisStats_thenShouldCallGetStatsOfDnaDatabase() {
        //execution
        dnaAnalysisController.analysisStats();
        //validation
        verify(dnaDatabase, times(1)).getStats();
    }

    @Test
    public void givenRightDnaString_whenCallDnaMutantInformation_thenShouldCallIsMutantOfDnaAnalysisAndIfReturnTrueShouldCallSaveAnalysisAndIfReturnTrueShouldReturnHttpStatusOk() {
        //setup
        Mockito.when(dnaAnalysis.isMutant(any())).thenReturn(true);
        Mockito.when(dnaDatabase.saveAnalysis(any(), any())).thenReturn(true);
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayTest));
        //validation
        assertThat(testStatus, is(HttpStatus.OK));
        verify(dnaAnalysis, times(1)).isMutant(dnaArrayTest);
        verify(dnaDatabase, times(1)).saveAnalysis(dnaArrayTest, AnalysisResults.MUTANT);
    }

    @Test
    public void givenRightDnaString_whenCallDnaMutantInformation_thenShouldCallIsMutantOfDnaAnalysisAndIfReturnFalseShouldCallSaveAnalysisAndIfReturnTrueShouldShouldReturnHttpStatusForbidden() {
        //setup
        Mockito.when(dnaAnalysis.isMutant(any())).thenReturn(false);
        Mockito.when(dnaDatabase.saveAnalysis(any(), any())).thenReturn(true);
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayTest));
        //validation
        assertThat(testStatus, is(HttpStatus.FORBIDDEN));
        verify(dnaAnalysis, times(1)).isMutant(dnaArrayTest);
        verify(dnaDatabase, times(1)).saveAnalysis(dnaArrayTest, AnalysisResults.HUMAN);
    }

    @Test
    public void givenRightDnaString_whenCallDnaMutantInformation_thenShouldCallIsMutantOfDnaAnalysisAndIfReturnTrueShouldCallSaveAnalysisAndIfReturnFalseShouldReturnHttpStatusInternalServerError() {
        //setup
        Mockito.when(dnaAnalysis.isMutant(any())).thenReturn(true);
        Mockito.when(dnaDatabase.saveAnalysis(any(), any())).thenReturn(false);
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayTest));
        //validation
        assertThat(testStatus, is(HttpStatus.INTERNAL_SERVER_ERROR));
        verify(dnaAnalysis, times(1)).isMutant(dnaArrayTest);
        verify(dnaDatabase, times(1)).saveAnalysis(dnaArrayTest, AnalysisResults.MUTANT);
    }

    @Test
    public void givenRightDnaString_whenCallDnaMutantInformation_thenShouldCallIsMutantOfDnaAnalysisAndIfReturnFalseShouldCallSaveAnalysisAndIfReturnFalseShouldShouldReturnHttpStatusInternalServerError() {
        //setup
        Mockito.when(dnaAnalysis.isMutant(any())).thenReturn(false);
        Mockito.when(dnaDatabase.saveAnalysis(any(), any())).thenReturn(false);
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayTest));
        //validation
        assertThat(testStatus, is(HttpStatus.INTERNAL_SERVER_ERROR));
        verify(dnaAnalysis, times(1)).isMutant(dnaArrayTest);
        verify(dnaDatabase, times(1)).saveAnalysis(dnaArrayTest, AnalysisResults.HUMAN);
    }

    @Test
    public void givenIncorrectDnaStringWithInvalidSize_whenCallDnaMutantInformation_thenShouldntCallIsMutantOfDnaAnalysisAndShouldReturnBadRequestStatus() {
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayInvalidSize));
        //validation
        assertThat(testStatus, is(HttpStatus.BAD_REQUEST));
        verify(dnaAnalysis, times(0)).isMutant(dnaArrayTest);
    }

    @Test
    public void givenIncorrectDnaStringWithInvalidNitrogenousBase_whenCallDnaMutantInformation_thenShouldntCallIsMutantOfDnaAnalysisAndShouldReturnBadRequestStatus() {
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayInvalidNitrogenousBase));
        //validation
        assertThat(testStatus, is(HttpStatus.BAD_REQUEST));
        verify(dnaAnalysis, times(0)).isMutant(dnaArrayTest);
    }

    private IndividualInformation getIndividualInformation(String[] dnaArray){
        return IndividualInformation.builder()
                .dna(dnaArray)
                .build();
    }
}
