package URFU_Music.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "MUSIC_FILES")
public class MusicFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String filename;

    @Transient
    public String link;

    @OneToOne(mappedBy = "musicFile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Song song ;
}
