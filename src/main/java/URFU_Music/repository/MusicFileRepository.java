package URFU_Music.repository;

import URFU_Music.entity.MusicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicFileRepository extends JpaRepository<MusicFile, Long> {
}
