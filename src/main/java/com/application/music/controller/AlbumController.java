package com.application.music.controller;

import com.application.music.dto.AlbumDTO;
import com.application.music.dto.RatingDTO;
import com.application.music.exception.AlbumDeletionException;
import com.application.music.service.AlbumService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping()
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long id) {
        AlbumDTO albumDTO = albumService.findById(id);
        return ResponseEntity.ok(albumDTO);
    }

    @PostMapping(path = "/album")
    public ResponseEntity<AlbumDTO> createAlbum(@RequestBody @Valid AlbumDTO albumDTO) {
        AlbumDTO newAlbum = albumService.create(albumDTO);
        return ResponseEntity.ok(newAlbum);
    }

    @PatchMapping(path = "/{id}/rating")
    public ResponseEntity<AlbumDTO> addRatingToAlbum(@Valid @RequestBody RatingDTO ratingDTO, @PathVariable Long id) {
        logger.info("Received request to add rating: {}", ratingDTO.getRating());
        try {
            AlbumDTO updatedAlbum = albumService.addRating(id, ratingDTO.getRating());
            logger.info("Rating updated successfully for album with ID: {}", id);
            return ResponseEntity.ok(updatedAlbum);
        } catch (Exception e) {
            logger.error("Error updating rating for album with ID: {}. Error: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> updateAlbum(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
            AlbumDTO updatedAlbum = albumService.update(id, albumDTO);
            return ResponseEntity.ok(updatedAlbum);
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
