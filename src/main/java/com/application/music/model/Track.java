package com.application.music.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Track extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String title;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "track_artist",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private List<Artist> artists = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    public Track(String title, List<Artist> artists, Album album) {
        this.title = title;
        this.artists = artists;
        this.album = album;
    }

    public Track() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
        artist.getTrackList().add(this);
    }

    public void removeArtist(Artist artist) {
        artists.remove(artist);
        artist.getTrackList().remove(this);
    }

}
