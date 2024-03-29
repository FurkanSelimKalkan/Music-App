package com.application.music;

import com.application.music.controller.AlbumController;
import com.application.music.dto.AlbumDTO;
import com.application.music.dto.RatingDTO;
import com.application.music.exception.AlbumDeletionException;
import com.application.music.service.AlbumService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;





@WebMvcTest(AlbumController.class)
public class AlbumRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should return statuscode 200 when calling getAll albums")
    public void whenGetAllAlbums_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/albums"))
                .andExpect(status().isOk());

        verify(albumService, times(1)).getAllAlbums();
    }


    @Test
    public void whenDeleteAlbumForbidden_thenStatus403() throws Exception {
        doThrow(new AlbumDeletionException("Cannot delete album")).when(albumService).delete(2L);

        mockMvc.perform(delete("/api/v1/albums/{id}", 2))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenGetAllAlbums_thenCorrectAlbumsReturned() throws Exception {
        AlbumDTO mockAlbum1 = new AlbumDTO();
        AlbumDTO mockAlbum2 = new AlbumDTO();
        mockAlbum1.setName("Furkans Album");
        mockAlbum2.setName("Album von Fu");

        List<AlbumDTO> mockAlbums = Arrays.asList(mockAlbum1, mockAlbum2);

        when(albumService.getAllAlbums()).thenReturn(mockAlbums);

        mockMvc.perform(get("/api/v1/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(mockAlbum1.getName())))
                .andExpect(jsonPath("$[1].name", is(mockAlbum2.getName())));
    }

    @Test
    @DisplayName("Reject rating when out of valid range")
    public void whenAddInvalidRatingToAlbum_thenValidationError() throws Exception {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRating(6);

        mockMvc.perform(patch("/api/v1/albums/{id}/rating", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.rating").value(containsString("Number cannot be Bigger than 5")));

        verify(albumService, never()).addRating(anyLong(), any());
    }


}
