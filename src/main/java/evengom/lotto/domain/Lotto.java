package evengom.lotto.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Lotto {

    @Id
    private int round;

    private int winnerCnt;

    private long prizeMoney;

    private int first;

    private int second;

    private int third;

    private int fourth;

    private int fifth;

    private int sixth;

    private int bonus;

    @Override
    public String toString() {
        return "Lotto{" +
                "round=" + round +
                ", prizeMoney=" + prizeMoney +
                ", winnerCnt=" + winnerCnt +
                ", first=" + first +
                ", second=" + second +
                ", third=" + third +
                ", fourth=" + fourth +
                ", fifth=" + fifth +
                ", sixth=" + sixth +
                ", bonus=" + bonus +
                '}';
    }
}
