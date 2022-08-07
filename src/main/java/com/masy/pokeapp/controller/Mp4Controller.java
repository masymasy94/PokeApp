package com.masy.pokeapp.controller;

import com.masy.pokeapp.service.Mp4Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Mp4Controller {

    @Autowired
    Mp4Service service;

    @GetMapping("/srt/merge")
    public boolean mergeSubsIntoMp4() {
        service.mergeSrtToMp4ForFolders();
        return true;
    }
}
