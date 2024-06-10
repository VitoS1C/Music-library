package URFU_Music.controller;

import URFU_Music.service.SongService;
import URFU_Music.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/songs")
public class SongController {

    private final StorageService storageService;
    private final SongService songService;

    @Autowired
    public SongController(StorageService storageService, SongService songService) {
        this.storageService = storageService;
        this.songService = songService;
    }

    @GetMapping()
    public ModelAndView getAllSongs() {
        log.info("/list -> connection: " + SecurityContextHolder.getContext().getAuthentication().getName());
        ModelAndView mav = new ModelAndView("songs/list-songs");
        mav.addObject("songs", songService.getUsersSongs());
        return mav;
    }
    @DeleteMapping("/{referer}")
    public String removeFromList(@RequestParam long id, @PathVariable String referer) {
        songService.removeFromList(id);
        if ("index".equals(referer))
            return "redirect:/";
        else
            return "redirect:/songs";
    }

    @PostMapping()
    public String addToList(@RequestParam long id) {
        songService.addToList(id);
        return "redirect:/";
    }

    @GetMapping("/music/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        return songService.getMusicFile(filename);
    }
}
