package URFU_Music.repository;

import URFU_Music.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findBySingerStartingWithOrTrackNameStartingWithOrAlbumStartingWith(String singer, String track, String album);

}