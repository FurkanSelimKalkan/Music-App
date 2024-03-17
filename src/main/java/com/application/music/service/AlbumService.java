package com.application.music.service;

import com.application.music.dto.AlbumDTO;
import com.application.music.exception.AlbumDeletionException;
import com.application.music.model.Album;
import com.application.music.model.Artist;
import com.application.music.model.Track;
import com.application.music.repository.AlbumRepository;
import com.application.music.repository.ArtistRepository;
import com.application.music.repository.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private final AlbumRepository albumRepository;

    @Autowired
    private final ArtistRepository artistRepository;

    @Autowired
    private  final TrackRepository trackRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository, TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.trackRepository = trackRepository;
    }

    public List<Album> getALlAlbums() {
        return albumRepository.findAll();
    }

    @Transactional
    public Album create(AlbumDTO albumDTO) {
        Album album = new Album();
        album.setName(albumDTO.getName());

        //Optionale Attribute
        albumDTO.getReleaseDate().ifPresent(album::setReleaseDate);
        albumDTO.getRating().ifPresent(album::setRating);
        albumDTO.getRatingCount().ifPresent(album::setRatingCount);

        albumDTO.getArtistIds().ifPresent(artistIds -> {
            List<Artist> artists = artistRepository.findAllById(artistIds);
            artists.forEach(album::addArtist);
        });

        albumDTO.getTracksIds().ifPresent(tracksIds -> {
            List<Track> tracks = trackRepository.findAllById(tracksIds);
            tracks.forEach(album::addTrack);
        });

        return albumRepository.save(album);
    }

    @Transactional
    public Album addRating(Long albumId, double newRating) {
        Album updatedAlbum = albumRepository.getReferenceById(albumId);
        updatedAlbum.addRating(newRating);
        return albumRepository.save(updatedAlbum);
    }

    @Transactional
    public boolean delete(Long albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));

        if (canDeleteAlbum(album)) {
            albumRepository.deleteById(albumId);
            return true;
        } else {
            throw new AlbumDeletionException("Album with id: " + albumId + " cannot be deleted due to business rules.");
        }
    }

    public Album findById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id " + id));
    }

    public boolean canDeleteAlbum(Album album) {
        return !(album.getRating() > 4 && album.getRatingCount() >= 10);
    }

}
