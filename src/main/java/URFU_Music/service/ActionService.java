package URFU_Music.service;

import URFU_Music.entity.Action;
import URFU_Music.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActionService {

    private final ActionRepository actionRepository;

    @Autowired
    public ActionService(ActionRepository repository) {
        this.actionRepository = repository;
    }

    public List<Action> getAll(){
        return actionRepository.findAll();
    }

    @Transactional
    public void save(Action action) {
        actionRepository.save(action);
    }
}
