package elice.eliceauction.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Table(name = "Member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; //primary Key

    @Column(nullable = false, length = 30, unique = true)
    private String username;//아이디

    private String password;//비밀번호


    @Column(nullable = false, length = 30)
    private String name;//이름(실명)


    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 30)
    private Role role;//권한 -> USER, ADMIN


    @Column(length = 1000)
    private String refreshToken;//RefreshToken

    @Column(nullable = true, length = 100)
    private String email; // 이메일

    @Embedded
    private Address address; // 주소

    @Column(nullable = true, length = 20)
    private String phoneNumber; // 전화번호








    //== 정보 수정 ==//
    public void updatePassword(PasswordEncoder passwordEncoder, String password){
        this.password = passwordEncoder.encode(password);
    }

    public void updateName(String name){
        this.name = name;
    }


    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public void destroyRefreshToken(){
        this.refreshToken = null;
    }



    //비밀번호 변경, 회원 탈퇴 시, 비밀번호를 확인하며, 이때 비밀번호의 일치여부를 판단하는 메서드입니다.
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword){
        return passwordEncoder.matches(checkPassword, getPassword());
    }


    //회원가입시, USER의 권한을 부여하는 메서드입니다.
    public void addUserAuthority() {
        this.role = Role.USER;
    }



    //== 패스워드 암호화 ==//
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public void addAdminAuthority() {
        this.role = Role.ADMIN;
    }

    public void updateContactInfo(Address address, String phoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

}