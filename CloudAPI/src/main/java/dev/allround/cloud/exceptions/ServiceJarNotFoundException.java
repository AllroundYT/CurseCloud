package dev.allround.cloud.exceptions;

import dev.allround.cloud.service.ServiceType;
import dev.allround.cloud.servicegroup.IServiceGroup;

import java.nio.file.Path;

public class ServiceJarNotFoundException extends Exception{
    public ServiceJarNotFoundException(IServiceGroup iServiceGroup) {
        super("Could not find "+(iServiceGroup.getType() == ServiceType.PROXY ? "proxy" : "server")+".jar in "+ Path.of("templates",iServiceGroup.getGroupName()).toAbsolutePath());
    }
}
