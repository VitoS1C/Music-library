package URFU_Music.controller;

import URFU_Music.entity.Action;
import URFU_Music.entity.MusicFile;
import URFU_Music.entity.Song;
import URFU_Music.repository.ActionRepository;
import URFU_Music.repository.MusicFileRepository;
import URFU_Music.repository.SongRepository;
import URFU_Music.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class SongController {

    private final SongRepository songRepository;
    private final ActionRepository actionRepository;
    private final StorageService storageService;
    private final MusicFileRepository musicFileRepository;
    @Autowired
    public SongController(SongRepository songRepository, ActionRepository actionRepository, StorageService storageService,
                          MusicFileRepository musicFileRepository) {
        this.songRepository = songRepository;
        this.actionRepository = actionRepository;
        this.storageService = storageService;
        this.musicFileRepository = musicFileRepository;
    }

    @GetMapping("/list")
    @Transactional
    public ModelAndView getAllSongs() {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("list-songs");
        List<Song> songs = songRepository.findAll();
        mav.addObject("songs", songs);
        return mav;
    }

    @GetMapping("/actionList")
    public ModelAndView getAllActions() {
        log.info("/actionList-> connection");
        ModelAndView mav = new ModelAndView("list-actions");
        mav.addObject("actions", actionRepository.findAll());
        return mav;
    }

    @PostMapping("/showUpdateForm?{songId}")
    public String updateSong(@ModelAttribute Action action, @PathVariable Long songId) {
        Optional<Song> songOptional = songRepository.findById(songId);
        Song song = null;
        if (songOptional.isPresent()){
            song = songOptional.get();
        }
        songRepository.save(song);
        actionRepository.save(action);
        return "redirect:/list";
    }

    @PostMapping("/saveSong")
    public String saveSong(@RequestParam("file") MultipartFile file, @ModelAttribute Song song,
                           @ModelAttribute Action action) {
        Path path;
        MusicFile musicFile = new MusicFile();
        musicFile.setFilename(storageService.store(file));

        song.setMusicFile(musicFile);
        musicFile.setSong(song);
        path = storageService.load(song.getMusicFile().getFilename());
        song.getMusicFile().setLink(MvcUriComponentsBuilder.fromMethodName(SongController.class,
                "serveFile", path.getFileName().toString()).build().toUri().toString());//.
        //replaceAll("http://192.168.1.2","http://demo.komputer.keenetic.link"));
        action.setDateActions(getTime());
        musicFileRepository.save(musicFile);
        songRepository.save(song);
        actionRepository.save(action);
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
        Optional<Song> optionalSong = songRepository.findById(songId);
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
        action.setDescription("Изменена композиция " + "\"" + song.getSinger() + "\" - " + song.getTrackName());
        songRepository.save(song);
        actionRepository.save(action);
        return "redirect:/list";
    }

    @GetMapping("/deleteSong")
    public String deleteSong(@RequestParam Long songId) {
        Action action = new Action(getTime());
        Optional<Song> optionalSong = songRepository.findById(songId);
        Song song = new Song();
        if (optionalSong.isPresent()) {
            song = optionalSong.get();
        }
        action.setDescription("Удалена композиция " + "\"" + song.getSinger() + "\" - "
                + "\"" + song.getTrackName() + "\"");
        actionRepository.save(action);
        storageService.deleteFile(song.getMusicFile().filename);
        songRepository.deleteById(songId);
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