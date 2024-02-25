package de.thbingen.epro.project.okrservice.constants;

/**
 * This Java code defines an enum called `Privileges` which represents different privileges in the system. 
 * Each privilege has a corresponding name.
 * <hr />
 * <h3>This class does not validate or communicate with the database.</h4>
 * <h4>It is a static final provision of `Privileges`</h3>
 * <hr />
 * @param name the custom name of the privilege, allegedly stored in the database
 */
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
