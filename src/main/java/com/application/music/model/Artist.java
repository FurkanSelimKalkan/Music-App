package com.application.music.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Artist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "artists", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Track> trackList = new ArrayList<>();

    @ManyToMany(cascade =  {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "artist_album", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private List<Album> albumList = new ArrayList<>();

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
