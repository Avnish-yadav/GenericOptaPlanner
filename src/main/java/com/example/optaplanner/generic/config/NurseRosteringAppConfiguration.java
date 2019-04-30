package com.example.optaplanner.generic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NurseRosteringAppConfiguration {

    @Bean
    NurseRosteringApp nurseRosteringApp(){

        NurseRosteringApp nurseRosteringApp=new NurseRosteringApp();

        return nurseRosteringApp;
    }
}
