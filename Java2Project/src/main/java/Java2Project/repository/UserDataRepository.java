package Java2Project.repository;

import Java2Project.domain.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData,Long> {

//    @Modifying  //수정,삭제,삽입 쿼리
//    @Query("UPDATE UserData ud set ud.content = :content WHERE ud.id = :id AND ud.user.username = :username")
//    int updateContentByIdAndUsername(@Param("content") String content,@Param("id") Long id,@Param("username") String username);

    @Query("select ud from UserData ud WHERE ud.user.username = :username")
    List<UserData> findByUsername(@Param("username") String username);
}
