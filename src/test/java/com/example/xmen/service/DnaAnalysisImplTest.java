package com.example.xmen.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class DnaAnalysisImplTest {

    @InjectMocks
    DnaAnalysisImpl dnaAnalysis;

    private final String[] dnaHumanArrayTest = new String[]{"CGAG", "AGGC", "TACT", "GGAA"};
    private final String[] dnaMutanArrayTest = new String[]{"CGAG","AGGC","TGCT","GGAA"};
    private final String[] dnaMutanFiveArrayTest = new String[]{"ACTGG","GATCC","TCCTC","CGGAC","GGGGC"};
    private final String[] dnaMutanSixArrayTest = new String[]{"AAAACT","AGCAGT","AAAATT","AAATCT","ATACCA","TTAAAA"};
    private final String[] dnaMutanSevenArrayTest = new String[]{"AAGACTA","CATAGTC","AGACTCG","ACAACTA","ATACTAG","TTGTCAC","GGCAGTG"};
    private final String[] dnaMutanTenArrayTest = new String[]{"AAAAAAATTT", "CTTACAACGT", "CGTGCATCAT", "CAGATAGAAT", "TCACACTGAG", "GAACTAAAAC", "ACGCCTAGGA", "ACTCGCAGAC", "AGTATTAACC", "ACCCCGATTC"};

    @Test
    public void givenRightDnaStringWithHumanInformation_whenCallIsMutant_thenShouldReturnFalse() {
        //execution
        boolean isMutantResultTest = dnaAnalysis.isMutant(dnaHumanArrayTest);
        //validation
        assertThat(isMutantResultTest, is(false));
    }

    @Test
    public void givenRightDnaStringWithMutantInformation_whenCallIsMutant_thenShouldReturnTrue() {
        //execution
        boolean isMutantResultTest = dnaAnalysis.isMutant(dnaMutanArrayTest);
        //validation
        assertThat(isMutantResultTest, is(true));
    }

    @Test
    public void givenRightDnaStringWithMutantInformation_whenCallIsMutant_thenShouldReturnTrueAndTheRightNumberOfSequences() {
        //execution
        boolean isMutantResultTest = dnaAnalysis.isMutant(dnaMutanFiveArrayTest);
        //validation
        assertThat(isMutantResultTest, is(true));
        assertThat(dnaAnalysis.numberTotalSequences, is(2));
    }

    @Test
    public void givenRightDnaStringWithMutantInformationAndStringCrosses_whenCallIsMutant_thenShouldReturnTrueAndTheRightNumberOfSequences() {
        //execution
        boolean isMutantResultTest = dnaAnalysis.isMutant(dnaMutanSixArrayTest);
        //validation
        assertThat(isMutantResultTest, is(true));
        assertThat(dnaAnalysis.numberTotalSequences, is(8));
    }

    @Test
    public void givenRightDnaStringWithMutantInformationAndObliqueUpAndDownSequences_whenCallIsMutant_thenShouldReturnTrueAndTheRightNumberOfSequences() {
        //execution
        boolean isMutantResultTest = dnaAnalysis.isMutant(dnaMutanSevenArrayTest);
        //validation
        assertThat(isMutantResultTest, is(true));
        assertThat(dnaAnalysis.numberTotalSequences, is(2));
    }

    @Test
    public void givenRightDnaStringWithMutantInformationAndTwoStringOfTheSameLetterInRow_whenCallIsMutant_thenShouldReturnTrueAndTheRightNumberOfSequences() {
        //execution
        boolean isMutantResultTest = dnaAnalysis.isMutant(dnaMutanTenArrayTest);
        //validation
        assertThat(isMutantResultTest, is(true));
        assertThat(dnaAnalysis.numberTotalSequences, is(12));
    }
}
