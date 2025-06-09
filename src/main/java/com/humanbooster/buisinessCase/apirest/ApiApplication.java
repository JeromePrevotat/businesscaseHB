package com.humanbooster.buisinessCase.apirest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class ApiApplication extends ResourceConfig{
    public ApiApplication(){
        packages("com.humanbooster.buisinessCase",
            "com.humanbooster.buisinessCase.apirest",
            "com.humanbooster.buisinessCase.model",
            "com.humanbooster.buisinessCase.repository",
            "com.humanbooster.buisinessCase.service",
            "com.humanbooster.buisinessCase.controller",
            "com.humanbooster.buisinessCase.dto",
            "com.humanbooster.buisinessCase.mapper",
            "com.humanbooster.buisinessCase.utils");
        register(JacksonFeature.class);
    }
}