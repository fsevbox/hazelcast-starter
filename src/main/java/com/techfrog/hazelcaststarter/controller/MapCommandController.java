package com.techfrog.hazelcaststarter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/map")
public class MapCommandController {

    @Autowired()
    @Qualifier("map")
    private Map<String, String> map;

    @PostMapping("/put")
    public CommandResponse put(@RequestParam(value = "key") String key,
                               @RequestParam(value = "value") String value) {
        map.put(key, value);
        return new CommandResponse(value);
    }

    @GetMapping("/get")
    public CommandResponse get(@RequestParam(value = "key") String key) {
        String value = map.get(key);
        return new CommandResponse(value);
    }

    @GetMapping("/get-all")
    public List<String> get() {
        return map.entrySet().stream()
                .map((e) -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.toList());
    }
}
