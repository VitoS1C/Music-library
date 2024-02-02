package URFU_Music.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "music_file_id")
    private MusicFile musicFile;

    @ManyToMany(mappedBy = "songs", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    public void setMusicFile(MusicFile musicFile) {
        musicFile.setSong(this);
        this.musicFile = musicFile;
    }

    @Transient
    private MultipartFile file;
}
