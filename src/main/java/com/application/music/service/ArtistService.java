package com.application.music.service;

import com.application.music.model.Artist;
import com.application.music.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        return artistRepository.save(artist);
    }

}
