package com.example.optaplanner.generic.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@PlanningSolution
public class CourseSchedule {
    Logger logger = LoggerFactory.getLogger("CourseSchedule");

    private List<Integer> roomList= new ArrayList<>();
    private List<Integer> periodList= new ArrayList<>();
    private List<Lecture> lectureList= new ArrayList<>();
    private HardSoftScore score;


    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "roomAvailable")
    public List<Integer> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Integer> roomList) {
        this.roomList = roomList;
    }


    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "periodAvailable")
    public List<Integer> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Integer> periodList) {
        this.periodList = periodList;
    }

    @PlanningEntityCollectionProperty
    public List<Lecture> getLectureList() {
        return lectureList;
    }

    public void setLectureList(List<Lecture> lectureList) {
        this.lectureList = lectureList;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public void printCourseSchedule() {
        lectureList.stream()
                .map(c -> "Lecture in Room " + c.getRoomNumber().toString() + " during Period " + c.getPeriod().toString())
                .forEach(k -> logger.info(k));
    }
}
