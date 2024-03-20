package com.application.music.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "albumList", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Artist> artistList = new ArrayList<>();

    @OneToMany(mappedBy = "album", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Column(name = "tracks", nullable = false)
    private List<Track> trackList = new ArrayList<>();

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Column(name = "rating")
    private BigDecimal rating = new BigDecimal(0);

    public Album(String name, List<Artist> artistList, List<Track> trackList, LocalDate releaseDate,int ratingCount, BigDecimal rating) {
        this.name = name;
        this.artistList = artistList;
        this.trackList = trackList;
        this.releaseDate = releaseDate;
        this.ratingCount = ratingCount;
        this.rating = rating;
    }

    public Album() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artists) {
        this.artistList = artists;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> tracks) {
        this.trackList = tracks;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public void addArtist(Artist artist) {
        artistList.add(artist);
        artist.getAlbumList().add(this);
    }

    public void removeArtist(Artist artist) {
        artistList.remove(artist);
        artist.getAlbumList().remove(this);
    }

    public void addTrack(Track track) {
        trackList.add(track);
        track.setAlbum(this);
    }

    public void removeTrack(Track track) {
        trackList.remove(track);
        track.setAlbum(null);
    }



    public void addRating(BigDecimal newRating) {
        BigDecimal totalRating = this.rating.multiply(BigDecimal.valueOf(this.ratingCount)).add(newRating);
        this.ratingCount++;
        BigDecimal newCount = BigDecimal.valueOf(this.ratingCount);
        this.rating = totalRating.divide(newCount, 2, RoundingMode.HALF_UP);
    }



}
