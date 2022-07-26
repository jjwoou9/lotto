package evengom.lotto.model;

import lombok.Data;

import java.util.List;

@Data
public class ConsecutiveDto {

    int seq;

    int conNum;

    int count;

    List<Integer> rounds;

    public ConsecutiveDto(int seq, int conNum, int count, List<Integer> rounds) {
        this.seq = seq;
        this.conNum = conNum;
        this.count = count;
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        return "ConsecutiveDto{" +
                "conNum=" + conNum +
                ", count=" + count +
                ", rounds=" + rounds +
                '}';
    }
}
