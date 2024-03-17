package com.application.music.model;

import jakarta.persistence.*;

import java.util.ArrayList;
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
    private List<Track> trackList = new ArrayList<>();

    @ManyToMany(cascade =  {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "artist_album", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private List<Album> albumList = new ArrayList<>();

    public Artist(String name, List<Track> trackList, List<Album> albumList) {
        this.name = name;
        this.trackList = trackList;
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

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> tracks) {
        this.trackList = tracks;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    public void addTrack(Track track) {
        trackList.add(track);
        track.getArtists().add(this);
    }

    public void removeTrack(Track track) {
        trackList.remove(track);
        track.getArtists().remove(this);
    }

    public void addAlbum(Album album) {
        albumList.add(album);
        album.getArtistList().add(this);
    }

    public void removeAlbum(Album album) {
        albumList.remove(album);
        album.getArtistList().remove(this);
    }
}
