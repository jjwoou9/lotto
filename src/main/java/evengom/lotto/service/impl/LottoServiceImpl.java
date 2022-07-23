package evengom.lotto.service.impl;

import evengom.lotto.domain.Lotto;
import evengom.lotto.model.LottoDto;
import evengom.lotto.model.NumberDto;
import evengom.lotto.repository.LottoRepository;
import evengom.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @Override
    public List<Integer> selectMostFrequentNumbers() {
        /*
            1,2,3,4,5,6,bonus column만 select 한뒤
            한개의 list로
         */

        List<Lotto> lottoList = lottoRepository.findAll();
//        List<Lotto> lottoList = lottoRepository.findLatest(5);
        List<Integer> numberList = new ArrayList<>();

        List<NumberDto> dFirstList = lottoList.stream().map(lotto -> new NumberDto(lotto.getFirst())).collect(Collectors.toList());

        for (Lotto lotto : lottoList){
            numberList.add(lotto.getFirst());
            numberList.add(lotto.getSecond());
            numberList.add(lotto.getThird());
            numberList.add(lotto.getFourth());
            numberList.add(lotto.getFifth());
            numberList.add(lotto.getSixth());
            numberList.add(lotto.getBounus());
        }

        Map<Integer, Integer> map = numberList.stream().collect(Collectors.toMap(Function.identity(), value -> 1, Integer::sum));
        System.out.println(map);


        return numberList;
    }

}
