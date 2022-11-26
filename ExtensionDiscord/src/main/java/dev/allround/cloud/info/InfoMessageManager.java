package dev.allround.cloud.info;

public class InfoMessageManager {
    //TODO: lädt daten aus config -> welche packet typen eine info nachricht triggern sollen und in welchen channels
    //TODO: Yaml dependencies adden
    /*
    Aufbau InfoMsg config.yml:

    infoMsgs:
        PACKETTYPE:
            Channel:
                - 0123456789
                - 1234567890
            Message:
                Author: Allround
                ColorHex: xFF0000
                Lines:
                    - "Ein Packet wurde gesendet. PACKETTYPE"
                    - "Line 2"
                    - "Erstes parameter des Packets falls vorhanden: %VAR1%"
     */
}
