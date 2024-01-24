import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Music Application GUI");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Create panels
            SongDisplayPanel songDisplayPanel = new SongDisplayPanel();
            SearchPanel searchPanel = new SearchPanel(songDisplayPanel);

            // Button to add songs
            JButton addSongButton = new JButton("Add Song");
            addSongButton.addActionListener(e -> openFileChooser(frame, songDisplayPanel));

            JPanel addSongPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            addSongPanel.add(addSongButton);

            // Add panels to the frame
            frame.setLayout(new BorderLayout());
            frame.add(songDisplayPanel, BorderLayout.CENTER);
            frame.add(searchPanel, BorderLayout.NORTH);
            frame.add(addSongPanel, BorderLayout.WEST);

            frame.setVisible(true);
        });
    }

    private static void openFileChooser(JFrame frame, SongDisplayPanel songDisplayPanel) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3");
        fileChooser.setFileFilter(filter);
    
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                if (!selectedFile.getName().toLowerCase().endsWith(".mp3")) {
                    throw new UnsupportedAudioFileException("Selected file is not an MP3 file.");
                }
    
                // Prompt for song name
                String songName = JOptionPane.showInputDialog(frame, "Enter Song Name:", "New Song", JOptionPane.PLAIN_MESSAGE);
                if (songName != null && !songName.trim().isEmpty()) {
                    DatabaseHelper dbHelper = new DatabaseHelper();
                    if (!dbHelper.songExists(songName)) {
                        dbHelper.insertSong(songName, "", selectedFile.getAbsolutePath()); 
                        songDisplayPanel.refreshSongs();
                    } else {
                        JOptionPane.showMessageDialog(frame, "A song with this name already exists.", "Duplicate Song", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Song name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (UnsupportedAudioFileException e) {
                JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
