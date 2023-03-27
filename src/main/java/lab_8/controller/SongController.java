package lab_8.controller;

import lab_8.entity.Action;
import lab_8.entity.Song;
import lab_8.repository.ActionRepository;
import lab_8.repository.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Controller
public class SongController {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private ActionRepository actionRepository;

    @GetMapping("/list")
    public ModelAndView getAllSongs() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-songs");
        mav.addObject("songs", songRepository.findAll());
        return mav;
    }

    @GetMapping("/actionList")
    public ModelAndView getAllActions() {
        log.info("/actionList-> connection");
        ModelAndView mav = new ModelAndView("list-actions");
        mav.addObject("actions", actionRepository.findAll());
        return mav;
    }



    @PostMapping("/saveSong")
    public String saveSong(@ModelAttribute Song song, @ModelAttribute Action action) {
        ModelAndView modelAndView = new ModelAndView();
        songRepository.save(song);
        actionRepository.save(action);
        return "redirect:/list";
    }

    @GetMapping("/addSongForm")
    public ModelAndView addSongsForm(){
        songRepository.findAll();
        actionRepository.findAll();
        ModelAndView mav = new ModelAndView("add-song-form");
        Song song = new Song();
        Action action = new Action(getTime(), "Добавлена новая запись песни " + song.getSinger());
        mav.addObject(song);
        mav.addObject(action);
        return mav;
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long songId, @RequestParam Long actionId) {
        ModelAndView mav = new ModelAndView("add-song-form");
        Optional<Song> optionalSong = songRepository.findById(songId);
        Optional<Action> optionalAction = actionRepository.findById(actionId);
        Song song = new Song();
        Action action = new Action();
        if (optionalSong.isPresent()) {
            song = optionalSong.get();
        }
        if (optionalAction.isPresent()) {
            action = optionalAction.get();
        }
        mav.addObject("song", song);
        mav.addObject("action", action);
        return mav;
    }

    @GetMapping("/deleteSong")
    public String deleteSong(@RequestParam Long songId) {
        songRepository.deleteById(songId);
        return "redirect:/list";
    }

    private String getTime()  {
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
