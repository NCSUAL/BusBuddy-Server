package Java2Project.repository;

import Java2Project.domain.BusStop;
import Java2Project.domain.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities,Long> {

    Optional<Facilities> findByBusStop(BusStop busStop);
}
