package elice.eliceauction.domain.user.entity;

public enum UserGrade {
    REGULAR("일반회원"),
    ADMIN("관리자회원");

    private final String description;

    UserGrade(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
