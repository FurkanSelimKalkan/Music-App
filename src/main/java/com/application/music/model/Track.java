package com.application.music.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
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

    public void addArtist(Artist artist) {
        artists.add(artist);
        artist.getTrackList().add(this);
    }

    public void removeArtist(Artist artist) {
        artists.remove(artist);
        artist.getTrackList().remove(this);
    }

}
