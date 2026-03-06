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
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code AudioManager} serves as the centralized controller for all audio playback.
 * <p>
 * It categorizes audio into two main types:
 * <ul>
 * <li><b>Sound Effects (SFX):</b> Short, low-latency sounds managed via {@link AudioClip}. 
 * Multiple SFX can play simultaneously without interrupting each other.</li>
 * <li><b>Music:</b> Long, streaming tracks managed via {@link MediaPlayer}. 
 * Only one music track plays at a time, supporting looping and volume fading.</li>
 * </ul>
 */
public class AudioManager {

    private static MediaPlayer currentMusicPlayer;
    private static String currentMusic;

    /**
     * Internal cache for short sound effect files.
     * SFX are pre-loaded into memory as {@link AudioClip} for immediate playback.
     */
    private static class SoundEffectFiles {
        private static final String RESOURCE_DIR = "/asset/audio/sfx/";
        private static final String[] FILENAMES = {
                "button-click", "card-effect-1", "card-effect-2", "card-effect-3",
                "card-move", "card-spawn", "card-destroy", "game-error",
                "game-win", "mover-place", "mover-rotate", "mover-pickup"
        };

        private static final Map<String, AudioClip> audios = new HashMap<>();

        static {
            for (String filename : FILENAMES) {
                audios.put(filename, new AudioClip(AudioManager.class.getResource(RESOURCE_DIR + filename + ".wav").toExternalForm()));
            }
        }
    }

    /**
     * Internal cache for background music files.
     * Music is stored as {@link Media} objects to be streamed by a {@link MediaPlayer}.
     */
    private static class MusicFiles {
        private static final String RESOURCE_DIR = "/asset/audio/music/";
        private static final String[] FILENAMES = { "music-game", "music-menu", "music-win" };
        private static final Map<String, Media> medias = new HashMap<>();

        static {
            for (String filename : FILENAMES) {
                medias.put(filename, new Media(AudioManager.class.getResource(RESOURCE_DIR + filename + ".mp3").toExternalForm()));
            }
        }
    }

    /**
     * Analyzes a set of modifiers and plays the corresponding sound effects.
     * <p>
     * This is typically called when a card interacts with a tile containing multiple modifiers,
     * ensuring each logic change has a distinct auditory cue.
     * * @param modSet A set of {@link Modifier} instances to play sounds for.
     */
    public static void playSFXWithModifierSet(HashSet<Modifier> modSet) {
        HashSet<String> seen = new HashSet<>();
        for (Modifier mod : modSet) {
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
        for (String name : seen) playSoundEffect(name);
    }

    /**
     * Plays a pre-loaded sound effect by its filename.
     * @param name The name of the sound file (without extension).
     */
    public static void playSoundEffect(String name) {
        AudioClip selectedAudio = SoundEffectFiles.audios.get(name);
        if (selectedAudio != null) selectedAudio.play();
    }

    /**
     * Transitions the background music to the specified track.
     * <p>
     * If the requested track is already playing, this method does nothing.
     * Otherwise, it stops the current track, disposes of its resources, and loops the new track.
     * * @param name The name of the music track to play.
     */
    public static void playMusic(String name) {
        if (Objects.equals(name, currentMusic)) return;
        
        if (currentMusicPlayer != null) {
            currentMusicPlayer.stop();
            currentMusicPlayer.dispose();
        }

        Media selectedMusic = MusicFiles.medias.get(name);
        if (selectedMusic == null) return;

        currentMusicPlayer = new MediaPlayer(selectedMusic);
        currentMusicPlayer.setVolume(0.7);
        currentMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentMusicPlayer.play();
        currentMusic = name;
    }

    /**
     * Gradually increases the music volume over a set duration.
     * @param player The player to fade in.
     * @param seconds Duration of the fade in seconds.
     * @param targetVolume Final volume level (0.0 to 1.0).
     */
    private static void musicFadeIn(MediaPlayer player, double seconds, double targetVolume) {
        player.setVolume(0);
        player.play();

        Timeline fade = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(player.volumeProperty(), targetVolume)
                )
        );
        fade.play();
    }

    /**
     * Gradually decreases the music volume to silence and stops playback.
     * @param player The player to fade out.
     * @param seconds Duration of the fade in seconds.
     */
    private static void musicFadeOut(MediaPlayer player, double seconds) {
        Timeline fade = new Timeline(
                new KeyFrame(Duration.seconds(seconds),
                        new KeyValue(player.volumeProperty(), 0)
                )
        );

        fade.setOnFinished(e -> player.stop());
        fade.play();
    }
}