package de.thbingen.epro.project.okrservice;

public enum Privileges {
    CO_READ("CO_READ"),
    CO_WRITE("CO_WRITE"),
    BUO_READ("BUO_READ"),
    BUO_WRITE("BUO_WRITE");

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
