package com.example.optaplanner.generic.persistence.util;

import com.example.optaplanner.generic.OptaplannerApplication;
import com.example.optaplanner.generic.common.app.CommonApp;
import com.example.optaplanner.generic.common.app.LoggingMain;
import com.example.optaplanner.generic.common.persistence.AbstractXlsxSolutionFileIO;
import com.example.optaplanner.generic.config.OptaplannerApplicationInit;
import com.example.optaplanner.generic.domain.Customer;
import com.example.optaplanner.generic.domain.Standstill;
import com.example.optaplanner.generic.domain.Vehicle;
import com.example.optaplanner.generic.domain.VehicleRoutingSolution;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.score.director.ScoreDirectorFactory;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VehicleRoutingDistanceTypeComparison extends LoggingMain {

    private final ScoreDirectorFactory<VehicleRoutingSolution> scoreDirectorFactory;

    public static void main(String[] args) {
        new VehicleRoutingDistanceTypeComparison().compare(
                "solved/tmp-p-belgium-n50-k10.xml",
                "solved/tmp-p-belgium-road-km-n50-k10.xml",
                "solved/tmp-p-belgium-road-time-n50-k10.xml");
    }

    protected final File dataDir;
    protected final AbstractXlsxSolutionFileIO<VehicleRoutingSolution> solutionFileIO;

    public VehicleRoutingDistanceTypeComparison() {
        dataDir = CommonApp.determineDataDir(OptaplannerApplicationInit.DATA_DIR_NAME);
        solutionFileIO = new AbstractXlsxSolutionFileIO<VehicleRoutingSolution>() {
            @Override
            public VehicleRoutingSolution read(File inputSolutionFile) {
                return null;
            }

            @Override
            public void write(VehicleRoutingSolution vehicleRoutingSolution, File outputSolutionFile) {

            }
        };
        SolverFactory<VehicleRoutingSolution> solverFactory = SolverFactory.createFromXmlResource(OptaplannerApplicationInit.SOLVER_CONFIG);
        scoreDirectorFactory = solverFactory.buildSolver().getScoreDirectorFactory();
    }

    public void compare(String... filePaths) {
        File[] files = new File[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            File file = new File(dataDir, filePaths[i]);
            if (!file.exists()) {
                throw new IllegalArgumentException("The file (" + file + ") does not exist.");
            }
            files[i] = file;
        }
        for (File varFile : files) {
            logger.info("  Results for {}:", varFile.getName());
            // Intentionally create a new instance instead of reusing the older one.
            VehicleRoutingSolution variablesSolution = (VehicleRoutingSolution) solutionFileIO.read(varFile);
            for (File inputFile : files) {
                HardSoftLongScore score;
                if (inputFile == varFile) {
                    score = variablesSolution.getScore();
                } else {
                    VehicleRoutingSolution inputSolution = (VehicleRoutingSolution) solutionFileIO.read(inputFile);
                    applyVariables(inputSolution, variablesSolution);
                    score = inputSolution.getScore();
                }
                logger.info("    {} (according to {})", score.getSoftScore(), inputFile.getName());
            }
        }
    }

    private void applyVariables(VehicleRoutingSolution inputSolution, VehicleRoutingSolution varSolution) {
        List<Vehicle> inputVehicleList = inputSolution.getVehicleList();
        Map<Long, Vehicle> inputVehicleMap = new LinkedHashMap<>(inputVehicleList.size());
        for (Vehicle vehicle : inputVehicleList) {
            inputVehicleMap.put(vehicle.getId(), vehicle);
        }
        List<Customer> inputCustomerList = inputSolution.getCustomerList();
        Map<Long, Customer> inputCustomerMap = new LinkedHashMap<>(inputCustomerList.size());
        for (Customer customer : inputCustomerList) {
            inputCustomerMap.put(customer.getId(), customer);
        }

        for (Vehicle varVehicle : varSolution.getVehicleList()) {
            Vehicle inputVehicle = inputVehicleMap.get(varVehicle.getId());
            Customer varNext = varVehicle.getNextCustomer();
            inputVehicle.setNextCustomer(varNext == null ? null : inputCustomerMap.get(varNext.getId()));
        }
        for (Customer varCustomer : varSolution.getCustomerList()) {
            Customer inputCustomer = inputCustomerMap.get(varCustomer.getId());
            Standstill varPrevious = varCustomer.getPreviousStandstill();
            inputCustomer.setPreviousStandstill(varPrevious == null ? null :
                    varPrevious instanceof Vehicle ? inputVehicleMap.get(((Vehicle) varPrevious).getId())
                    : inputCustomerMap.get(((Customer) varPrevious).getId()));
            Customer varNext = varCustomer.getNextCustomer();
            inputCustomer.setNextCustomer(varNext == null ? null : inputCustomerMap.get(varNext.getId()));
        }
        try (ScoreDirector<VehicleRoutingSolution> scoreDirector = scoreDirectorFactory.buildScoreDirector()) {
            scoreDirector.setWorkingSolution(inputSolution);
            scoreDirector.calculateScore();
        }
    }

}
