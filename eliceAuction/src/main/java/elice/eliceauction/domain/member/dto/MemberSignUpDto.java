package elice.eliceauction.domain.member.dto;

import elice.eliceauction.domain.member.entity.Member;

public record MemberSignUpDto(String username, String password, String name,
                              String nickName, Integer age) {

    public Member toEntity() {
        return Member.builder().username(username).password(password).name(name).build();
    }
}
