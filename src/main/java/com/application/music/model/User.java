package com.application.music.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
@Data
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max=50)
    @Email
    @Column(name= "mail", unique = true)
    private String mail;

    @NotBlank
    @Size(max=100, min = 6)
    @Column(name= "password")
    private String password;

    @OneToMany
    private List<Album> favoritAlbums = new ArrayList<>();

    @OneToMany
    private List<Artist> favoritArtists = new ArrayList<>();

    @OneToMany
    private List<Track> favoritTracks = new ArrayList<>();
}
