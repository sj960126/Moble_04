package com.withpet.iot;

public class iot_recordeUpload {
    private String audiofile;

    public iot_recordeUpload(){

    }
    public iot_recordeUpload(String audiofile){
        this.audiofile = audiofile;
    }

    public String getAudiofile() {
        return audiofile;
    }

    public void setAudiofile(String audiofile) {
        this.audiofile = audiofile;
    }
}
