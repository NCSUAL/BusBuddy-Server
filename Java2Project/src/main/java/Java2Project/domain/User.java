package Java2Project.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "AppUser")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    @CreationTimestamp
    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UserData> userdatas = new ArrayList<>();

    @Builder
    public User(String username){
        this.username = username;
    }

    protected User(){}

    public void addUserData(UserData userData){
        userdatas.add(userData);
        userData.setUser(this);
    }
}
