package com.example.optaplanner.generic;

import com.example.optaplanner.generic.domain.CourseSchedule;
import com.example.optaplanner.generic.domain.Lecture;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OptaplannerApplicationTests {

	static CourseSchedule unsolvedCourseSchedule;

	@BeforeClass
	public static void setUp() {

		unsolvedCourseSchedule = new CourseSchedule();

		for(int i = 0; i < 10; i++){
			unsolvedCourseSchedule.getLectureList().add(new Lecture());
		}

		unsolvedCourseSchedule.getPeriodList().addAll(Arrays.asList(new Integer[] { 1, 2, 3 }));
		unsolvedCourseSchedule.getRoomList().addAll(Arrays.asList(new Integer[] { 1, 2 }));
	}

	@Test
	public void test_whenCustomJavaSolver() {

		SolverFactory<CourseSchedule> solverFactory = SolverFactory.createFromXmlResource("courseScheduleSolverConfiguration.xml");
		Solver<CourseSchedule> solver = solverFactory.buildSolver();
		CourseSchedule solvedCourseSchedule = solver.solve(unsolvedCourseSchedule);

		Assert.assertNotNull(solvedCourseSchedule.getScore());
		//Assert.assertEquals(-4, solvedCourseSchedule.getScore().getHardScore());
		solvedCourseSchedule.printCourseSchedule();
	}

	@Test
	public void test_whenDroolsSolver() {

		SolverFactory<CourseSchedule> solverFactory = SolverFactory.createFromXmlResource("courseScheduleSolverConfigDrools.xml");
		Solver<CourseSchedule> solver = solverFactory.buildSolver();
		CourseSchedule solvedCourseSchedule = solver.solve(unsolvedCourseSchedule);

		Assert.assertNotNull(solvedCourseSchedule.getScore());
		Assert.assertEquals(0, solvedCourseSchedule.getScore().getHardScore());
		solvedCourseSchedule.printCourseSchedule();

	}

}

