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

package com.example.optaplanner.generic.swingui;

import com.example.optaplanner.generic.domain.*;
import com.example.optaplanner.generic.persistence.SolutionService;
//import org.optaplanner.examples.common.swingui.SolutionPanel;
//import org.optaplanner.examples.nurserostering.domain.*;

import java.util.List;
import java.util.*;

public class NurseRosteringPanel extends SolutionService<NurseRoster> {

   // public static final String LOGO_PATH = "/org/optaplanner/examples/nurserostering/swingui/nurseRosteringLogo.png";

    //private final ImageIcon employeeIcon;
    //private final ImageIcon deleteEmployeeIcon;

    //private JPanel employeeListPanel;

   // private JTextField planningWindowStartField;
    //private AbstractAction advancePlanningWindowStartAction;
    private EmployeePanel unassignedPanel;
    private Map<Employee, EmployeePanel> employeeToPanelMap;

    public NurseRosteringPanel() {

        createEmployeeListPanel();

    }





    private void createEmployeeListPanel() {


        employeeToPanelMap = new LinkedHashMap<>();
        employeeToPanelMap.put(null, unassignedPanel);
    }

    @Override
    public void resetPanel(NurseRoster nurseRoster) {
        for (EmployeePanel employeePanel : employeeToPanelMap.values()) {
            if (employeePanel.getEmployee() != null) {
                //remove(employeePanel);
            }
        }
        employeeToPanelMap.clear();
        employeeToPanelMap.put(null, unassignedPanel);
        unassignedPanel.clearShiftAssignments();

        List<ShiftDate> shiftDateList = nurseRoster.getShiftDateList();
        List<Shift> shiftList = nurseRoster.getShiftList();
        unassignedPanel.setShiftDateListAndShiftList(shiftDateList, shiftList);
        updatePanel(nurseRoster);

    }

    @Override
    public void updatePanel(NurseRoster nurseRoster) {

        List<ShiftDate> shiftDateList = nurseRoster.getShiftDateList();
        List<Shift> shiftList = nurseRoster.getShiftList();
        Set<Employee> deadEmployeeSet = new LinkedHashSet<>(employeeToPanelMap.keySet());
        deadEmployeeSet.remove(null);
        for (Employee employee : nurseRoster.getEmployeeList()) {
            deadEmployeeSet.remove(employee);
            EmployeePanel employeePanel = employeeToPanelMap.get(employee);
            if (employeePanel == null) {
                employeePanel = new EmployeePanel(this, shiftDateList, shiftList, employee);

                employeeToPanelMap.put(employee, employeePanel);
            }
            employeePanel.clearShiftAssignments();
        }
        unassignedPanel.clearShiftAssignments();
        for (ShiftAssignment shiftAssignment : nurseRoster.getShiftAssignmentList()) {
            Employee employee = shiftAssignment.getEmployee();
            EmployeePanel employeePanel = employeeToPanelMap.get(employee);
            employeePanel.addShiftAssignment(shiftAssignment);
        }
        for (Employee deadEmployee : deadEmployeeSet) {
            EmployeePanel deadEmployeePanel = employeeToPanelMap.remove(deadEmployee);

        }
        for (EmployeePanel employeePanel : employeeToPanelMap.values()) {
            //employeePanel.update();
        }
    }


    private boolean isIndictmentHeatMapEnabled() {
        return true;
    }

    private void advancePlanningWindowStart() {
        logger.info("Advancing planningWindowStart.");

        doProblemFactChange(scoreDirector -> {
            NurseRoster nurseRoster = scoreDirector.getWorkingSolution();
            NurseRosterParametrization nurseRosterParametrization = nurseRoster.getNurseRosterParametrization();
            List<ShiftDate> shiftDateList = nurseRoster.getShiftDateList();
            ShiftDate planningWindowStart = nurseRosterParametrization.getPlanningWindowStart();
            int windowStartIndex = shiftDateList.indexOf(planningWindowStart);
            if (windowStartIndex < 0) {
                throw new IllegalStateException("The planningWindowStart ("
                        + planningWindowStart + ") must be in the shiftDateList ("
                        + shiftDateList + ").");
            }
            ShiftDate oldLastShiftDate = shiftDateList.get(shiftDateList.size() - 1);
            ShiftDate newShiftDate = new ShiftDate();
            newShiftDate.setId(oldLastShiftDate.getId() + 1L);
            newShiftDate.setDayIndex(oldLastShiftDate.getDayIndex() + 1);
            newShiftDate.setDate(oldLastShiftDate.getDate().plusDays(1));
            List<Shift> refShiftList = planningWindowStart.getShiftList();
            List<Shift> newShiftList = new ArrayList<>(refShiftList.size());
            newShiftDate.setShiftList(newShiftList);
            nurseRoster.getShiftDateList().add(newShiftDate);
            scoreDirector.afterProblemFactAdded(newShiftDate);
            Shift oldLastShift = nurseRoster.getShiftList().get(nurseRoster.getShiftList().size() - 1);
            long shiftId = oldLastShift.getId() + 1L;
            int shiftIndex = oldLastShift.getIndex() + 1;
            long shiftAssignmentId = nurseRoster.getShiftAssignmentList().get(
                    nurseRoster.getShiftAssignmentList().size() - 1).getId() + 1L;
            for (Shift refShift : refShiftList) {
                Shift newShift = new Shift();
                newShift.setId(shiftId);
                shiftId++;
                newShift.setShiftDate(newShiftDate);
                newShift.setShiftType(refShift.getShiftType());
                newShift.setIndex(shiftIndex);
                shiftIndex++;
                newShift.setRequiredEmployeeSize(refShift.getRequiredEmployeeSize());
                newShiftList.add(newShift);
                nurseRoster.getShiftList().add(newShift);
                scoreDirector.afterProblemFactAdded(newShift);
                for (int indexInShift = 0; indexInShift < newShift.getRequiredEmployeeSize(); indexInShift++) {
                    ShiftAssignment newShiftAssignment = new ShiftAssignment();
                    newShiftAssignment.setId(shiftAssignmentId);
                    shiftAssignmentId++;
                    newShiftAssignment.setShift(newShift);
                    newShiftAssignment.setIndexInShift(indexInShift);
                    nurseRoster.getShiftAssignmentList().add(newShiftAssignment);
                    scoreDirector.afterEntityAdded(newShiftAssignment);
                }
            }
            windowStartIndex++;
            ShiftDate newPlanningWindowStart = shiftDateList.get(windowStartIndex);
            scoreDirector.beforeProblemPropertyChanged(nurseRosterParametrization);
            nurseRosterParametrization.setPlanningWindowStart(newPlanningWindowStart);
            nurseRosterParametrization.setLastShiftDate(newShiftDate);
            scoreDirector.afterProblemPropertyChanged(nurseRosterParametrization);
        }, true);
    }

    public void deleteEmployee(final Employee employee) {
        logger.info("Scheduling delete of employee ({}).", employee);
        doProblemFactChange(scoreDirector -> {
            NurseRoster nurseRoster = scoreDirector.getWorkingSolution();
            Employee workingEmployee = scoreDirector.lookUpWorkingObject(employee);
            if (workingEmployee == null) {
                // The employee has already been deleted (the UI asked to changed the same employee twice), so do nothing
                return;
            }
            // First remove the problem fact from all planning entities that use it
            for (ShiftAssignment shiftAssignment : nurseRoster.getShiftAssignmentList()) {
                if (shiftAssignment.getEmployee() == workingEmployee) {
                    scoreDirector.beforeVariableChanged(shiftAssignment, "employee");
                    shiftAssignment.setEmployee(null);
                    scoreDirector.afterVariableChanged(shiftAssignment, "employee");
                }
            }
            // A SolutionCloner does not clone problem fact lists (such as employeeList)
            // Shallow clone the employeeList so only workingSolution is affected, not bestSolution or guiSolution
            ArrayList<Employee> employeeList = new ArrayList<>(nurseRoster.getEmployeeList());
            nurseRoster.setEmployeeList(employeeList);
            // Remove it the problem fact itself
            scoreDirector.beforeProblemFactRemoved(workingEmployee);
            employeeList.remove(workingEmployee);
            scoreDirector.afterProblemFactRemoved(workingEmployee);
            scoreDirector.triggerVariableListeners();
        });
    }

    public void moveShiftAssignmentToEmployee(ShiftAssignment shiftAssignment, Employee toEmployee) {
        solutionBusiness.doChangeMove(shiftAssignment, "employee", toEmployee);
        //solverAndPersistenceFrame.resetScreen();
    }



    /*public void remove(Component comp) {
        synchronized (getTreeLock()) {
            if (comp.parent == this)  {
                int index = component.indexOf(comp);
                if (index >= 0) {
                    remove(index);
                }
            }
        }*/
    }





