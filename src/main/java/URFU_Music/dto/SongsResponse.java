package URFU_Music.dto;

import URFU_Music.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SongsResponse {
    private List<Song> songs;
    private int pages;
    private int elements;
}
