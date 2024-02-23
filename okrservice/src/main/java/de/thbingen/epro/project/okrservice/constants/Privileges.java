package de.thbingen.epro.project.okrservice.constants;

public enum Privileges {
    CO_READ(Privileges.CO_READ_NAME),
    CO_WRITE(Privileges.CO_WRITE_NAME),
    BUO_READ(Privileges.BUO_READ_NAME),
    BUO_WRITE(Privileges.BUO_WRITE_NAME);

    public static final String CO_READ_NAME = "CO_READ";
    public static final String CO_WRITE_NAME = "CO_WRITE";
    public static final String BUO_READ_NAME = "BUO_READ";
    public static final String BUO_WRITE_NAME = "BUO_WRITE";

    private String name;
    Privileges(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
