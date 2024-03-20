package com.application.music.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlbumDTO {
    private Long id;
    private String name;
    private List<Long> artistIds =  new ArrayList<>();
    private List<Long> tracksIds = new ArrayList<>();
    private LocalDate releaseDate;
    @DecimalMin(value = "0.00", message = "Number cannot be smaller than 0")
    @DecimalMax(value = "5.00", message = "Number cannot be Bigger than 5")
    private BigDecimal rating = new BigDecimal(0);
    private Integer ratingCount = 0;

    public AlbumDTO(Long id, String name, List<Long> artistIds, List<Long> tracksIds, LocalDate releaseDate, BigDecimal rating, Integer ratingCount) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
        this.tracksIds = tracksIds;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public AlbumDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(List<Long> artistIds) {
        this.artistIds = artistIds;
    }

    public List<Long> getTracksIds() {
        return tracksIds;
    }

    public void setTracksIds(List<Long> tracksIds) {
        this.tracksIds = tracksIds;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }
}
