package com.application.music.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Album {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
