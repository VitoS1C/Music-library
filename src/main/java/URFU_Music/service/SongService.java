package URFU_Music.service;

import URFU_Music.entity.Song;
import URFU_Music.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getAll() {
        return songRepository.findAll();
    }

    @Transactional
    public void save(Song song) {
        songRepository.save(song);
    }

    public Optional<Song> findById(Long songId) {
        return songRepository.findById(songId);
    }

    @Transactional
    public void deleteById(Long songId) {
        songRepository.deleteById(songId);
    }
}
