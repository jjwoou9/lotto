package evengom.lotto.service;

import evengom.lotto.model.LottoDto;

import java.util.List;

public interface LottoService {

    List<LottoDto> selectAll();

    List<LottoDto> selectLottoList(int count);

    LottoDto selectOne(int round);
}
