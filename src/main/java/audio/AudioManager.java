package audio;

import component.modifier.Modifier;
import component.modifier.changer.Arithmetic;
import component.modifier.changer.Setter;
import component.modifier.combinator.Combinator;
import component.modifier.pathway.Entrance;
import component.modifier.pathway.Exit;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class AudioManager {

    private static MediaPlayer currentMusicPlayer;
    private static String currentMusic;

    private static class SoundEffectFiles {
        private static final String RESOURCE_DIR = "/asset/audio/sfx/";
        private static final String[] FILENAMES = {
                "button-click",
                "card-effect-1",
                "card-effect-2",
                "card-effect-3",
                "card-move",
                "card-spawn",
                "card-destroy",
                "game-error",
                "game-win",
                "mover-place",
                "mover-rotate",
                "mover-pickup"
        };

        private static final Map<String, AudioClip> audios = new HashMap<>();

        static {
            for (String filename: FILENAMES) {
                 audios.put(filename, new AudioClip( AudioManager.class.getResource(RESOURCE_DIR + filename + ".wav").toExternalForm()));
            }
        }

    }

    private static class MusicFiles {
        private static final String RESOURCE_DIR = "/asset/audio/music/";
        private static final String[] FILENAMES = { "music-game", "music-menu", "music-win" };
        private static final Map<String, Media> medias = new HashMap<>();

        static {
            for (String filename: FILENAMES) {

                medias.put(filename, new Media( AudioManager.class.getResource(RESOURCE_DIR + filename + ".mp3").toExternalForm() ));
            }
        }
   }

   public static void playSFXWithModifierSet(HashSet<Modifier> modSet) {
        HashSet<String> seen = new HashSet<>();
        for (Modifier mod: modSet) {
            if (mod instanceof Arithmetic)
                seen.add("card-effect-1");
            else if (mod instanceof Setter<?>)
                seen.add("card-effect-2");
            else if (mod instanceof Combinator)
                seen.add("card-effect-3");
            else if (mod instanceof Entrance)
                seen.add("card-spawn");
            else if (mod instanceof Exit)
                seen.add("card-destroy");
        }
        for (String name: seen) playSoundEffect(name);
   }

    public static void playSoundEffect(String name) {
        AudioClip selectedAudio = SoundEffectFiles.audios.get(name);
        selectedAudio.play();
    }

    public static void playMusic(String name) {
        // 1. Stop and clean up the previous song
        if (Objects.equals(name, currentMusic)) return;
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer.dispose();
        }

        Media selectedMusic = MusicFiles.medias.get(name);
        if (selectedMusic == null) return;

        // 2. Just create the player and hit play
        currentMusicPlayer = new MediaPlayer(selectedMusic);
        currentMusicPlayer.setVolume(0.7);
        currentMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentMusicPlayer.play();
        currentMusic = name;
    }

    private static void musicFadeIn(MediaPlayer player, double seconds, double targetVolume) {
        player.setVolume(0); // Start at silence
        player.play();       // Start playing

        Timeline fade = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(player.volumeProperty(), targetVolume)
                )
        );
        fade.play();
    }

    private static void musicFadeOut(MediaPlayer player, double seconds) {
        Timeline fade = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(player.volumeProperty(), 0) // Target volume: 0
                )
        );

        // Optional: Stop the player once the fade finishes
        fade.setOnFinished(e -> player.stop());
        fade.play();
    }

}
