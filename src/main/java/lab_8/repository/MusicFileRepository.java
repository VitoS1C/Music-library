package lab_8.repository;

import lab_8.entity.MusicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicFileRepository extends JpaRepository<MusicFile, Long> {
}
