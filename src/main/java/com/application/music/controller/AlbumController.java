package com.application.music.controller;

import com.application.music.dto.AlbumDTO;
import com.application.music.dto.RatingDTO;
import com.application.music.model.Album;
import com.application.music.service.AlbumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping()
    public ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getALlAlbums());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        Album album = albumService.findById(id);
        return ResponseEntity.ok(album);
    }

    @PostMapping(path = "/album")
    public ResponseEntity<Void> createAlbum(@RequestBody AlbumDTO albumDTO) throws URISyntaxException {
        Album newAlbum = albumService.create(albumDTO);
        URI uri = new URI("/api/v1/albums" + newAlbum.getId());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/{id}/rating")
    public ResponseEntity<Album> addRatingToAlbum(@PathVariable Long id,@Valid @RequestBody RatingDTO ratingDTO) {
            albumService.addRating(id, ratingDTO.getRating());
            return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
