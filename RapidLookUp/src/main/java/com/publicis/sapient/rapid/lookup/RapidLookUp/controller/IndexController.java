package com.publicis.sapient.rapid.lookup.RapidLookUp.controller;

import com.publicis.sapient.rapid.lookup.RapidLookUp.service.MassIndexerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/index")
public class IndexController {

    private final MassIndexerConfig massIndexerConfig;

    @Autowired
    public IndexController(MassIndexerConfig massIndexerConfig) {
        this.massIndexerConfig = massIndexerConfig;
    }

    @PostMapping("/reindex")
    public ResponseEntity<String> reindexData() {
        massIndexerConfig.rebuildIndex();
        return ResponseEntity.ok("Reindexing started successfully!");
    }

}
