package evengom.lotto.model;

import lombok.Data;

import java.util.List;

@Data
public class NumberDto {

    private int number;

    private int duplication;

    private List<Integer> rounds;

    public NumberDto(int number, int duplication, List<Integer> rounds) {
        this.number = number;
        this.duplication = duplication;
        this.rounds = rounds;
    }

    public NumberDto(int number, int duplication) {
        this.number = number;
        this.duplication = duplication;
    }
}
