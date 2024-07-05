package URFU_Music.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name ="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<FavoriteSongs> favoriteSongs = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Playlist> playlists = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<FavoriteAlbums> favoriteAlbums = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<FavoriteArtists> favoriteArtists = new ArrayList<>();
}
