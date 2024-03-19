package com.application.music.controller;

import com.application.music.dto.AlbumDTO;
import com.application.music.dto.RatingDTO;
import com.application.music.exception.AlbumDeletionException;
import com.application.music.model.Album;
import com.application.music.service.AlbumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getALlAlbums());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long id) {
        AlbumDTO albumDTO = albumService.findById(id);
        return ResponseEntity.ok(albumDTO);
    }

    @PostMapping(path = "/album")
    public ResponseEntity<Void> createAlbum(@RequestBody @Valid AlbumDTO albumDTO) throws URISyntaxException {
        AlbumDTO newAlbum = albumService.create(albumDTO);
        URI uri = new URI("/api/v1/albums" + newAlbum.getId());
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping(path = "/{id}/rating")
    public ResponseEntity<AlbumDTO> addRatingToAlbum(@PathVariable Long id,@Valid @RequestBody RatingDTO ratingDTO) {
            AlbumDTO updatedAlbum = albumService.addRating(id, ratingDTO.getRating());
            return ResponseEntity.ok(updatedAlbum);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> updateAlbum(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
        try {
            AlbumDTO updatedAlbum = albumService.updateAlbum(id, albumDTO);
            return ResponseEntity.ok(updatedAlbum);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteAlbum(@PathVariable Long id) {
        try {
            albumService.delete(id);
            return ResponseEntity.ok().build();
        } catch (AlbumDeletionException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
