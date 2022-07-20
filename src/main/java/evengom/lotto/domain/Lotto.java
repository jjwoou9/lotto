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

    private int first;

    private int second;

    private int third;

    private int fourth;

    private int fifth;

    private  int sixth;

    private  int bounus;
}
