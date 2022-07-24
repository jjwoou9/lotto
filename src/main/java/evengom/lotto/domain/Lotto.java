package evengom.lotto.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
@Getter
public class Lotto {

    @Id
    private int round;

    private int winnerCnt;

    private long prizeMoney;

    @Size(max=2)
    private int first;
    @Size(max=2)
    private int second;
    @Size(max=2)
    private int third;
    @Size(max=2)
    private int fourth;
    @Size(max=2)
    private int fifth;
    @Size(max=2)
    private int sixth;
    @Size(max=2)
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
