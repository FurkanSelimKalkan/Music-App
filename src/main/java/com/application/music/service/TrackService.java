package com.application.music.service;

import com.application.music.model.Track;
import com.application.music.repository.TrackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    @Autowired
    TrackRepository trackRepository;

    @Transactional
    public Track create(Track track) {
        return trackRepository.save(track);
    }
}
