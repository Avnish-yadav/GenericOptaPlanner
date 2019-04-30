/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.optaplanner.generic.common.persistence;

import org.apache.commons.io.FilenameUtils;
import org.optaplanner.core.api.domain.solution.PlanningSolution;

import java.io.*;

/**
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public abstract class AbstractXlsxSolutionImporter<Solution_> extends AbstractSolutionImporter<Solution_> {

    private static final String DEFAULT_INPUT_FILE_SUFFIX = "xlsx";

    @Override
    public String getInputFileSuffix() {
        return DEFAULT_INPUT_FILE_SUFFIX;
    }

    public abstract XlsxInputBuilder<Solution_> createXlsxInputBuilder();

    @Override
    public Solution_ readSolution(File inputFile) {
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            XlsxInputBuilder<Solution_> xlsxInputBuilder = createXlsxInputBuilder();
            xlsxInputBuilder.setInputFile(inputFile);
            try {
                Solution_ solution = xlsxInputBuilder.readSolution();
                logger.info("Imported: {}", inputFile);
                return solution;
            } catch (IllegalArgumentException | IllegalStateException e) {
                throw new IllegalArgumentException("Exception in inputFile (" + inputFile + ")", e);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read the file (" + inputFile.getName() + ").", e);
        }
    }

    public static abstract class XlsxInputBuilder<Solution_> extends InputBuilder {

        protected File inputFile;

        public void setInputFile(File inputFile) {
            this.inputFile = inputFile;
        }


        public abstract Solution_ readSolution() throws IOException;

        // ************************************************************************
        // Helper methods
        // ************************************************************************

        public String getInputId() {
            return FilenameUtils.getBaseName(inputFile.getPath());
        }






    }
}
