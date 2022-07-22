package evengom.lotto.service.impl;

import evengom.lotto.domain.Lotto;
import evengom.lotto.model.LottoDto;
import evengom.lotto.repository.LottoRepository;
import evengom.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LottoServiceImpl implements LottoService {

    private final LottoRepository lottoRepository;

    @Override
    public List<LottoDto> selectAll() {
        List<Lotto> lottoList = lottoRepository.findAll();

        return lottoList.stream()
                .map(LottoDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LottoDto> selectLottoList(int count) {
        List<Lotto> lottoList = lottoRepository.findLatest(count);

        return lottoList.stream()
                .map(LottoDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public LottoDto selectOne(int round) {
        return new LottoDto(lottoRepository.finOne(round));
    }
}
