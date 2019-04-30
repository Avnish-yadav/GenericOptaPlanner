package com.example.optaplanner.generic.common.persistence;

import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.score.constraint.Indictment;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public abstract class AbstractXlsxSolutionFileIO<Solution_> implements SolutionFileIO<Solution_> {

    protected static final Pattern VALID_TAG_PATTERN = Pattern.compile("(?U)^[\\w&\\-\\.\\/\\(\\)\\'][\\w&\\-\\.\\/\\(\\)\\' ]*[\\w&\\-\\.\\/\\(\\)\\']?$");
    protected static final Pattern VALID_NAME_PATTERN = AbstractXlsxSolutionFileIO.VALID_TAG_PATTERN;
    protected static final Pattern VALID_CODE_PATTERN = Pattern.compile("(?U)^[\\w\\-\\.\\/\\(\\)]+$");

    public static final DateTimeFormatter DAY_FORMATTER
            = DateTimeFormatter.ofPattern("E yyyy-MM-dd", Locale.ENGLISH);
    public static final DateTimeFormatter MONTH_FORMATTER
            = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
    public static final DateTimeFormatter TIME_FORMATTER
            = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    public static final DateTimeFormatter DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);



    @Override
    public String getInputFileExtension() {
        return "xlsx";
    }

    public static abstract class AbstractXlsxReader<Solution_> {


        protected Solution_ solution;


        protected int currentRowNumber;
        protected int currentColumnNumber;

        public AbstractXlsxReader() {

        }

        public abstract Solution_ read();

        public static abstract class AbstractXlsxWriter<Solution_> {

            protected final Solution_ solution;
            protected final List<ConstraintMatchTotal> constraintMatchTotalList;
            protected final Map<Object, Indictment> indictmentMap;


            protected int currentRowNumber;
            protected int currentColumnNumber;
            protected int headerCellCount;

            public AbstractXlsxWriter(Solution_ solution, String solverConfigResource) {
                this.solution = solution;
                ScoreDirectorFactory<Solution_> scoreDirectorFactory
                        = SolverFactory.<Solution_>createFromXmlResource(solverConfigResource)
                        .buildSolver().getScoreDirectorFactory();
                try (ScoreDirector<Solution_> scoreDirector = scoreDirectorFactory.buildScoreDirector()) {
                    scoreDirector.setWorkingSolution(solution);
                    scoreDirector.calculateScore();
                    constraintMatchTotalList = new ArrayList<>(scoreDirector.getConstraintMatchTotals());
                    constraintMatchTotalList.sort(Comparator.comparing(ConstraintMatchTotal::getScore));
                    indictmentMap = scoreDirector.getIndictmentMap();
                }
            }

        }


    }
}
