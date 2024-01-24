import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SongDisplayPanel extends JPanel {
    private JList<String> songList;
    private DefaultListModel<String> listModel;
    private JButton playButton, stopButton, deleteButton;
    private AudioPlayer audioPlayer = new AudioPlayer(); // Audio player instance

    public SongDisplayPanel() {
        setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        songList = new JList<>(listModel);

        // Button setup
        deleteButton = new JButton("Delete Song");
        playButton = new JButton("Play");
        stopButton = new JButton("Stop");

        // Button actions
        deleteButton.addActionListener(e -> deleteSelectedSong());
        playButton.addActionListener(e -> playSelectedSong());
        stopButton.addActionListener(e -> audioPlayer.stop());

        // Control panel for buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(playButton);
        controlPanel.add(stopButton);
        controlPanel.add(deleteButton);

        // Adding components
        add(new JScrollPane(songList), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        loadSongs();
    }

    private void loadSongs() {
        DatabaseHelper dbHelper = new DatabaseHelper();
        List<String> songs = dbHelper.getAllSongs();
        listModel.clear();
        for (String song : songs) {
            listModel.addElement(song);
        }
    }

    public void refreshSongs() {
        loadSongs();  // Re-fetch and update the list of songs
    }

    public void updateSongList(List<String> songs) {
        listModel.clear();
        for (String song : songs) {
            listModel.addElement(song);
        }
    }

    private void deleteSelectedSong() {
        String selectedSong = songList.getSelectedValue();
        if (selectedSong != null) {
            DatabaseHelper dbHelper = new DatabaseHelper();
    
            // Extracting the title from the selected song
            String title = selectedSong.split(" - ")[0];
            int songId = dbHelper.getSongId(title);
    
            if (songId != -1) {
                dbHelper.deleteSong(songId);
                listModel.removeElement(selectedSong);
            } else {
                JOptionPane.showMessageDialog(this, "Song ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void playSelectedSong() {
        String selectedSong = songList.getSelectedValue();
        if (selectedSong != null) {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                String filePath = dbHelper.getFilePath(selectedSong.split(" - ")[0]); // Ensure this method exists and works correctly
                audioPlayer.loadAudio(filePath);
                audioPlayer.play();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error playing the song: " + ex.getMessage(), "Playback Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}