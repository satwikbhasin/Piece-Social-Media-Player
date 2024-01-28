package com.piece.v1;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MusicPlayerController implements Initializable {
    public static final HashMap<Integer, MediaPlayer> mediaPlayersMap = new HashMap<>();
    public static Integer musicId = 1;
    public static HashMap<Integer, ArrayList<String>> namesMap = new HashMap<>();
    @FXML
    public Label suggestionLabel;
    @FXML
    public Button likeButton;
    @FXML
    public Label likedByLabel;
    @FXML
    public Label songName;
    @FXML
    public Button previousSong;
    @FXML
    public Button nextSong;
    @FXML
    public ImageView songIcon;
    @FXML
    public Button playPauseButton;
    @FXML
    public ImageView playPauseImage;
    @FXML
    public Slider slider;
    @FXML
    public Label timeStamp;
    String likedByNames = "";

    public static String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);
        millis /= 60;
        String h = formatTime(millis % 24);
        return h + ":" + m + ":" + s;
    }

    public static String formatTime(double time) {
        int t = (int) time;
        if (t > 9) {
            return String.valueOf(t);
        }
        return "0" + t;
    }

    public void likedOrNot() throws SQLException {
        ResultSet likedOrNot = Utilities.connection.createStatement().executeQuery("SELECT * FROM social WHERE music_id=" + musicId + " AND user_id=" + Utilities.userId + ";");
        if (likedOrNot.next()) {
            likeButton.setText("Liked");
        } else {
            likeButton.setText("Like");
        }
    }

    private void playPauseLogic() {
        System.out.println("reached playPauseLogic and current media player's status is: " + mediaPlayersMap.get(musicId).getStatus());
        if (mediaPlayersMap.get(musicId).getStatus().equals(MediaPlayer.Status.STOPPED) || mediaPlayersMap.get(musicId).getStatus().equals(MediaPlayer.Status.PAUSED) || mediaPlayersMap.get(musicId).getStatus().equals(MediaPlayer.Status.READY)) {
            System.out.println("Playing now!");
            changePlayPauseImage("Playing");
            mediaPlayersMap.get(musicId).play();
            bindTimeStamp();
            System.out.println("status after playing: " + mediaPlayersMap.get(musicId).getStatus());
        } else if (mediaPlayersMap.get(musicId).getStatus().equals(MediaPlayer.Status.PLAYING)) {
            System.out.println("Paused now!");
            changePlayPauseImage("Paused");
            mediaPlayersMap.get(musicId).pause();
            bindTimeStamp();
            System.out.println("status after pausing: " + mediaPlayersMap.get(musicId).getStatus());
        }
    }

    private void changePlayPauseImage(String status) {
        String playPauseImagePath = "src//main//resources//com//piece//v1//Images//";
        File file;
        if (status.equals("Playing"))
            file = new File(playPauseImagePath + "Pause.png");
        else
            file = new File(playPauseImagePath + "Play.png");
        String imagePath = new File(file.getPath()).toURI().toString();
        Image image = new Image(imagePath);
        playPauseImage.setImage(image);
    }

    public void nextSong() throws SQLException {
        if (musicId != 3) {
            ++musicId;
            resetPlayers();
            likedOrNot();
            updateLabel();
            suggestionLabel.setText("");
            ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT song_icon, song_name FROM songs WHERE song_id =" + musicId + ";");
            while (rs.next()) {
                File file = new File("src//main//resources//com//piece//v1//Images//" + rs.getString(1));
                String imagePath = new File(file.getPath()).toURI().toString();
                Image image = new Image(imagePath);
                songIcon.setImage(image);
                songName.setText(rs.getString(2));
                System.out.println(rs.getString(2));
            }
        }
    }

    public void previousSong() throws SQLException {
        if (musicId != 1) {
            --musicId;
            resetPlayers();
            likedOrNot();
            updateLabel();
            suggestionLabel.setText("");
            ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT song_icon, song_name FROM songs WHERE song_id =" + musicId + ";");
            while (rs.next()) {
                File file = new File("src//main//resources//com//piece//v1//Images//" + rs.getString(1));
                String imagePath = new File(file.getPath()).toURI().toString();
                Image image = new Image(imagePath);
                songIcon.setImage(image);
                songName.setText(rs.getString(2));
                System.out.println(rs.getString(2));
            }
        }
    }

    public void handlePlayButton() throws SQLException {
        ResultSet resultSet = Utilities.connection.createStatement().executeQuery("SELECT song_file FROM songs WHERE song_id =" + musicId + ";");
        if (!mediaPlayersMap.containsKey(musicId)) {
            while (resultSet.next()) {
                File file = new File("src//main//resources//com//piece//v1//Songs//" + resultSet.getString(1));
                String song = new File(file.getPath()).toURI().toString();
                Media media = new Media(song);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                if (mediaPlayer.getStatus().equals(MediaPlayer.Status.UNKNOWN)) {
                    File trial = new File("src//main//resources//com//piece//v1//Images//Play.png");
                    String imagePath = new File(trial.getPath()).toURI().toString();
                    Image image = new Image(imagePath);
                    playPauseImage.setImage(image);
                }
                mediaPlayersMap.put(musicId, mediaPlayer);
                playPauseLogic();
            }
        } else {
            playPauseLogic();
        }
    }

    private void bindTimeStamp() {
        timeStamp.setText(getTimeString(mediaPlayersMap.get(musicId).getCurrentTime().toMillis()) + "/" + getTimeString(mediaPlayersMap.get(musicId).getTotalDuration().toMillis()));
        slider.maxProperty().bind(Bindings.createDoubleBinding(
                () -> mediaPlayersMap.get(musicId).getTotalDuration().toSeconds(),
                mediaPlayersMap.get(musicId).totalDurationProperty()));
    }

    private void resetPlayers() {
        for (Integer songNo : mediaPlayersMap.keySet()) {
            mediaPlayersMap.get(songNo).stop();
            mediaPlayersMap.get(songNo).setStartTime(mediaPlayersMap.get(songNo).getStartTime());
        }
    }

    public void handleLikeButton() throws SQLException {
        ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT * FROM social WHERE music_id=" + musicId + " AND user_id=" + Utilities.userId + ";");
        if (!rs.next()) {
            String sql = "INSERT INTO social (music_id, user_id)" + "VALUES (?,?)";
            PreparedStatement preparedStatement = Utilities.connection.prepareStatement(sql);
            preparedStatement.setInt(1, musicId);
            preparedStatement.setInt(2, Utilities.userId);
            preparedStatement.executeUpdate();
            namesMap.get(musicId).add(Utilities.Name);
            likedOrNot();
            updateLabel();
            suggestFriends();
        } else {
            likeButton.setText("Like");
            String sql = "DELETE FROM social WHERE music_id = " + "'" + musicId + "'" + "AND user_id = " + "'" + Utilities.userId + "'";
            PreparedStatement preparedStatement = Utilities.connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            namesMap.get(musicId).remove(Utilities.Name);
            likedOrNot();
            updateLabel();
            suggestionLabel.setText("");
        }
    }

    public void updateLabel() {
        likedByNames = "";
        for (String name : namesMap.get(musicId)) {
            if (name.equals(Utilities.Name)) {
                name = "Me";
            }
            likedByNames = likedByNames + name + ", ";
        }
        likedByLabel.setText(likedByNames);
    }

    public void suggestFriends() {
        String namesOfLikes = "You may want to follow:";
        String[] substrings = likedByNames.split(", ");
        for (String string : substrings) {
            if (!string.equals("Me"))
                namesOfLikes = namesOfLikes + "\n" + string;
        }
        if (namesOfLikes.equals("You may want to follow:")) {
            suggestionLabel.setText("Only you like this song :)");
        } else {
            suggestionLabel.setText(namesOfLikes);
        }
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet resultSet = Utilities.connection.createStatement().executeQuery("SELECT song_id, song_file FROM songs;");
        while (resultSet.next()) {
            File file = new File("src//main//resources//com//piece//v1//Songs//" + resultSet.getString(2));
            String song = new File(file.getPath()).toURI().toString();
            Media media = new Media(song);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayersMap.put(resultSet.getInt(1), mediaPlayer);
        }

        mediaPlayersMap.get(musicId).currentTimeProperty().addListener(ov -> {
            double total = mediaPlayersMap.get(musicId).getTotalDuration().toMillis();
            double current = mediaPlayersMap.get(musicId).getCurrentTime().toMillis();
            timeStamp.setText(getTimeString(current) + "/" + getTimeString(total));
        });

        slider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            mediaPlayersMap.get(musicId).seek(Duration.seconds(newValue.doubleValue()));
            double total = mediaPlayersMap.get(musicId).getTotalDuration().toMillis();
            double current = mediaPlayersMap.get(musicId).getCurrentTime().toMillis();
            timeStamp.setText(getTimeString(current) + "/" + getTimeString(total));
        });

        ResultSet countOfSongs = Utilities.connection.createStatement().executeQuery("SELECT COUNT(DISTINCT song_id) FROM songs;");
        while (countOfSongs.next()) {
            Integer songsCount = countOfSongs.getInt(1);
            for (Integer i = 1; i <= songsCount; i++) {
                ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT user_name FROM users WHERE user_id IN (SELECT user_id FROM social WHERE music_id=" + i + ");");
                ArrayList<String> names = new ArrayList<>();
                while (rs.next()) {
                    names.add(rs.getString(1));
                }
                namesMap.put(i, names);
            }
        }
        updateLabel();
        likedOrNot();
    }

}
