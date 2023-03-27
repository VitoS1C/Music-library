package lab_8.controller;

import lab_8.repository.ActionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ActionController {

    @Autowired
    private final ActionRepository actionRepository;

    public ActionController(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }
}
