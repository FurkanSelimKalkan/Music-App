package com.application.music;

import com.application.music.model.Album;
import com.application.music.model.Artist;
import com.application.music.model.Track;
import com.application.music.repository.AlbumRepository;
import com.application.music.repository.ArtistRepository;
import com.application.music.repository.TrackRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class MusicApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicApplication.class, args);
	}
}
