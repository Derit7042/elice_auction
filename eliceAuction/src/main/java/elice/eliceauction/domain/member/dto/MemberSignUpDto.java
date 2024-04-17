package elice.eliceauction.domain.member.dto;

import elice.eliceauction.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberSignUpDto(@NotBlank(message = "아이디를 입력해주세요") @Size(min = 2, max = 25, message = "아이디는 2~25자 내외로 입력해주세요")
                              String username,

                              @NotBlank(message = "비밀번호를 입력해주세요")
                              @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{2,30}$",
                                      message = "비밀번호는 2~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
                              String password,

                              @NotBlank(message = "이름을 입력해주세요") @Size(min=2, message = "사용자 이름이 너무 짧습니다.")
                              @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 입력해주세요.")
                              String name) {

    public Member toEntity() {
        return Member.builder().username(username).password(password).name(name).build();
    }
}
