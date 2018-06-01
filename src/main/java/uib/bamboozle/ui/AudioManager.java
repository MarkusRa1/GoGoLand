package uib.bamboozle.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class AudioManager implements Disposable {
    private final Map<String, Music> tracks = new HashMap<>();
    private final Map<String, Sound> sounds = new HashMap<>();

    @Override
    public void dispose() {
        tracks.forEach((name, track) -> track.dispose());
        sounds.forEach((name, sound) -> sound.dispose());
    }

    private Sound getSound(String name) {
        if (!sounds.containsKey(name)) {
            sounds.put(name, Gdx.audio.newSound(Gdx.files.internal(name)));
        }
        return sounds.get(name);
    }

    private Music getTrack(String name) {
        if (!tracks.containsKey(name)) {
            tracks.put(name, Gdx.audio.newMusic(Gdx.files.internal(name)));
        }
        return tracks.get(name);
    }

    public void loopSound(String name) {
        getSound(name).stop();
        long id = getSound(name).play();
        getSound(name).setLooping(id, true);
    }

    public void loopTrack(String name) {
        getTrack(name).stop();
        getTrack(name).setLooping(true);
        getTrack(name).play();
    }

    public void pauseAll() {
        tracks.forEach((name, track) -> track.pause());
        sounds.forEach((name, sound) -> sound.pause());
    }

    public void playTrack(String name) {
        stopTrack(name);
        getTrack(name).play();
    }

    public void preloadTracks(String... names) {
        for (String name : names) {
            getTrack(name);
        }
    }

    public void stopTrack(String name) {
        getTrack(name).stop();
        getTrack(name).setLooping(false);
    }
}
