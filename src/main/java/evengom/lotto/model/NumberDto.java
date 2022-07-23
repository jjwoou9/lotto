package evengom.lotto.model;

import lombok.Data;

import java.util.List;

@Data
public class NumberDto {

    private int number;

    private int duplication;

    private List<Integer> rounds;

    public NumberDto(int number) {
        this.number = number;
    }
}
