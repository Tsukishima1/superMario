package com.example;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Music {
    public Music() throws FileNotFoundException, JavaLayerException {
        // 播放音乐的对象
        Player player;
        String str = System.getProperty("user.dir") + "/src/main/resource/music/music.wav";

        BufferedInputStream name = new BufferedInputStream(new FileInputStream(str));

        player = new Player(name);

        player.play();
    }
}
