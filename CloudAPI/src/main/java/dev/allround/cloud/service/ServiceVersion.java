package dev.allround.cloud.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@Getter
@RequiredArgsConstructor
public enum ServiceVersion {
    SPIGOT_1_18_1("SERVER",""),
    SPIGOT_1_18_2("SERVER",""),
    SPIGOT_1_19_1("SERVER",""),
    SPIGOT_1_19_2("SERVER",""),
    WATERFALL_LATEST("PROXY","")
    ;
    private final String type;
    private final String url;


    public void download(Path path){

    }
}
