package elice.eliceauction.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor //모든 컬럼 생성자 생성
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //회원아이디

    @Column(nullable = false)
    private String username; //회원 이름

    @Column(nullable = false, unique = true)
    private String email; //회원 이메일

    @Column(nullable = false)
    private String password; //회원 비밀번호

    @Enumerated(EnumType.STRING)
    private UserGrade grade;  //회원 등급

    @Column
    private String address;  //회원 주소



}
