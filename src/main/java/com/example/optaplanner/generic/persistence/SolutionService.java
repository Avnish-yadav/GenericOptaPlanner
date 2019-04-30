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
import org.optaplanner.core.impl.solver.ProblemFactChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;


/**
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public abstract class SolutionService<Solution_> {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    protected SolverAndPersistence solverAndPersistence;
    protected SolutionBusiness<Solution_> solutionBusiness;

    protected boolean useIndictmentColor = false;
    protected double[] indictmentMinimumLevelNumbers;

    public SolverAndPersistence getSolverAndPersistence() {
        return solverAndPersistence;
    }

    public void setSolverAndPersistence(SolverAndPersistence solverAndPersistence) {
        this.solverAndPersistence = solverAndPersistence;
    }

    public SolutionBusiness<Solution_> getSolutionBusiness() {
        return solutionBusiness;
    }

    public void setSolutionBusiness(SolutionBusiness<Solution_> solutionBusiness) {
        this.solutionBusiness = solutionBusiness;
    }

    public boolean isUseIndictmentColor() {
        return useIndictmentColor;
    }

    public void setUseIndictmentColor(boolean useIndictmentColor) {
        this.useIndictmentColor = useIndictmentColor;
    }

    public String getUsageExplanationPath() {
        return null;
    }

    public boolean isWrapInScrollPane() {
        return true;
    }

    public abstract void resetPanel(Solution_ solution);

    public void updatePanel(Solution_ solution) {
        resetPanel(solution);
    }

    public Solution_ getSolution() {
        return (Solution_) solutionBusiness.getSolution();
    }



   /* public void refreshScoreField(Score score) {
        scoreField.setForeground(determineScoreFieldForeground(score));
        scoreField.setText("Latest best score: " + score);
    }*/

    public void doProblemFactChange(ProblemFactChange<Solution_> problemFactChange) {
        doProblemFactChange(problemFactChange, false);
    }




    public void doProblemFactChange(ProblemFactChange<Solution_> problemFactChange, boolean reset) {
        solutionBusiness.doProblemFactChange(problemFactChange);
        Solution_ solution = getSolution();
        Score score = solutionBusiness.getScore();
        if (reset) {
            resetPanel(solution);
        } else {
            updatePanel(solution);
        }
       // validate();
      //  solverAndPersistenceFrame.refreshScoreField(score);
    }


    public void resetScreen() {
        Solution_ solution = solutionBusiness.getSolution();
        Score score = solutionBusiness.getScore();
        resetPanel(solution);
       // validate();
        //refreshScoreField(score);
    }





}
