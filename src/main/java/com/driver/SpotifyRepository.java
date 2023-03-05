package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User person = new User(name,mobile);
        users.add(person);
        return person;
    }

    public Artist createArtist(String name) {
        Artist person = new Artist(name);
        artists.add(person);
        return person;
    }

    public Album createAlbum(String title, String artistName) {
        // if artists does not exists
        if(!artists.contains(artistName)){
            createArtist(artistName);
        }
        // search for artist
        Artist artistKey = null;
        for(Artist a: artists){
            if(artistName.equals(a.getName())){
                artistKey = a;
                break;
            }
        }
        // creating album
        Album tempAlbum = new Album(title);
        albums.add(tempAlbum);

        // put in artist-album map
        List<Album> tempAlbumList;
        if(artistAlbumMap.containsKey(artistKey)){
            tempAlbumList = artistAlbumMap.get(artistKey);
        }else{
            tempAlbumList = new ArrayList<>();
        }
        tempAlbumList.add(tempAlbum);
        artistAlbumMap.put(artistKey,tempAlbumList);

        return tempAlbum;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{

        // search for album
        Album albumKey = null;
        for(Album a: albums){
            if(albumName.equals(a.getTitle())){
                albumKey = a;
                break;
            }
        }
        if(albumKey==null)   throw new Exception("Album does not exist");

        // create song
        Song gaana = new Song(title,length);
        songs.add(gaana);

        // putting in album - song map
        List<Song> tempSongsList = new ArrayList<>();
        if(albumSongMap.containsKey(albumKey)){
            tempSongsList = albumSongMap.get(albumKey);
        }
        tempSongsList.add(gaana);
        albumSongMap.put(albumKey,tempSongsList);

        return gaana;
    }
    public User getUser(String mobile){
        User currUser = null;
        for(User a: users){
            if(mobile.equals(a.getMobile())){
                currUser = a;
                break;
            }
        }
        return currUser;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

        Playlist tempPlaylist = new Playlist(title);
        playlists.add(tempPlaylist);

        // list of songs having given length
        List<Song> songOfGivenLength = new ArrayList<>();
        for(Song s: songs){
            if(length == s.getLength()){
                songOfGivenLength.add(s);
            }
        }
        //        public HashMap<Playlist, List<Song>> playlistSongMap;
        playlistSongMap.put(tempPlaylist,songOfGivenLength);

        //        public HashMap<User, Playlist> creatorPlaylistMap;
        creatorPlaylistMap.put(currUser,tempPlaylist);

//        public HashMap<Playlist, List<User>> playlistListenerMap;
        List<User> listOfListener = playlistListenerMap.getOrDefault(tempPlaylist,new ArrayList<>());
        listOfListener.add(currUser);
        playlistListenerMap.put(tempPlaylist,listOfListener);

//        public HashMap<User, List<Playlist>> userPlaylistMap;
        List<Playlist> listOfPlaylist = userPlaylistMap.getOrDefault(currUser,new ArrayList<>());
        listOfPlaylist.add(tempPlaylist);
        userPlaylistMap.put(currUser,listOfPlaylist);

        return tempPlaylist;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        // user
        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

        // playlist
        Playlist tempPlaylist = new Playlist(title);
        playlists.add(tempPlaylist);

        // songs
        List<Song> songOfGivenName = new ArrayList<>();
        for(Song s: songs){
            if(songTitles.contains(s.getTitle())){
                songOfGivenName.add(s);
            }
        }

        // playlist - list of song map
        playlistSongMap.put(tempPlaylist,songOfGivenName);

        // creator - playlist map
        creatorPlaylistMap.put(currUser,tempPlaylist);

        // playlist - list of listeners map
        List<User> listOfListener = playlistListenerMap.getOrDefault(tempPlaylist,new ArrayList<>());
        listOfListener.add(currUser);
        playlistListenerMap.put(tempPlaylist,listOfListener);


        // user - list of playlist map
        List<Playlist> listOfPlaylist = userPlaylistMap.getOrDefault(currUser,new ArrayList<>());
        listOfPlaylist.add(tempPlaylist);
        userPlaylistMap.put(currUser,listOfPlaylist);

        return tempPlaylist;

    }


    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        // check for playlist existance
        Playlist currPlaylist = null;
        for(Playlist p: playlists){
            if(playlistTitle.equals(p.getTitle())){
                currPlaylist = p;
                break;
            }
        }
        if(currPlaylist==null) throw new Exception("Playlist does not exist");

        // check for user existance
        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

//        public HashMap<Playlist, List<User>> playlistListenerMap;
//        public HashMap<User, Playlist> creatorPlaylistMap;
        // making curr user as listener if he is not already
        List<User> tempList = new ArrayList<>();
        if(playlistListenerMap.containsKey(currPlaylist)){
            tempList = playlistListenerMap.get(currPlaylist);
        }
        if(!tempList.contains(currUser)){
            tempList.add(currUser);
        }
        playlistListenerMap.put(currPlaylist,tempList);

        if(!creatorPlaylistMap.get(currUser).equals(currPlaylist)){
            creatorPlaylistMap.put(currUser,currPlaylist);
        }

        List<Playlist> temp2PlayList = new ArrayList<>();
        if(userPlaylistMap.containsKey(currUser)){
            temp2PlayList = userPlaylistMap.get(currUser);
        }
        if(!temp2PlayList.contains(currPlaylist)){
            temp2PlayList.add(currPlaylist);
        }
        userPlaylistMap.put(currUser,temp2PlayList);

        return currPlaylist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User currUser = getUser(mobile);
        if(currUser==null) throw new Exception("User does not exist");

        // check for song existance
        Song currSong = null;
        for(Song s: songs){
            if(songTitle.equals(s.getTitle())){
                currSong = s;
                break;
            }
        }
        if(currSong==null) throw new Exception("Playlist does not exist");

//        public HashMap<Song, List<User>> songLikeMap;
        List<User> likesList = songLikeMap.getOrDefault(currSong,new ArrayList<>());
        if(!likesList.contains(currUser)){
            likesList.add(currUser);
            songLikeMap.put(currSong,likesList);

            currSong.setLikes(currSong.getLikes()+1);
//            public HashMap<Artist, List<Album>> artistAlbumMap;
//            public HashMap<Album, List<Song>> albumSongMap;

            // song -> album
            Album currAlbum = null;
            for(Album a: albumSongMap.keySet()){
                if(albumSongMap.get(a).contains(currSong)){
                    currAlbum = a;
                    break;
                }
            }
            // album -> artist
            Artist currArtist = null;
            for(Artist a: artistAlbumMap.keySet()){
                if(artistAlbumMap.get(a).contains(currAlbum)){
                    currArtist = a;
                    break;
                }
            }
            currArtist.setLikes(currArtist.getLikes()+1);

        }



        return currSong;
    }

    public String mostPopularArtist() {
        int maxLikes = 0;
        String result ="";
        for(Artist a: artists){
            if(a.getLikes()>maxLikes){
                maxLikes = a.getLikes();
                result = a.getName();
            }
        }
        return result;
    }

    public String mostPopularSong() {
        int maxLikes = 0;
        String result ="";
        for(Song a: songs){
            if(a.getLikes()>maxLikes){
                maxLikes = a.getLikes();
                result = a.getTitle();
            }
        }
        return result;
    }
}
