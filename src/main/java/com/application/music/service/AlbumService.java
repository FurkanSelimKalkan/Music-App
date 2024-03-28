package com.application.music.service;

import com.application.music.dto.AlbumDTO;
import com.application.music.exception.AlbumDeletionException;
import com.application.music.model.Album;
import com.application.music.model.Artist;
import com.application.music.model.BaseEntity;
import com.application.music.model.Track;
import com.application.music.repository.AlbumRepository;
import com.application.music.repository.ArtistRepository;
import com.application.music.repository.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private  final TrackRepository trackRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository, TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.trackRepository = trackRepository;
    }

    @Transactional
    public List<AlbumDTO> getAllAlbums() {
        List<Album> albums = albumRepository.findAll();
        List<AlbumDTO> albumsDTO = new ArrayList<>();

        for(Album album: albums) {
            AlbumDTO albumDTO = convertToDTO(album);
            albumsDTO.add(albumDTO);
        }
        return albumsDTO;
    }

    @Transactional
    public AlbumDTO findById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id " + id));
        return convertToDTO(album);
    }

    @Transactional
    public AlbumDTO create(AlbumDTO albumDTO) {
        Album album = mapDtoToAlbum(albumDTO);
        Album savedAlbum = albumRepository.save(album);

        handleTracks(albumDTO.getTracksIds(), savedAlbum);
        handleArtists(albumDTO.getArtistIds(), savedAlbum);

        return convertToDTO(savedAlbum);
    }

    @Transactional
    public AlbumDTO update(Long id, AlbumDTO albumDTO) {
        Album album = albumRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        album = updateAlbumFromDTO(albumDTO, album);
        Album updatedAlbum = albumRepository.save(album);
        AlbumDTO albumDTONew = convertToDTO(updatedAlbum);
        return albumDTONew;
    }

    @Transactional
    public AlbumDTO addRating(Long albumId, Integer newRating) {
        Album updatedAlbum = albumRepository.getReferenceById(albumId);
        BigDecimal convertedNewRating = BigDecimal.valueOf(newRating);
        updatedAlbum.addRating(convertedNewRating);
        updatedAlbum = albumRepository.save(updatedAlbum);
        return convertToDTO(updatedAlbum);
    }

    @Transactional
    public void delete(Long albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));

        if (!canDeleteAlbum(album)) {
            throw new AlbumDeletionException("Album with id: " + albumId + "cannot be deleted due to policy (10+ Ratings & 4.0+ Rating Score)");
        }
        album.getArtistList().forEach(artist -> artist.getAlbumList().remove(album));
        album.getArtistList().clear();

        album.getTrackList().forEach(track -> track.setAlbum(null));

        albumRepository.deleteById(albumId);
    }

    private boolean canDeleteAlbum(Album album) {
        BigDecimal ratingThreshold = new BigDecimal("4");
        int ratingCountThreshold = 10;
        boolean cannotDelete = album.getRating().compareTo(ratingThreshold) >= 0 && album.getRatingCount() >= ratingCountThreshold;
        return !cannotDelete;
    }

    private Album mapDtoToAlbum(AlbumDTO albumDTO) {
        Album album = new Album();
        album.setName(albumDTO.getName());
        album.setReleaseDate(albumDTO.getReleaseDate());
        album.setRating(albumDTO.getRating());
        album.setRatingCount(albumDTO.getRatingCount());
        return album;
    }

    private void handleTracks(List<Long> trackIds, Album album) {
        if (trackIds != null && !trackIds.isEmpty()) {
            List<Track> tracks = trackRepository.findAllById(trackIds);
            validateFoundEntities(trackIds, tracks, "Track");
            tracks.forEach(album::addTrack);
        }
    }

    private void handleArtists(List<Long> artistIds, Album album) {
        if (artistIds != null && !artistIds.isEmpty()) {
            List<Artist> artists = artistRepository.findAllById(artistIds);
            validateFoundEntities(artistIds, artists, "Artist");
            artists.forEach(album::addArtist);
        }
    }

    private Album updateAlbumFromDTO(AlbumDTO albumDTO, Album album) {
        updateTracks(albumDTO.getTracksIds(), album);
        updateArtists(albumDTO.getArtistIds(), album);

        album.setName(albumDTO.getName());
        album.setReleaseDate(albumDTO.getReleaseDate());
        album.setRating(albumDTO.getRating());
        album.setRatingCount(albumDTO.getRatingCount());

        return album;
    }

    private void updateTracks(List<Long> trackIds, Album album) {
        if (trackIds == null) return;

        List<Track> currentTracks = new ArrayList<>(album.getTrackList());
        currentTracks.forEach(album::removeTrack);

        if (!trackIds.isEmpty()) {
            List<Track> newTracks = trackRepository.findAllById(trackIds);
            validateFoundEntities(trackIds, newTracks, "Track");
            newTracks.forEach(album::addTrack);
        }
    }

    private void updateArtists(List<Long> artistIds, Album album) {
        if (artistIds == null) return;

        album.getArtistList().forEach(artist -> artist.getAlbumList().remove(album));
        album.getArtistList().clear();

        if (!artistIds.isEmpty()) {
            List<Artist> newArtists = artistRepository.findAllById(artistIds);
            validateFoundEntities(artistIds, newArtists, "Artist");
            newArtists.forEach(album::addArtist);
        }
    }

    private <T extends BaseEntity> void validateFoundEntities(List<Long> ids, List<T> entities, String entityType) {
        Set<Long> foundIds = entities.stream().map(BaseEntity::getId).collect(Collectors.toSet());
        Set<Long> requestedIds = new HashSet<>(ids);
        requestedIds.removeAll(foundIds);
        if (!requestedIds.isEmpty()) {
            throw new EntityNotFoundException(entityType + " IDs not found: " + requestedIds);
        }
    }

    private AlbumDTO convertToDTO(Album album) {
        AlbumDTO albumDTO = new AlbumDTO();

        albumDTO.setId(album.getId());
        albumDTO.setName(album.getName());
        albumDTO.setReleaseDate(album.getReleaseDate());
        albumDTO.setRating(album.getRating());
        albumDTO.setRatingCount(album.getRatingCount());

        List<Long> artistIds = new ArrayList<>();
        for (Artist artist : album.getArtistList()) {
            artistIds.add(artist.getId());
        }

        List<Long> trackIds = new ArrayList<>();
        for (Track track : album.getTrackList()) {
            trackIds.add(track.getId());
        }

        albumDTO.setArtistIds(artistIds);
        albumDTO.setTracksIds(trackIds);

        return albumDTO;
    }
}
