package URFU_Music.service;

import URFU_Music.entity.Action;
import URFU_Music.entity.Song;
import URFU_Music.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class ActionServiceImpl implements ActionService{
    private final UserService userService;
    private final ActionRepository actionRepository;

    @Autowired
    public ActionServiceImpl(UserService userService, ActionRepository repository) {
        this.userService = userService;
        this.actionRepository = repository;
    }

    @Override
    public List<Action> getAll(){
        return actionRepository.findAll();
    }

    @Transactional
    @Override
    public void save(Action action, Song song) {
        action.setDescription("Добавлена новая композиция: " + song.getSinger() + " - " + song.getTrackName());
        action.setUser(userService.findCurrentUser());
        actionRepository.save(action);
    }
}
