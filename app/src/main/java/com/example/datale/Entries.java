package com.example.datale;

public class Entries {
    String eentry, elocation, edate, etime;
    String eemoji, evideo, ephoto, eaudio;

    public Entries() { }

    public Entries(String eentry, String elocation, String edate, String etime) {
        this.eentry = eentry;
        this.elocation = elocation;
        this.edate = edate;
        this.etime = etime;
    }

    public Entries(String eentry, String elocation, String edate, String etime, String eemoji, String evideo, String ephoto, String eaudio) {
        this.eentry = eentry;
        this.elocation = elocation;
        this.edate = edate;
        this.etime = etime;
        this.eemoji = eemoji;
        this.evideo = evideo;
        this.ephoto = ephoto;
        this.eaudio = eaudio;
    }

    public String getEentry() {
        return eentry;
    }

    public void setEentry(String eentry) {
        this.eentry = eentry;
    }

    public String getElocation() {
        return elocation;
    }

    public void setElocation(String elocation) {
        this.elocation = elocation;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getEemoji() {
        return eemoji;
    }

    public void setEemoji(String eemoji) {
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
}
