package URFU_Music.controller;

import URFU_Music.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
    @PatchMapping()
    public String removeFromList(@RequestParam long id) {
        songService.removeFromList(id);
        return "redirect:/";
    }

    @PostMapping()
    public String addToList(@RequestParam long id) {
        songService.addToList(id);
        return "redirect:/";
    }

//    @GetMapping("/actionList")
//    public ModelAndView getAllActions() {
//        log.info("/actionList-> connection");
//        User currentUser = userService.findCurrentUser();
//        ModelAndView mav = new ModelAndView("songs/list-actions");
//        mav.addObject("actions", actionService.getAll().stream().filter(action -> action
//                .getUser().getId().equals(currentUser.getId())).collect(Collectors.toList()));
//        mav.addObject("auth",userService.getAuthentication());
//        return mav;
//    }
//
//    @PostMapping("/showUpdateForm?{songId}")
//    public String updateSong(@ModelAttribute Action action, @PathVariable Long songId) {
//        Optional<Song> songOptional = songService.findById(songId);
//        Song song = null;
//        if (songOptional.isPresent()) {
//            song = songOptional.get();
//        }
//        songService.save(song);
//        actionService.save(action);
//        return "redirect:/list";
//    }
//
//
//

//
//    @GetMapping("/showUpdateForm")
//    public ModelAndView showUpdateForm(@RequestParam Long songId) {
//        log.info("show form connect");
//        ModelAndView mav = new ModelAndView("add-song-form-for-update");
//        Optional<Song> optionalSong = songService.findById(songId);
//        Song song = new Song();
//        Action action = new Action();
//        if (optionalSong.isPresent()) {
//            song = optionalSong.get();
//        }
//        mav.addObject("action", action);
//        mav.addObject("song", song);
//        mav.addObject("auth",userService.getAuthentication());
//        return mav;
//    }
//
//    @PostMapping("/updateSong")
//    public String updateSong(@ModelAttribute Song song) {
//        Action action = new Action(getTime());
//        User currentUser = userService.findCurrentUser();
//        action.setDescription("Изменена композиция " + "\"" + song.getSinger() + "\" - " + song.getTrackName());
//        action.setUser(currentUser);
//        song.setUsers(currentUser);
//        songService.save(song);
//        actionService.save(action);
//        return "redirect:/list";
//    }
//
//    @GetMapping("/deleteSong")
//    public String deleteSong(@RequestParam Long songId) {
//        User currentUser = userService.findCurrentUser();
//        Action action = new Action(getTime());
//        action.setUser(currentUser);
//        Optional<Song> optionalSong = songService.findById(songId);
//        Song song = new Song();
//        if (optionalSong.isPresent()) {
//            song = optionalSong.get();
//        }
//        action.setDescription("Удалена композиция " + "\"" + song.getSinger() + "\" - "
//                + "\"" + song.getTrackName() + "\"");
//        actionService.save(action);
//        storageService.deleteFile(song.getMusicFile().filename);
//        songService.deleteById(songId);
//        return "redirect:/list";
//    }
//


    @GetMapping("/music/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
