package evengom.lotto.model;

import evengom.lotto.domain.Lotto;
import lombok.Data;

@Data
public class LottoDto {

    private int round;
    private int winnerCnt;
    private long prizeMoney;
    private int first;
    private int second;
    private int third;
    private int fourth;
    private int fifth;
    private  int sixth;
    private  int bonus;

    public LottoDto(Lotto lotto) {
        this.round = lotto.getRound();
        this.winnerCnt = lotto.getWinnerCnt();
        this.prizeMoney = lotto.getPrizeMoney();
        this.first = lotto.getFirst();
        this.second = lotto.getSecond();
        this.third = lotto.getThird();
        this.fourth = lotto.getFourth();
        this.fifth = lotto.getFifth();
        this.sixth = lotto.getSixth();
        this.bonus = lotto.getBonus();
    }

}
