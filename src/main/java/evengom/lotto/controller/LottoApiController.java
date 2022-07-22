package evengom.lotto.controller;

import evengom.lotto.domain.Lotto;
import evengom.lotto.model.LottoDto;
import evengom.lotto.repository.LottoRepository;
import evengom.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = {"/api/lotto"})
public class LottoApiController {

    private final LottoService lottoService;

    @GetMapping("/all")
    public List<LottoDto> getAllLottoList() {
        return lottoService.selectAll();
    }

    @GetMapping("/list")
    public List<LottoDto> getLottoList(@RequestParam("count") int count) {
        return lottoService.selectLottoList(count);
    }

    @GetMapping("/one")
    public LottoDto getOne(@RequestParam("round") int round) {
        return lottoService.selectOne(round);
    }





}
