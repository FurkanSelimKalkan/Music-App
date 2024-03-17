package com.application.music.dto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AlbumDTO {
    private String name;
    private Optional<List<Long>> artistIds;
    private Optional<List<Long>> tracksIds;
    private Optional<Date> releaseDate;
    private Optional<Integer> ratingCount;
    private Optional<Double> rating;

    public AlbumDTO(String name, Optional<List<Long>> artistIds, Optional<List<Long>> tracksIds, Optional<Date> releaseDate,Optional<Integer> ratingCount, Optional<Double> rating) {
        this.name = name;
        this.artistIds = artistIds;
        this.tracksIds = tracksIds;
        this.releaseDate = releaseDate;
        this.ratingCount = ratingCount;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<List<Long>> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(Optional<List<Long>> artistIds) {
        this.artistIds = artistIds;
    }

    public Optional<List<Long>> getTracksIds() {
        return tracksIds;
    }

    public void setTracksIds(Optional<List<Long>> tracksIds) {
        this.tracksIds = tracksIds;
    }

    public Optional<Date> getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Optional<Date> releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Optional<Integer> getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Optional<Integer> ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Optional<Double> getRating() {
        return rating;
    }

    public void setRating(Optional<Double> rating) {
        this.rating = rating;
    }
}
