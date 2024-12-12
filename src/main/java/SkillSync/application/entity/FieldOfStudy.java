package SkillSync.application.entity;

public enum FieldOfStudy {
    COMPUTER_SCIENCE,
    MECHANICAL_ENGINEERING,
    BUSINESS,
    ELECTRICAL_ENGINEERING,
    BIOLOGY,
    PHYSICS;

    public String getFormattedName() {
        // Replace underscores with spaces and capitalize the first letter of each word
        return this.name().replace("_", " ")
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .toLowerCase()
                .replaceFirst(".", String.valueOf(this.name().charAt(0)).toUpperCase()); // Capitalize first letter
    }
}


