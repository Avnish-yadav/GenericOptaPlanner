/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
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

import com.example.optaplanner.generic.jdom.Document;
import com.example.optaplanner.generic.jdom.Element;
import com.example.optaplanner.generic.jdom.JDOMException;
import org.apache.commons.io.FilenameUtils;
import org.optaplanner.core.api.domain.solution.PlanningSolution;

import java.io.*;

/**
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public abstract class AbstractXmlSolutionImporter<Solution_> extends AbstractSolutionImporter<Solution_> {

    private static final String DEFAULT_INPUT_FILE_SUFFIX = "xml";

    @Override
    public String getInputFileSuffix() {
        return DEFAULT_INPUT_FILE_SUFFIX;
    }

    public abstract XmlInputBuilder<Solution_> createXmlInputBuilder();

    @Override
    public Solution_ readSolution(File inputFile) {
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile))) {
            XmlInputBuilder<Solution_> xmlInputBuilder = createXmlInputBuilder();
            xmlInputBuilder.setInputFile(inputFile);
            try {
                logger.info("Imported: {}", inputFile);
            } catch (IllegalArgumentException | IllegalStateException e) {
                throw new IllegalArgumentException("Exception in inputFile (" + inputFile + ")", e);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read the file (" + inputFile.getName() + ").", e);
        }
        return null;
    }

    public static abstract class XmlInputBuilder<Solution_> extends InputBuilder {

        protected File inputFile;
        protected Document document;

        public void setInputFile(File inputFile) {
            this.inputFile = inputFile;
        }

        public void setDocument(Document document) {
            this.document = document;
        }

        public abstract Solution_ readSolution() throws IOException, JDOMException;

        // ************************************************************************
        // Helper methods
        // ************************************************************************

        public String getInputId() {
            return FilenameUtils.getBaseName(inputFile.getPath());
        }


        protected void assertElementName(Element element, String name) {
            if (!element.getName().equals(name)) {
                throw new IllegalStateException("Element name (" + element.getName()
                        + ") should be " + name + ".");
            }
        }

    }

}
