package dusk.command;

public enum FlagType {
    BY,
    FROM,
    TO;

    public static FlagType fromString(String flag) {
        try {
            return FlagType.valueOf(flag.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
