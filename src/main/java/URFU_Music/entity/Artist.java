package URFU_Music.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "artist")
    private List<Song> songs = new ArrayList<>();

    @OneToMany(mappedBy = "album")
    private List<Album> albums = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "artist")
    private List<FavoriteArtists> favoriteArtists = new ArrayList<>();
}
