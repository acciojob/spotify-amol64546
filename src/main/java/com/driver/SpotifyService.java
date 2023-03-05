package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){
        User person = spotifyRepository.createUser(name,mobile);
        return person;
    }

    public Artist createArtist(String name) {
        Artist person = spotifyRepository.createArtist(name);
        return person;
    }

    public Album createAlbum(String title, String artistName) {
        Album songsList = spotifyRepository.createAlbum(title,artistName);
        return songsList;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        Song gaana = spotifyRepository.createSong(title, albumName, length);
        return gaana;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist tempPlaylist = spotifyRepository.createPlaylistOnLength(mobile, title, length);
        return tempPlaylist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist tempPlaylist = spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
        return tempPlaylist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist tempPlaylist = spotifyRepository.findPlaylist(mobile,playlistTitle);
        return tempPlaylist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        Song tempSong = spotifyRepository.likeSong(mobile, songTitle);
        return tempSong;
    }

    public String mostPopularArtist() {
        return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
        return spotifyRepository.mostPopularSong();
    }
}
