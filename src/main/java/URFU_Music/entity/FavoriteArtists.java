package URFU_Music.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "favorite_artists")
public class FavoriteArtists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public void setUser(User user) {
        this.user = user;
        this.user.getFavoriteArtists().add(this);
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
        this.artist.getFavoriteArtists().add(this);
    }
}
