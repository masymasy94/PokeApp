package com.masy.pokeapp.controller;

import com.masy.pokeapp.service.ExcellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcellController {

    @Autowired
    ExcellService service;

    @PatchMapping("/excell/read")
    public boolean deleteAllSrt() {
        service.readFile();
        return true;
    }

}

