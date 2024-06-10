package URFU_Music.service;

import URFU_Music.dto.SongsResponse;
import URFU_Music.entity.Song;
import URFU_Music.entity.User;
import URFU_Music.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor()
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final UserService userService;
    private final StorageService storageService;

    @Override
    public SongsResponse getAll(Integer page, Integer songsPerPage) {
        SongsResponse songsResponse = new SongsResponse();
        songsResponse.setSongs(songRepository.findAll(PageRequest.of(page, songsPerPage)).getContent());
        songsResponse.setPages(songRepository.findAll(PageRequest.of(page, songsPerPage)).getTotalPages());
        songsResponse.setElements(songRepository.findAll(PageRequest.of(page, songsPerPage)).getNumberOfElements());
        return songsResponse;
    }

    @Override
    public List<Song> getUsersSongs() {
        User currentUser = userService.findCurrentUser();
        return currentUser.getSongs();
    }

    @Override
    @Transactional
    public void save(Song song, MultipartFile file) {
        song.setFileName(file.getOriginalFilename());
        songRepository.save(song);
    }

    @Override
    public Optional<Song> findById(Long songId) {
        return songRepository.findById(songId);
    }

//    @Override
//    @Transactional
//    public void deleteById(Long songId) {
//        songRepository.deleteById(songId);
//    }

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
    public SongsResponse findTrack(String query) {
        List<Song> songs = songRepository
                    .findBySingerStartingWithOrTrackNameStartingWithOrAlbumStartingWith(query, query, query);

        return new SongsResponse(songs, 1, songs.size());

    }

    @Override
    @Transactional
    public void update(Song song) {
        songRepository.findById(song.getId()).ifPresent(existingSong -> song.setFileName(existingSong.getFileName()));
        songRepository.save(song);
    }

    @Override
    public ResponseEntity<Resource> getMusicFile(String filename) {
        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                STR."attachment; filename=\"\{file.getFilename()}\"").body(file);
    }
}
