package com.application.music;

import com.application.music.dto.AlbumDTO;
import com.application.music.exception.AlbumDeletionException;
import com.application.music.model.Album;
import com.application.music.repository.AlbumRepository;
import com.application.music.service.AlbumService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumService albumService;

    @Test
    @DisplayName("Return correct albums")
    public void whenGetAllAlbums_thenCorrectAlbumsRetunred() {
        Album mockAlbum = new Album();
        mockAlbum.setName("Test Album");
        when(albumRepository.findAll()).thenReturn(Collections.singletonList(mockAlbum));

        List<AlbumDTO> albums = albumService.getAllAlbums();

        String actual = albums.get(0).getName();
        String expected = "Test Album";

        assertThat(albums).isNotEmpty();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    public void whenDeleteAlbumWithHighRatingAndManyRating_thenThrowException() {
        Album mockAlbum = new Album();
        mockAlbum.setRating(new BigDecimal(4.0));
        mockAlbum.setRatingCount(10);
        when(albumRepository.findById(1L)).thenReturn(Optional.of(mockAlbum));

        assertThrows(AlbumDeletionException.class, () -> albumService.delete(1L));
    }

    @Test
    @DisplayName("Should create a album")
    public void testCreateAlbum() {
    }
}
