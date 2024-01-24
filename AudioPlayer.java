import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.InputStream;

public class AudioPlayer {
    private Player player;
    private String currentFilePath;

    public void loadAudio(String filePath) throws Exception {
        if (player != null) {
            player.close();
        }
        this.currentFilePath = filePath;
        InputStream is = new FileInputStream(filePath);
        player = new Player(is);
    }

    public void play() {
        if (player != null) {
            new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void stop() {
        if (player != null) {
            player.close();
            player = null;  // Reset the player
            try {
                loadAudio(currentFilePath);  // Reload the audio for future playback
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
