package lab_8.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SONGS")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_song",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")}
    )
    @Column(nullable = false)
    private List<User> userSongs;

    @Column(nullable = false)
    private String singer;

    @Column(nullable = false)
    private String trackName;

    @Column(nullable = false)
    private String album;

    @Column(nullable = false)
    private String duration;

}
