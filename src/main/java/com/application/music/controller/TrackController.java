package com.application.music.controller;

import com.application.music.model.Track;
import com.application.music.repository.TrackRepository;
import com.application.music.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tracks")
public class TrackController {

    @Autowired
    TrackService trackService;

    @PostMapping
    public ResponseEntity<Track> create(@RequestBody Track track) {
        return ResponseEntity.ok(trackService.create(track));
    }
}
