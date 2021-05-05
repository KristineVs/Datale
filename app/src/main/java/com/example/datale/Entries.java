package com.example.datale;

import java.util.Date;

public class Entries {

    String etitle, eentry, evideo, ephoto, eaudio;
    Date edate;
    int eemoji;
    double latitude, longitude;

    public Entries() { }

    public Entries(String etitle, String eentry, int eemoji, String evideo, String ephoto, String eaudio, Date edate, float latitude, float longitude) {
        this.etitle = etitle;
        this.eentry = eentry;
        this.eemoji = eemoji;
        this.evideo = evideo;
        this.ephoto = ephoto;
        this.eaudio = eaudio;
        this.edate = edate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Entries(String etitle, String eentry, Date edate) {
        this.etitle = etitle;
        this.eentry = eentry;
        this.edate = edate;
    }

    public String getEtitle() {
        return etitle;
    }

    public void setEtitle(String etitle) {
        this.etitle = etitle;
    }

    public String getEentry() {
        return eentry;
    }

    public void setEentry(String eentry) {
        this.eentry = eentry;
    }

    public int getEemoji() {
        return eemoji;
    }

    public void setEemoji(int eemoji) {
        this.eemoji = eemoji;
    }

    public String getEvideo() {
        return evideo;
    }

    public void setEvideo(String evideo) {
        this.evideo = evideo;
    }

    public String getEphoto() {
        return ephoto;
    }

    public void setEphoto(String ephoto) {
        this.ephoto = ephoto;
    }

    public String getEaudio() {
        return eaudio;
    }

    public void setEaudio(String eaudio) {
        this.eaudio = eaudio;
    }

    public Date getEdate() {
        return edate;
    }

    public void setEdate(Date edate) {
        this.edate = edate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
