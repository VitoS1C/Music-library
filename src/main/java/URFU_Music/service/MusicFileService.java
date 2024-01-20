package URFU_Music.service;

import URFU_Music.entity.MusicFile;
import org.springframework.transaction.annotation.Transactional;

public interface MusicFileService {
    @Transactional
    void save(MusicFile musicFile);
}
