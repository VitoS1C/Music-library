package lab_8.entity;

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

    @Column
    public String link;

    @OneToOne(mappedBy = "musicFile", cascade = CascadeType.ALL)
    private Song song ;
}
