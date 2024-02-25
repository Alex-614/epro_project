package de.thbingen.epro.project.okrservice.constants;

/**
 * This Java code defines an enum called `KeyResultTypes` which represents different types in the system. 
 * Each type has a corresponding name.
 * <hr />
 * <h3>This class does not validate or communicate with the database.</h4>
 * <h4>It is a static final provision of `KeyResultTypes`</h3>
 * <hr />
 * @param name the custom name of the type, allegedly stored in the database
 */
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
