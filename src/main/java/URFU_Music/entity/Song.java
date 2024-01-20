package URFU_Music.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String singer;

    @Column(nullable = false)
    private String trackName;

    @Column(nullable = false)
    private String album;

    //musicFile.setSong(this);
    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private MusicFile musicFile;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

}
