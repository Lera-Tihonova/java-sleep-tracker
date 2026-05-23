package ru.yandex.practicum.sleeptracker.model;

public enum Chronotype {
    OWL("Сова"), LARK("Жаворонок"), DOVE("Голубь");

    private final String russianName;

    Chronotype(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
