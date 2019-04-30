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

package com.example.optaplanner.generic.common.app;

import com.example.optaplanner.generic.domain.HillClimbingAlgorithm;
import com.example.optaplanner.generic.domain.LateAcceptanceAlgorithm;
import com.example.optaplanner.generic.domain.SimulatedAnnealingAlgorithm;
import com.example.optaplanner.generic.domain.TabuSearchAlgorithm;
import com.example.optaplanner.generic.dto.SolverConfigDTO;
import com.example.optaplanner.generic.enums.InitializingScoreTrend;
import com.example.optaplanner.generic.enums.ScoreCalculatorType;
import com.example.optaplanner.generic.enums.TerminationConfigParameter;
import com.example.optaplanner.generic.common.business.SolutionBusiness;
import com.example.optaplanner.generic.common.persistence.AbstractSolutionExporter;
import com.example.optaplanner.generic.common.persistence.AbstractSolutionImporter;
import com.example.optaplanner.generic.persistence.NurseRosteringImporter;
import com.example.optaplanner.generic.persistence.SolutionService;
import com.example.optaplanner.generic.persistence.SolverAndPersistence;
import com.example.optaplanner.generic.persistence.VehicleRoutingImporter;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.LocalSearchType;
import org.optaplanner.core.config.localsearch.decider.acceptor.AcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.acceptor.AcceptorType;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public abstract class CommonApp<Solution_> extends LoggingMain {

    /**
     * The path to the data directory, preferably with unix slashes for portability.
     * For example: -D{@value #DATA_DIR_SYSTEM_PROPERTY}=sources/data/
     */
    public static final String DATA_DIR_SYSTEM_PROPERTY = "solvers";
    //public static final String DATA_DIR_NAME = "vehiclerouting";
    public static final String DATA_DIR_NAME = "nurserostering";
    private static Map<Integer, String> xmlData = new HashMap<>();

    public static File determineDataDir(String dataDirName) {
        String dataDirPath = System.getProperty(DATA_DIR_SYSTEM_PROPERTY, "data/");
        File dataDir = new File(dataDirPath, dataDirName);
        if (!dataDir.exists()) {
            throw new IllegalStateException("The directory dataDir (" + dataDir.getAbsolutePath()
                    + ") does not exist.\n" +
                    " Either the working directory should be set to the directory that contains the data directory" +
                    " (which is not the data directory itself), or the system property "
                    + DATA_DIR_SYSTEM_PROPERTY + " should be set properly.\n" +
                    " The data directory is different in a git clone (optaplanner/optaplanner-solvers/data)" +
                    " and in a release zip (solvers/sources/data).\n" +
                    " In an IDE (IntelliJ, Eclipse, NetBeans), open the \"Run configuration\""
                    + " to change \"Working directory\" (or add the system property in \"VM options\").");
        }
        return dataDir;
    }

    /**
     * Some examples are not compatible with every native LookAndFeel.
     * For example, NurseRosteringPanel is incompatible with Mac.

    public static void prepareSwingEnvironment() {
        SwingUncaughtExceptionHandler.register();
        SwingUtils.fixateLookAndFeel();
    }
    */



    protected  String name = null;
    protected  String description = null;
    protected  String solverConfig = null;
    protected  String dataDirName = null;
    protected  Map constructionHeuristicType = new HashMap();
    protected  Map localSearchType = new HashMap();
    protected SolverConfigDTO solverConfigDTO = null;
    protected ConstructionHeuristicType[] constructionHeuristicTypes = ConstructionHeuristicType.values();
    protected LocalSearchType[] localSearchTypes = LocalSearchType.values();
    protected TerminationConfigParameter[] terminationConfigParameters = TerminationConfigParameter.values();
    protected InitializingScoreTrend[] initializingScoreTrends = InitializingScoreTrend.values();
    protected ScoreCalculatorType[] scoreCalculatorTypes = ScoreCalculatorType.values();
    protected SolverAndPersistence<Solution_> solverAndPersistence;
    protected SolutionBusiness<Solution_> solutionBusiness;

    protected CommonApp(String name, String description, String solverConfig, String dataDirName) {
        this.name = name;
        this.description = description;
        this.solverConfig = solverConfig;
        this.dataDirName = dataDirName;

        for(int i=1; i < constructionHeuristicTypes.length; i++){
            constructionHeuristicType.put(i, constructionHeuristicTypes[i]);
        }
        for(int j=1; j < localSearchTypes.length; j++){
            localSearchType.put(j, localSearchTypes[j]);
        }
    }

    public CommonApp() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSolverConfig() {
        return solverConfig;
    }

    public String getDataDirName() {
        return dataDirName;
    }

    private void displayProblemStatement(){
        File file = determineDataDir(DATA_DIR_NAME+"/unsolved");
        File[] listOfFiles = file.listFiles();
        for (int i = 1; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName  = listOfFiles[i].getName();
                xmlData.put(i,listOfFiles[i].getPath() );
                System.out.println(i+") "+fileName.substring(0,fileName.lastIndexOf(".")));


            }

        } //System.out.println(xmlData);
    }

    private void displayConstructionHeuristicType(){
        System.out.println("==========PROVIDE CONSTRUCTION HEURISTICS(CH) CONFIGURATION==========");
        System.out.println("==========CHOOSE CONSTRUCTION HEURISTICS(CH) ALGORITHM==========");
        showOptions(constructionHeuristicTypes);
    }

    private void displayLocalSearchType(){
        System.out.println("==========PROVIDE META HEURISTICS(MH) CONFIGURATION==========");
        System.out.println("==========CHOOSE META HEURISTICS(MH) ALGORITHM==========");
        showOptions(localSearchTypes);
    }

    private void showOptions(Object[] objects){
        for (int i = 0; i < objects.length; i++) {
            System.out.println(i+") "+objects[i]);
        }
    }

    private void displayTerminationConfigurationOptions(Scanner sc){
        System.out.println("==========ENTER TERMINATION CONFIGURATION==========");
        System.out.println("SELECT TERMINATION TIME OPTIONS :");
        showOptions(terminationConfigParameters);
        int terminationOptionSelected = sc.nextInt();
        TerminationConfigParameter terminationConfigParameter = terminationConfigParameters[terminationOptionSelected];
        solverConfigDTO.setTerminationConfigParameter(terminationConfigParameter);
        System.out.println("ENTER "+terminationConfigParameter+" value :");
    }

    private void displayScoreDirectorConfiguration(Scanner sc){
        System.out.println("==========ENTER SCORE CALCULATOR CONFIGURATION==========");
        System.out.println("SELECT SCORE TREND OPTIONS :");
        showOptions(initializingScoreTrends);
        int trendSelected = sc.nextInt();
        InitializingScoreTrend initializingScoreTrend = initializingScoreTrends[trendSelected];
        solverConfigDTO.setInitializingScoreTrend(initializingScoreTrend);
        System.out.println("SELECT CALCULATOR TYPE :");
        showOptions(scoreCalculatorTypes);
        int scoreCalculatorSelection = sc.nextInt();
        ScoreCalculatorType scoreCalculatorType = scoreCalculatorTypes[scoreCalculatorSelection];
        solverConfigDTO.setScoreCalculatorType(scoreCalculatorType);
       /* System.out.println("ENTER "+ scoreCalculatorType +" Class/File :");
        String calculatorFileName = sc.next();

        if(scoreCalculatorType == ScoreCalculatorType.DROOL_SCORE_CALCULATOR) {
            if(!calculatorFileName.contains(".drl") & calculatorFileName.indexOf("/") == -1){
                System.out.println("PLEASE provide correct drools file path");
                System.exit(0);
            }*//*else{
                if(!new File(calculatorFileName).exists()){
                    System.out.println("PLEASE provide correct drools file path");
                    System.exit(0);
                }
            }
            solverConfigDTO.setScoreCalculatorDroolFile(Arrays.asList(calculatorFileName));
        }else{
            solverConfigDTO.setScoreCalculatorClassName(calculatorFileName);
        }*/
    }

    public void configureLocalSearchTypeSelected(int localSearchType, Scanner sc){
        System.out.println("Please configure selected "+localSearchTypes[localSearchType]+" ::");
        switch (localSearchType){
            case 0 : solverConfigDTO.setLocalSearchAlgorithm(new HillClimbingAlgorithm(localSearchTypes[localSearchType]));

                break;
            case 1 :
                    System.out.println("Please enter entity tabu size ::");
                    int entityTabuSize = sc.nextInt();
                    System.out.println("Please enter value tabu size ::");
                    int valueTabuSize = sc.nextInt();
                    System.out.println("Please enter move tabu size ::");
                    int moveTabuSize = sc.nextInt();
                    System.out.println("Please enter undo move tabu size ::");
                    int undoMoveTabuSize = sc.nextInt();
                    solverConfigDTO.setLocalSearchAlgorithm(new TabuSearchAlgorithm(entityTabuSize,valueTabuSize, moveTabuSize, undoMoveTabuSize, localSearchTypes[localSearchType]));
                break;
            case 2 :
                int entityTabuSizeForSimlatedAnnealing=0;
                System.out.println("Please enter simulated Annealing Starting Temperature ::");
                String startingTemperature = sc.next();
                System.out.println("Do you want to apply entity tabu size ::");
                String isEntityTabuApplied = sc.next();
                if("YES".equalsIgnoreCase(isEntityTabuApplied)) {
                    System.out.println("Please enter entity tabu size ::");
                    entityTabuSizeForSimlatedAnnealing = sc.nextInt();
                }
                solverConfigDTO.setLocalSearchAlgorithm(new SimulatedAnnealingAlgorithm(startingTemperature, entityTabuSizeForSimlatedAnnealing,localSearchTypes[localSearchType]));
                break;
            case 3 : solverConfigDTO.setLocalSearchAlgorithm(new LateAcceptanceAlgorithm(localSearchTypes[localSearchType]));
                break;
            default: System.out.println("Continue processing without any local search algorithm");
        }
    }


    public void init() {
        init(null, true);
        solverConfigDTO = new SolverConfigDTO();
        Scanner sc = new Scanner(System.in);
        int selection;
        boolean exit = false;
        while (!exit) {
            try {
                System.out.println("==========ENTER SOLVER CONFIGURATION DATA==========");
                displayTerminationConfigurationOptions(sc);
                int terminationTimeValue = sc.nextInt();
                solverConfigDTO.setTerminationConfigParameterValue(terminationTimeValue);
                displayScoreDirectorConfiguration(sc);

                displayConstructionHeuristicType();
                int chSelection = sc.nextInt();
                solverConfigDTO.setConstructionHeuristicType((ConstructionHeuristicType)constructionHeuristicType.get(chSelection));
                displayLocalSearchType();
                int lsSelection = sc.nextInt();
                configureLocalSearchTypeSelected(lsSelection,sc);

                if(solverConfigDTO.getLocalSearchAlgorithm().getLocalSearchType() == LocalSearchType.LATE_ACCEPTANCE) {
                    System.out.println("Please enter late acceptance size:::");
                    int lateAcceptance = sc.nextInt();
                    solverConfigDTO.setLateAcceptance(lateAcceptance);
                }
                System.out.println("Please enter AcceptedCountLimit:::");
                int acceptedCountLimit = sc.nextInt();
                solverConfigDTO.setAcceptedCountLimit(acceptedCountLimit);

                solutionBusiness.setSolver(createSolver(solverConfigDTO));

                displayProblemStatement();
                selection = sc.nextInt();

                System.out.println("file selected is ::" + xmlData.get(selection));
                solverAndPersistence.openAndSolveProblem(new File(xmlData.get(selection)));
            }catch (InputMismatchException inputException){
                System.out.println("Please provide correct input");
                throw new IllegalArgumentException("Please provide correct input");
            }
        }
    }
   /* public void init(Component centerForComponent, boolean exitOnClose) {
        solutionBusiness = createSolutionBusiness();
        solverAndPersistence = new SolverAndPersistence<>(solutionBusiness, createSolutionPanel());
       // VehicleRoutingImporter.loadXmlData();
    }*/


      public void init(Component centerForComponent, boolean exitOnClose) {
      solutionBusiness = createSolutionBusiness();
      solverAndPersistence = new SolverAndPersistence<>(solutionBusiness, createSolutionPanel());
      NurseRosteringImporter.loadXmlData();

      }



    public SolutionBusiness<Solution_> createSolutionBusiness() {
        SolutionBusiness<Solution_> solutionBusiness = new SolutionBusiness<>(this);
        solutionBusiness.setDataDir(determineDataDir(dataDirName));
        solutionBusiness.setSolutionFileIO(createSolutionFileIO());
        solutionBusiness.setImporters(createSolutionImporters());
        solutionBusiness.setExporter(createSolutionExporter());
        solutionBusiness.updateDataDirs();
        return solutionBusiness;
    }

    protected Solver<Solution_> createSolver(SolverConfigDTO solverConfigDTO) {
        SolverFactory<Solution_> solverFactory = SolverFactory.createFromXmlResource("solvers/nurserostering/solver/nurseRosteringSolverConfig.xml");
        SolverConfig solverConfig = solverFactory.getSolverConfig();
        ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
        scoreDirectorFactoryConfig.setScoreDrlList(Arrays.asList(
                "solvers/nurserostering/solver/nurseRosteringScoreRules.drl"
                ));
        scoreDirectorFactoryConfig.setInitializingScoreTrend(solverConfigDTO.getInitializingScoreTrend().getValue());
        solverConfig.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);

        TerminationConfig terminationConfig = new TerminationConfig();
        terminationConfig.setMinutesSpentLimit(Long.valueOf(solverConfigDTO.getTerminationConfigParameterValue()));
        solverConfig.setTerminationConfig(terminationConfig);


        List<PhaseConfig> solverPhaseConfigList = new ArrayList<>();

        ConstructionHeuristicPhaseConfig constructionHeuristicSolverPhaseConfig = new ConstructionHeuristicPhaseConfig();
        constructionHeuristicSolverPhaseConfig.setConstructionHeuristicType(solverConfigDTO.getConstructionHeuristicType());
        solverPhaseConfigList.add(constructionHeuristicSolverPhaseConfig);

        LocalSearchPhaseConfig localSearchSolverPhaseConfig = new LocalSearchPhaseConfig();

        AcceptorConfig acceptorConfig = new AcceptorConfig();
        if(solverConfigDTO.getLocalSearchAlgorithm() != null) {
            switch (solverConfigDTO.getLocalSearchAlgorithm().getLocalSearchType()) {
                case TABU_SEARCH:
                    TabuSearchAlgorithm tabuSearchAlgorithm = (TabuSearchAlgorithm) solverConfigDTO.getLocalSearchAlgorithm();
                    if (tabuSearchAlgorithm.getEntityTabuSize() > 0) {
                        acceptorConfig.setEntityTabuSize(tabuSearchAlgorithm.getEntityTabuSize());
                    }
                    if (tabuSearchAlgorithm.getValueTabuSize() > 0) {
                        acceptorConfig.setValueTabuSize(tabuSearchAlgorithm.getValueTabuSize());
                    }
                    if (tabuSearchAlgorithm.getUndoMoveTabuSize() > 0) {
                        acceptorConfig.setUndoMoveTabuSize(tabuSearchAlgorithm.getUndoMoveTabuSize());
                    }
                    if (tabuSearchAlgorithm.getMoveTabuSize() > 0) {
                        acceptorConfig.setMoveTabuSize(tabuSearchAlgorithm.getMoveTabuSize());
                    }
                    break;

                case HILL_CLIMBING:
                    acceptorConfig.setAcceptorTypeList(Arrays.asList(AcceptorType.HILL_CLIMBING));
                    break;

                case SIMULATED_ANNEALING:
                    SimulatedAnnealingAlgorithm simulatedAnnealing = (SimulatedAnnealingAlgorithm) solverConfigDTO.getLocalSearchAlgorithm();
                    if (simulatedAnnealing.getEntityTabuSize() > 0) {
                        acceptorConfig.setEntityTabuSize(simulatedAnnealing.getEntityTabuSize());
                    }
                    if (simulatedAnnealing.getStartingTemperature() != null) {
                        acceptorConfig.setSimulatedAnnealingStartingTemperature(simulatedAnnealing.getStartingTemperature());
                    }
                    break;

                case LATE_ACCEPTANCE:
                    LateAcceptanceAlgorithm lateAcceptance = (LateAcceptanceAlgorithm) solverConfigDTO.getLocalSearchAlgorithm();
                    if (lateAcceptance.getEntityTabuSize() > 0) {
                        acceptorConfig.setEntityTabuSize(lateAcceptance.getEntityTabuSize());
                    }
                    acceptorConfig.setAcceptorTypeList(Collections.singletonList(AcceptorType.LATE_ACCEPTANCE));
                    if (lateAcceptance.getLateAcceptanceSize() > 0) {
                        acceptorConfig.setLateAcceptanceSize(lateAcceptance.getLateAcceptanceSize());
                    }
                    break;

                default:
            }
        }

        if(solverConfigDTO.getLateAcceptance() > 0) {
            acceptorConfig.setLateAcceptanceSize(solverConfigDTO.getLateAcceptance());
        }
        localSearchSolverPhaseConfig.setAcceptorConfig(acceptorConfig);

        if(solverConfigDTO.getAcceptedCountLimit() > 0) {
            LocalSearchForagerConfig localSearchForagerConfig = new LocalSearchForagerConfig();
            localSearchForagerConfig.setAcceptedCountLimit(solverConfigDTO.getAcceptedCountLimit());
            localSearchSolverPhaseConfig.setForagerConfig(localSearchForagerConfig);
        }

        solverPhaseConfigList.add(localSearchSolverPhaseConfig);

        solverConfig.setPhaseConfigList(solverPhaseConfigList);
        return solverFactory.buildSolver();
    }

    /**
     * Used for the unsolved and solved directories,
     * not for the import and output directories, in the data directory.
     * @return never null
     */
    public abstract SolutionFileIO<Solution_> createSolutionFileIO();

    protected abstract SolutionService<Solution_> createSolutionPanel();

    protected AbstractSolutionImporter[] createSolutionImporters() {
        return new AbstractSolutionImporter[]{};
    }

    protected AbstractSolutionExporter createSolutionExporter() {
        return null;
    }

}
