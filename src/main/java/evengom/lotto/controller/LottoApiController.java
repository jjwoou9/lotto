package evengom.lotto.controller;

import evengom.lotto.domain.Lotto;
import evengom.lotto.model.LottoDto;
import evengom.lotto.model.NumberDto;
import evengom.lotto.repository.LottoRepository;
import evengom.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = {"/api/lotto"})
public class LottoApiController {

    private final LottoService lottoService;

    @GetMapping("/")
    public List<LottoDto> getAllLottoList() {
        log.info("get api/all {}");
        return lottoService.selectAll();
    }

    @GetMapping("/list")
    public List<LottoDto> getLottoList(@RequestParam("count") int count) {
        log.info("get api/list  param : " + count);
        return lottoService.selectLottoList(count);
    }

    @GetMapping("/one")
    public LottoDto getOne(@RequestParam("round") int round) {
        return lottoService.selectOne(round);
    }

    @GetMapping("/mostFrequent")
    public List<NumberDto> getMostFrequentNumbers() {
        List<NumberDto> mostNumbers = lottoService.selectMostFrequentNumbers();
        return mostNumbers;
    }

    @GetMapping("/consecutive")
    public List<LottoDto> getConsecutive(){
        List<LottoDto> consecutiveList = lottoService.selectConsecutiveList();

        return null;
    }

    @PostMapping("/")
    public String insert(@Valid @RequestBody Lotto lotto) {
        log.info("insert Params {} ", lotto.toString());

        lottoService.insert(lotto);

        return "success";
    }


    /*
     *  역대 가장 많이 뽑힌 번호.
     *  1번중에 많이 뽑힌 ~보너스로 많이 뽑힌
     *
     *  계산용 모델 생성. num, 중복횟수, 회차List
     *
     *   연속된 회차에서 겹치는 번호 - 있는 경우 ? 번호
     *
     *  대충 위에 로직 합쳐서 예상 번호 ?
     * */


}
