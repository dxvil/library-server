package bof.mohyla.server.bean;

public enum Role {
    ADMIN(1),
    USER(2);

    private int value;

    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}