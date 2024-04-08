package elice.eliceauction.domain.user.exception;

public class UserAlreadyExistException extends RuntimeException {
    //회원가입 로직에서 이미 존재하는 사용자(예를 들어, 중복된 이메일 주소)로 회원가입을 시도할 때 예외처리
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
