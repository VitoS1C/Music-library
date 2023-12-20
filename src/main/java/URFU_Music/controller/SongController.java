package URFU_Music.controller;

import URFU_Music.entity.Action;
import URFU_Music.entity.MusicFile;
import URFU_Music.entity.Song;
import URFU_Music.entity.User;
import URFU_Music.repository.MusicFileRepository;
import URFU_Music.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class SongController {

    private final ActionService actionService;
    private final StorageService storageService;
    private final MusicFileService musicFileService;
    private final UserServiceImpl userService;
    private final SongService songService;

    @Autowired
    public SongController(ActionService actionService, StorageService storageService,
                          MusicFileService musicFileService, UserServiceImpl userService, SongService songService) {
        this.actionService = actionService;
        this.storageService = storageService;
        this.musicFileService = musicFileService;
        this.userService = userService;
        this.songService = songService;
    }

    @GetMapping("/list")
    public ModelAndView getAllSongs() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-songs");
        User currentUser = userService.findCurrentUser();
        Path path;
        List<Song> songs = songService.getAll().stream()
                .filter(song -> song.getUser().getId().equals(currentUser.getId())).collect(Collectors.toList());

        for (Song song : songs) {
            path = storageService.load(song.getMusicFile().getFilename());
            song.getMusicFile().setLink(MvcUriComponentsBuilder.fromMethodName(SongController.class,
                    "serveFile", path.getFileName().toString()).build().toUri().toString());
        }
        mav.addObject("songs", songs);
        return mav;
    }

    @GetMapping("/actionList")
    public ModelAndView getAllActions() {
        log.info("/actionList-> connection");
        User currentUser = userService.findCurrentUser();
        ModelAndView mav = new ModelAndView("list-actions");
        mav.addObject("actions", actionService.getAll().stream().filter(action -> action
                .getUser().getId().equals(currentUser.getId())).collect(Collectors.toList()));
        return mav;
    }

    @PostMapping("/showUpdateForm?{songId}")
    public String updateSong(@ModelAttribute Action action, @PathVariable Long songId) {
        Optional<Song> songOptional = songService.findById(songId);
        Song song = null;
        if (songOptional.isPresent()){
            song = songOptional.get();
        }
        songService.save(song);
        actionService.save(action);
        return "redirect:/list";
    }

    @PostMapping("/saveSong")
    public String saveSong(@RequestParam("file") MultipartFile file, @ModelAttribute Song song,
                           @ModelAttribute Action action) {
        User currentUser = userService.findCurrentUser();
        MusicFile musicFile = new MusicFile();
        musicFile.setFilename(storageService.store(file));
        song.setMusicFile(musicFile);
        song.setUser(currentUser);
        action.setUser(currentUser);
        musicFile.setSong(song);
        action.setDateActions(getTime());
        musicFileService.save(musicFile);
        userService.update(currentUser);
        songService.save(song);
        actionService.save(action);
        return "redirect:/list";
    }

    @GetMapping("/addSongForm")
    public ModelAndView addSongsForm() {
        ModelAndView mav = new ModelAndView("add-song-form");
        Song song = new Song();
        Action action = new Action(getTime());
        mav.addObject(song);
        mav.addObject(action);
        return mav;
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long songId) {
        log.info("show form connect");
        ModelAndView mav = new ModelAndView("add-song-form-for-update");
        Optional<Song> optionalSong = songService.findById(songId);
        Song song = new Song();
        Action action = new Action();
        if (optionalSong.isPresent()) {
            song = optionalSong.get();
        }
        mav.addObject("action", action);
        mav.addObject("song", song);
        return mav;
    }

    @PostMapping("/updateSong")
    public String  updateSong(@ModelAttribute Song song) {
        Action action = new Action(getTime());
        User currentUser = userService.findCurrentUser();
        action.setDescription("Изменена композиция " + "\"" + song.getSinger() + "\" - " + song.getTrackName());
        action.setUser(currentUser);
        song.setUser(currentUser);
        songService.save(song);
        actionService.save(action);
        return "redirect:/list";
    }

    @GetMapping("/deleteSong")
    public String deleteSong(@RequestParam Long songId) {
        User currentUser = userService.findCurrentUser();
        Action action = new Action(getTime());
        action.setUser(currentUser);
        Optional<Song> optionalSong = songService.findById(songId);
        Song song = new Song();
        if (optionalSong.isPresent()) {
            song = optionalSong.get();
        }
        action.setDescription("Удалена композиция " + "\"" + song.getSinger() + "\" - "
                + "\"" + song.getTrackName() + "\"");
        actionService.save(action);
        storageService.deleteFile(song.getMusicFile().filename);
        songService.deleteById(songId);
        return "redirect:/list";
    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("dd-MM-yyyy HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }

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
