package de.thbingen.epro.project.okrservice;

public enum KeyResultTypes {
    NUMERIC(KeyResultTypes.NUMERIC_NAME),
    PERCENTUAL(KeyResultTypes.PERCENTUAL_NAME),
    BINARY(KeyResultTypes.BINARY_NAME);

    public static final String NUMERIC_NAME = "numeric";
    public static final String PERCENTUAL_NAME = "percentual";
    public static final String BINARY_NAME = "binary";

    private String name;
    KeyResultTypes(String name) {
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
