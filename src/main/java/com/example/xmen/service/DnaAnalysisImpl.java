package com.example.xmen.service;

import com.example.xmen.constant.Directions;
import com.example.xmen.constant.DnaConstants;
import com.example.xmen.service.api.DnaAnalysis;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@Service
public class DnaAnalysisImpl implements DnaAnalysis {

    public int numberTotalSequences;

    @Override
    public boolean isMutant(String[] dna) {
        String[][] dnaMatrix = new String[dna.length][dna.length];
        IntStream.range(0, dna.length).forEach(dnaStringIndex -> {
            String[] dnaArray = dna[dnaStringIndex].split("");
            IntStream.range(0, dnaArray.length).forEach(nitrogenousBaseIndex -> {
                dnaMatrix[dnaStringIndex][nitrogenousBaseIndex] = dnaArray[nitrogenousBaseIndex];
            });
        });
        numberTotalSequences = getNumberOfSequences(dnaMatrix, Directions.HORIZONTAL) + getNumberOfSequences(dnaMatrix, Directions.VERTICAL)
                + getNumberOfSequences(dnaMatrix, Directions.OBLIQUE_UP) + getNumberOfSequences(dnaMatrix, Directions.OBLIQUE_DOWN);
        return numberTotalSequences > 1;
    }

    private int getNumberOfSequences(String[][] dnaMatrix, Directions direction) {
        int sequencesNumber = 0;
        int verticalIncrement = 0, horizontalIncrement = 0;
        int rowBegin = 0, rowEnd = dnaMatrix.length - 1, colBegin = 0, colEnd = dnaMatrix.length - 1;
        List<int[]> checkedPositions = new ArrayList<>();
        switch (direction) {
            case VERTICAL:
                verticalIncrement = 1;
                rowEnd = dnaMatrix.length - DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES;
                break;
            case HORIZONTAL:
                horizontalIncrement = 1;
                colEnd = dnaMatrix.length - DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES;
                break;
            case OBLIQUE_DOWN:
                verticalIncrement = 1;
                horizontalIncrement = 1;
                rowEnd = dnaMatrix.length - DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES;
                colEnd = dnaMatrix.length - DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES;
                break;
            case OBLIQUE_UP:
                verticalIncrement = 1;
                horizontalIncrement = -1;
                rowEnd = dnaMatrix.length - DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES;
                colBegin = DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES - 1;
                break;
        }
        for (int row = rowBegin; row <= rowEnd; row++) {
            for (int col = colBegin; col <= colEnd; col++) {
                int repetitionCounter = 0, tempRow = row, tempCol = col;
                while (!positionAlreadyChecked(checkedPositions,
                        new int[]{tempRow, tempCol}) && Objects.equals(dnaMatrix[tempRow][tempCol],
                        dnaMatrix[tempRow + verticalIncrement][tempCol + horizontalIncrement])) {
                    checkedPositions.add(new int[]{tempRow, tempCol});
                    repetitionCounter++;
                    tempRow += verticalIncrement;
                    tempCol += horizontalIncrement;
                    if ((tempRow + verticalIncrement) >= dnaMatrix.length ||
                            (tempCol + horizontalIncrement) >= dnaMatrix.length ||
                            (tempCol + horizontalIncrement) < 0) {
                        break;
                    }
                    if (repetitionCounter >= DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES) {
                        break;
                    }
                }
                if (repetitionCounter >= DnaConstants.NUMBER_OF_REPEATED_NITROGENOUS_BASES - 1) {
                    sequencesNumber++;
                }
            }
        }
        return sequencesNumber;
    }

    private boolean positionAlreadyChecked(List<int[]> checkedPositions, int[] position) {
        AtomicBoolean alreadyChecked = new AtomicBoolean(false);
        checkedPositions.forEach(positionsArray -> {
            alreadyChecked.set(alreadyChecked.get() || positionsArray[0] == position[0] && positionsArray[1] == position[1]);
        });
        return alreadyChecked.get();
    }
}
