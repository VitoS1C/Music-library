package URFU_Music.service;

import URFU_Music.dto.SongsResponse;
import URFU_Music.entity.Action;
import URFU_Music.entity.Song;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SongService {

    List<Song> getUsersSongs();

    void save(Song song, MultipartFile file);

    Optional<Song> findById(Long songId);

    @Transactional
    void deleteById(Long songId);

    @Transactional
    void removeFromList(long id);

    @Transactional
    void addToList(long id);

    SongsResponse getAll(Integer page, Integer songsPerPage);

    SongsResponse findTrack(String name);
}
