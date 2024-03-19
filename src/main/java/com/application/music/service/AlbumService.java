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
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<AlbumDTO> getALlAlbums() {
        List<Album> albums = albumRepository.findAll();
        List<AlbumDTO> albumsDTO = new ArrayList<>();

        for(Album album: albums) {
            AlbumDTO albumDTO = convertToDTO(album);
            albumsDTO.add(albumDTO);
        }

        return albumsDTO;
    }

    @Transactional
    public AlbumDTO create(AlbumDTO albumDTO) {
        Album album = createOrUpdateOptionalAttributes(albumDTO, new Album());
        Album savedAlbum = albumRepository.save(album);
        return convertToDTO(savedAlbum);
    }

    @Transactional
    public AlbumDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        Album album = albumRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Album not found"));
        album = createOrUpdateOptionalAttributes(albumDTO, album);
        Album updatedAlbum = albumRepository.save(album);
        AlbumDTO albumDTONew = convertToDTO(updatedAlbum);
        return albumDTONew;
    }

    public Album createOrUpdateOptionalAttributes(AlbumDTO albumDTO, Album album) {
        album.setName(albumDTO.getName());
        album.setReleaseDate(albumDTO.getReleaseDate());
        album.setRating(albumDTO.getRating());
        album.setRatingCount(albumDTO.getRatingCount());

        if(albumDTO.getTracksIds() != null && !albumDTO.getTracksIds().isEmpty()) {
            List<Track> tracks = trackRepository.findAllById(albumDTO.getTracksIds());
            album.getTrackList().clear();
            for(Track track: tracks) {
                album.addTrack(track);
            }
        }

        if(albumDTO.getArtistIds() != null && !albumDTO.getArtistIds().isEmpty()) {
            List<Artist> artists = artistRepository.findAllById(albumDTO.getArtistIds());
            if (artists.size() != albumDTO.getArtistIds().size()) {
                // Findet heraus, welche IDs nicht gefunden wurden
                Set<Long> foundIds = artists.stream().map(Artist::getId).collect(Collectors.toSet());
                Set<Long> missingIds = albumDTO.getArtistIds().stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toSet());

                throw new EntityNotFoundException("Artist IDs not found: " + missingIds);
            }
            for(Artist artist : album.getArtistList()) {
                artist.getAlbumList().remove(album);
            }
            album.getArtistList().clear();
            for(Artist artist: artists) {
                album.addArtist(artist);
            }
        }

        return album;
    }

    @Transactional
    public AlbumDTO addRating(Long albumId, double newRating) {
        Album updatedAlbum = albumRepository.getReferenceById(albumId);
        updatedAlbum.addRating(newRating);
        updatedAlbum = albumRepository.save(updatedAlbum);
        return convertToDTO(updatedAlbum);
    }

    @Transactional
    public void delete(Long albumId) {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new EntityNotFoundException("Album not found with id: " + albumId));

        if (!canDeleteAlbum(album)) {
            throw new AlbumDeletionException("Album with id: " + albumId + "cannot be deleted due to policy (10+ Ratings & 4.0+ Rating Score");
        }
        albumRepository.deleteById(albumId);
    }

    public AlbumDTO findById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found with id " + id));
        return convertToDTO(album);
    }

    public boolean canDeleteAlbum(Album album) {
        return !(album.getRating() > 4 && album.getRatingCount() >= 10);
    }

    public AlbumDTO convertToDTO(Album album) {
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
