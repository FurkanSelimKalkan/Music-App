package com.application.music.model;

import jakarta.persistence.*;

import java.util.Date;
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
    private List<Artist> artists;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @Column(name = "tracks", nullable = false)
    private List<Track> tracks;

    @Column(name = "release_date", nullable = false)
    private Date releaseDate;

    @Column(name = "rating", nullable = false)
    private double rating;

    public Album(String name, List<Artist> artists, List<Track> tracks, Date releaseDate, double rating) {
        this.name = name;
        this.artists = artists;
        this.tracks = tracks;
        this.releaseDate = releaseDate;
        this.rating = rating;
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

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
