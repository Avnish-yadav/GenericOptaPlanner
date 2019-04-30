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

import com.example.optaplanner.generic.common.app.LoggingMain;
import org.optaplanner.core.api.domain.solution.PlanningSolution;

import java.io.File;

/**
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public abstract class AbstractSolutionExporter<Solution_> extends LoggingMain {

    public abstract String getOutputFileSuffix();

    public abstract void writeSolution(Solution_ solution, File outputFile);

    public static abstract class OutputBuilder extends LoggingMain {

    }

}