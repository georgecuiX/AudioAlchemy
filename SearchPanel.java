import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private SongDisplayPanel songDisplayPanel;

    public SearchPanel(SongDisplayPanel songDisplayPanel) {
        this.songDisplayPanel = songDisplayPanel;
        setLayout(new FlowLayout(FlowLayout.LEFT)); // Using FlowLayout for simplicity
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        add(searchField);
        add(searchButton);
    }

    private void performSearch() {
        String searchTerm = searchField.getText();
        DatabaseHelper dbHelper = new DatabaseHelper();
        List<String> results = dbHelper.searchSongs(searchTerm);
        songDisplayPanel.updateSongList(results);
    }
}
