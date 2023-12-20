package URFU_Music.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "MUSIC_FILES")
public class MusicFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(nullable = false)
    public String filename;

    @Transient
    public String link;

    @OneToOne(mappedBy = "musicFile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    private Song song ;
}
