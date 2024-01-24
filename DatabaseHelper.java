import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:your_music_library.db";

    public DatabaseHelper() {
        initialize();
    }

    private void initialize() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                var stmt = conn.createStatement();
                // SQL statement for creating a new table
                String sql = "CREATE TABLE IF NOT EXISTS songs ("
                        + " id integer PRIMARY KEY,"
                        + " title text NOT NULL,"
                        + " artist text,"
                        + " filePath text NOT NULL"
                        + ");";
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertSong(String title, String artist, String filePath) {
        String sql = "INSERT INTO songs(title, artist, filePath) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setString(3, filePath);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteSong(int songId) {
        String sql = "DELETE FROM songs WHERE id = ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, songId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }    

    public List<String> searchSongs(String searchTerm) {
        List<String> results = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE title LIKE ?";
    
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
    
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                // Optionally, include other details like artist
                results.add(title);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    public int getSongId(String title) {
        String sql = "SELECT id FROM songs WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1; // Return an invalid ID if not found or error occurs
    }

    public List<String> getAllSongs() {
        List<String> songs = new ArrayList<>();
        String sql = "SELECT title, artist FROM songs";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                songs.add(title + " - " + (artist != null ? artist : "Unknown Artist"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return songs;
    }

    public boolean songExists(String songName) {
        String sql = "SELECT COUNT(id) FROM songs WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, songName);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getFilePath(String songTitle) {
        String sql = "SELECT filePath FROM songs WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, songTitle);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("filePath");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
