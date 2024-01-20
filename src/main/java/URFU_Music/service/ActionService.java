package URFU_Music.service;

import URFU_Music.entity.Action;
import URFU_Music.entity.Song;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActionService {
    List<Action> getAll();

    @Transactional
    void save(Action action, Song song);
}
