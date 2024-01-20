package URFU_Music.service;

import URFU_Music.entity.MusicFile;
import URFU_Music.repository.MusicFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MusicFileServiceImpl implements MusicFileService {
    private final MusicFileRepository musicFileRepository;

    @Autowired
    public MusicFileServiceImpl(MusicFileRepository musicFileRepository) {
        this.musicFileRepository = musicFileRepository;
    }

    @Transactional
    public void save(MusicFile musicFile) {
        musicFileRepository.save(musicFile);
    }
}
