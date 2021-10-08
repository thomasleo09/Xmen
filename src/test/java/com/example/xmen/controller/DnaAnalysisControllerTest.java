package com.example.xmen.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import com.example.xmen.dto.IndividualInformation;
import com.example.xmen.service.api.DnaAnalysis;
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
    public void givenRightDnaString_whenCallDnaMutantInformation_thenShouldCallIsMutantOfDnaAnalysisAndIfReturnTrueShouldReturnHttpStatusOk() {
        //setup
        Mockito.when(dnaAnalysis.isMutant(any())).thenReturn(true);
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayTest));
        //validation
        assertThat(testStatus, is(HttpStatus.OK));
        verify(dnaAnalysis, times(1)).isMutant(dnaArrayTest);
    }

    @Test
    public void givenRightDnaString_whenCallDnaMutantInformation_thenShouldCallIsMutantOfDnaAnalysisAndIfReturnFalseShouldReturnHttpStatusForbidden() {
        //setup
        Mockito.when(dnaAnalysis.isMutant(any())).thenReturn(false);
        //execution
        HttpStatus testStatus = dnaAnalysisController.dnaMutantInformation(getIndividualInformation(dnaArrayTest));
        //validation
        assertThat(testStatus, is(HttpStatus.FORBIDDEN));
        verify(dnaAnalysis, times(1)).isMutant(dnaArrayTest);
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
