package project.Java2Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.Java2Project.domain.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
}
