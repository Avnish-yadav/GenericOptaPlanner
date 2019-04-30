/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
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

package com.example.optaplanner.generic.persistence;

import com.example.optaplanner.generic.common.persistence.AbstractXlsxSolutionFileIO;
import com.example.optaplanner.generic.domain.VehicleRoutingSolution;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.File;

public class VehicleRoutingFileIO implements SolutionFileIO<VehicleRoutingSolution> {

    private VehicleRoutingImporter importer = new VehicleRoutingImporter();

    @Override
    public String getInputFileExtension() {
        return "vrp";
    }

    @Override
    public VehicleRoutingSolution read(File inputSolutionFile) {
        return importer.readSolution(inputSolutionFile);
    }

    @Override
    public void write(VehicleRoutingSolution solution, File outputSolutionFile) {
        throw new UnsupportedOperationException();
    }

}