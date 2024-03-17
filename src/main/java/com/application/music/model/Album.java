package com.application.music.model;

import jakarta.persistence.*;

import java.util.ArrayList;
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
    private List<Artist> artistList = new ArrayList<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @Column(name = "tracks", nullable = false)
    private List<Track> trackList = new ArrayList<>();

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "rating_count")
    private int ratingCount = 0;

    @Column(name = "rating")
    private double rating = 0.0;

    public Album(String name, List<Artist> artistList, List<Track> trackList, Date releaseDate,int ratingCount, double rating) {
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
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

    public void addRating(double newRating) {
        double totalRating = this.rating * this.ratingCount + newRating;
        this.ratingCount++;
        this.rating = totalRating / this.ratingCount;
    }


}
