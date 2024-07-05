package URFU_Music.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "favorite_songs")
public class FavoriteSongs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setSong(Song song) {
        this.song = song;
        this.song.getFavoriteSongs().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        this.user.getFavoriteSongs().add(this);
    }
}
