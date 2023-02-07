package bof.mohyla.server.bean;

public enum Role {
    ADMIN("admin"),
    USER("user");

    public String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        if(value != null) {
            return value;
        }
        return null;
    }

    public static boolean isExist(String name) {
        for (Role role : values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}