package com.example.optaplanner.generic.config;

import com.example.optaplanner.generic.common.app.CommonApp;
import com.example.optaplanner.generic.common.persistence.AbstractSolutionImporter;
import com.example.optaplanner.generic.domain.VehicleRoutingSolution;
import com.example.optaplanner.generic.persistence.SolutionServiceImpl;
import com.example.optaplanner.generic.persistence.VehicleRoutingImporter;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

public class OptaplannerApplicationInit extends CommonApp<VehicleRoutingSolution> {


    public static final String DATA_DIR_NAME = "vehiclerouting";
    public static final String SOLVER_CONFIG
            = "solvers/vehiclerouting/solver/vehicleRoutingSolverConfig.xml";


    public OptaplannerApplicationInit() {
        super("Vehicle routing",
                "Official competition name: Capacitated vehicle routing problem (CVRP), " +
                        "optionally with time windows (CVRPTW)\n\n" +
                        "Pick up all items of all customers with a few vehicles.\n\n" +
                        "Find the shortest route possible.\n" +
                        "Do not overload the capacity of the vehicles.\n" +
                        "Arrive within the time window of each customer.",
                SOLVER_CONFIG, DATA_DIR_NAME);
    }

    @Override
    protected AbstractSolutionImporter[] createSolutionImporters() {
        return new AbstractSolutionImporter[]{
                new VehicleRoutingImporter()
        };

    }

    @Override
    protected SolutionServiceImpl createSolutionPanel() {
        return new SolutionServiceImpl();
    }

    @Override
    public SolutionFileIO<VehicleRoutingSolution> createSolutionFileIO() {
        return new XStreamSolutionFileIO<>(VehicleRoutingSolution.class);
    }
}

