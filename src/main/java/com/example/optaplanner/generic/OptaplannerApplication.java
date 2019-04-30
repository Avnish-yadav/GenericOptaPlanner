package com.example.optaplanner.generic;

import com.example.optaplanner.generic.common.app.CommonApp;
import com.example.optaplanner.generic.common.persistence.AbstractSolutionImporter;
import com.example.optaplanner.generic.domain.VehicleRoutingSolution;
import com.example.optaplanner.generic.persistence.VehicleRoutingImporter;
import com.example.optaplanner.generic.persistence.SolutionServiceImpl;
import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OptaplannerApplication  {


	public static void main(String[] args) {
		SpringApplication.run(OptaplannerApplication.class, args);

	}


}

