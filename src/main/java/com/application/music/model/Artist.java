package com.application.music.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "artists", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Track> tracks;

    @ManyToMany(cascade =  {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "artist_album", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private List<Album> albumList;

    public Artist(String name, List<Track> tracks, List<Album> albumList) {
        this.name = name;
        this.tracks = tracks;
        this.albumList = albumList;
    }
    public Artist(){}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    public void addTrack(Track track) {
        tracks.add(track);
        track.getArtists().add(this);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
        track.getArtists().remove(this);
    }
}
