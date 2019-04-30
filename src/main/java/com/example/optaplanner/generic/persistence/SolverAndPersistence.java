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

package com.example.optaplanner.generic.persistence;

import com.example.optaplanner.generic.common.business.SolutionBusiness;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public class SolverAndPersistence<Solution_>  {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());



    private final SolutionBusiness<Solution_> solutionBusiness;


    private SolutionService<Solution_> solutionService;


    public SolverAndPersistence(SolutionBusiness<Solution_> solutionBusiness, SolutionService<Solution_> solutionPanel) {
        this.solutionBusiness = solutionBusiness;
        this.solutionService = solutionPanel;
        solutionPanel.setSolutionBusiness(solutionBusiness);
        solutionPanel.setSolverAndPersistence(this);
        //constraintMatchesDialog = new ConstraintMatchesDialog(this, solutionBusiness);
    }

    private void registerListeners() {
        solutionBusiness.registerForBestSolutionChanges(this);
    }

    public void openAndSolveProblem(File file){
        //ssssregisterListeners();
        solutionBusiness.openSolution(file);
        Solution_ problem = solutionBusiness.getSolution();
        new SolveWorker(problem).execute();
    }

    protected class SolveWorker extends SwingWorker<Solution_, Void> {

        protected final Solution_ problem;

        public SolveWorker(Solution_ problem) {
            this.problem = problem;
        }

        @Override
        protected Solution_ doInBackground() throws Exception {
            return solutionBusiness.solve(problem);
        }

        @Override
        protected void done() {
            try {
                Solution_ bestSolution = get();
                System.out.println("DATAA===="+bestSolution.toString());
                solutionService.resetPanel(bestSolution);
                solutionBusiness.setSolution(bestSolution);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Solving was interrupted.", e);
            } catch (ExecutionException e) {
                throw new IllegalStateException("Solving failed.", e.getCause());
            } finally {

            }
        }

    }



    private class TerminateSolvingEarlyAction{

        /*@Override
        public void actionPerformed(ActionEvent e) {
            terminateSolvingEarlyAction.setEnabled(false);
            progressBar.setString("Terminating...");
            // This async, so it doesn't stop the solving immediately
            solutionBusiness.terminateSolvingEarly();
        }*/

    }

    public void bestSolutionChanged() {
        Solution_ solution = solutionBusiness.getSolution();
        Score score = solutionBusiness.getScore();
        logger.info("SCORE++++"+score.toString());
    }



    }
