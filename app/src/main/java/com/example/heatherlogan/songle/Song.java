package com.example.heatherlogan.songle;

public class Song {

    private String number;
    private String artist;
    private String title;
    private String link;

    public Song(String number, String artist, String title, String link) {

        this.number = number;
        this.artist = artist;
        this.title = title;
        this.link = link;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}
