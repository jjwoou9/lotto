package evengom.lotto.service;

import evengom.lotto.domain.Lotto;
import evengom.lotto.model.ConsecutiveDto;
import evengom.lotto.model.LottoDto;
import evengom.lotto.model.NumberDto;

import java.util.List;

public interface LottoService {

    List<LottoDto> selectAll();

    List<LottoDto> selectLottoList(int count);

    LottoDto selectOne(int round);

    List<NumberDto> selectMostFrequentNumbers();

    void insert(Lotto lotto);

    List<ConsecutiveDto> selectConsecutiveList();
}
