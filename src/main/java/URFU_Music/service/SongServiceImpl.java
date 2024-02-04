package URFU_Music.service;

import URFU_Music.controller.SongController;
import URFU_Music.dto.SongsResponse;
import URFU_Music.entity.MusicFile;
import URFU_Music.entity.Song;
import URFU_Music.entity.User;
import URFU_Music.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final UserService userService;
    private final StorageService storageService;

    @Autowired
    public SongServiceImpl(SongRepository songRepository, UserService userService, StorageService storageService) {
        this.songRepository = songRepository;
        this.userService = userService;
        this.storageService = storageService;
    }

    @Override
    public SongsResponse getAll(Integer page, Integer songsPerPage, boolean withLinks) {
        SongsResponse songsResponse = new SongsResponse();

        if (withLinks) {
            songsResponse.setSongs(songRepository.findAll(PageRequest.of(page, songsPerPage)).getContent().stream().peek(
                    song -> song.getMusicFile()
                            .setLink(MvcUriComponentsBuilder.fromMethodName(SongController.class,
                                            "serveFile", storageService.load(song.getMusicFile().getFilename())
                                                    .getFileName().toString())
                                    .build().toUri().toString())).collect(Collectors.toList())
            );
        } else {
            songsResponse.setSongs(songRepository.findAll(PageRequest.of(page, songsPerPage)).getContent());
        }

        songsResponse.setPages(songRepository.findAll(PageRequest.of(page, songsPerPage)).getTotalPages());
        songsResponse.setElements(songRepository.findAll(PageRequest.of(page, songsPerPage)).getNumberOfElements());
        return songsResponse;
    }

    @Override
    public List<Song> getUsersSongs() {
        User currentUser = userService.findCurrentUser();
        return currentUser.getSongs().stream().peek(song -> song.getMusicFile()
                        .setLink(MvcUriComponentsBuilder.fromMethodName(SongController.class,
                        "serveFile", storageService.load(song.getMusicFile().getFilename())
                                .getFileName().toString())
                .build().toUri().toString()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Song song, MultipartFile file) {
        MusicFile musicFile = new MusicFile();
        musicFile.setFilename(file.getOriginalFilename());
        song.setMusicFile(musicFile);
        songRepository.save(song);
    }

    @Override
    public Optional<Song> findById(Long songId) {
        return songRepository.findById(songId);
    }

    @Override
    @Transactional
    public void deleteById(Long songId) {
        songRepository.deleteById(songId);
    }

    @Transactional
    @Override
    public void removeFromList(long id) {
        User currentUser = userService.findCurrentUser();
        Song song = songRepository.findById(id).orElse(null);
        currentUser.getSongs().remove(song);
    }

    @Override
    @Transactional
    public void addToList(long id) {
        User currentUser = userService.findCurrentUser();
        Song song = songRepository.findById(id).orElse(null);
        currentUser.getSongs().add(song);
    }

    @Override
    public SongsResponse findTrack(String query, boolean withLinks) {
        if (withLinks) {
            List<Song> songs = songRepository
                    .findBySingerStartingWithOrTrackNameStartingWithOrAlbumStartingWith(query, query, query).stream()
                    .peek(song -> song.getMusicFile().setLink(MvcUriComponentsBuilder.fromMethodName(SongController.class,
                                    "serveFile", storageService.load(song.getMusicFile().getFilename())
                                            .getFileName().toString())
                            .build().toUri().toString())).collect(Collectors.toList());

            return new SongsResponse(songs, 1, songs.size());
        } else {
            List<Song> songs = songRepository
                    .findBySingerStartingWithOrTrackNameStartingWithOrAlbumStartingWith(query, query, query);

            return new SongsResponse(songs, 1, songs.size());
        }
    }

    @Override
    @Transactional
    public void update(Song song) {
        songRepository.findById(song.getId()).ifPresent(existingSong -> song.setMusicFile(existingSong.getMusicFile()));
        songRepository.save(song);
    }
}
