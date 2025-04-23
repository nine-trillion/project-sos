package team.project.sos.domain.user.enums;

public enum Grade {
    BASIC("일반"),
    SILVER("실버"),
    VIP("VIP");

    private final String name;

    Grade(String name) {
        this.name = name;
    }
}
