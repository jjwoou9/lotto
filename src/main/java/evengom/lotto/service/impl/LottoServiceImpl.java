package evengom.lotto.service.impl;

import evengom.lotto.Exception.RoundAlreadyExistException;
import evengom.lotto.domain.Lotto;
import evengom.lotto.model.ConsecutiveDto;
import evengom.lotto.model.LottoDto;
import evengom.lotto.model.NumberDto;
import evengom.lotto.repository.LottoRepository;
import evengom.lotto.service.LottoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LottoServiceImpl implements LottoService {

    private final LottoRepository lottoRepository;
    private static final List<ConsecutiveDto> consList = new ArrayList<>();
    private static int seq = 0;
    private static List<Integer> currList = new ArrayList<>();


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
    public List<NumberDto> selectMostFrequentNumbers() {

        List<Lotto> lottoList = lottoRepository.findAll();
        //로또 추첨된 번호들 1개의 list로
        List<Integer> numberList = transformToIntList(lottoList);
        //번호 모은 List 중복 횟수 추가해서 object로
        List<NumberDto> numberDtoList = transformToNumberDtoList(numberList);

        return numberDtoList;
    }

    @Override
    public List<ConsecutiveDto>  selectConsecutiveList() {
        List<Lotto> lottoList = lottoRepository.findLatest(3);
        List<LottoDto> lottoDtoList = lottoList.stream()
                .map(LottoDto::new)
                .collect(Collectors.toList());

        lottoDtoList.stream().forEach(System.out::println);

        /*
        DB 바뀌면 추가해야됨
        LottoDto(round=1025, winnerCnt=4, prizeMoney=6118853344, first=8, second=9, third=20, fourth=25, fifth=29, sixth=33, bonus=7)
        LottoDto(round=1024, winnerCnt=8, prizeMoney=3020323500, first=9, second=18, third=20, fourth=22, fifth=38, sixth=44, bonus=10)
         */

        /*
         *  list 가져온뒤 integer list로 변환
         *  1, 2번 retain 비교 => 같으면 3번 비교 => 4번 비교 ...
         *  2번부터 출발 2,3비교...
         *
         * 같으면 몇번 연속됐는지?
         * 연속된 번호?
         * */

        for (int i = 0; i < lottoDtoList.size(); i++) {

            compareHaveSameNum(lottoDtoList, i, i+1, false);
        }

        return consList;
    }

    private void compareHaveSameNum(List<LottoDto> lottoDtoList, int currentIdx, int nextIdx, boolean isCon) {
        System.out.println("curr : " + currentIdx + "  next : " + nextIdx );
        // 여기에 && currentIdx == lottoDtoList.size()

        if(nextIdx < lottoDtoList.size()){
            List<Integer> list1 = transformDtoToIntList(lottoDtoList.get(currentIdx));
            List<Integer> list2 = transformDtoToIntList(lottoDtoList.get(nextIdx));

            //수정 needed
            if(isCon){
                list1 = currList;
            }else{
                list1 = transformDtoToIntList(lottoDtoList.get(currentIdx));
                currList = list1;
                seq++;
            }
            currList.retainAll(list2);

            System.out.println(" retain1 " + list1.size());
            System.out.println(" retain1 " + list1);

            /*
             list1이 들어올때마다 값이 reset 된다

             seq ++ 시점?
             */


            if(currList.size() > 0) {

                System.out.println("list is not empty  seq : "  + seq);
                IntStream.range(0, currList.size()).forEach(i -> {
                    saveConsecutiveData(currList.get(i), lottoDtoList.get(currentIdx).getRound(),lottoDtoList.get(nextIdx).getRound() ,isCon);
                });
                compareHaveSameNum(lottoDtoList,currentIdx, nextIdx+1, true);
            }
        }


    }

    private void saveConsecutiveData(int num,int round, int comround, boolean isCon){
        System.out.println("saveConsecutiveData" );
        System.out.println("num : " + num + ", round " + round + "  comround: " +comround);
        //조건 추가
        if(!checkConNumhasRound(num, isCon ? round : comround)){
            System.out.println("nonMatch");
            List<Integer> roundList = new LinkedList<>();
            roundList.add(round);
            roundList.add(comround);
            ConsecutiveDto consecutiveDto = new ConsecutiveDto(seq, num, 1, roundList);
            consList.add(consecutiveDto);
        }else{
            System.out.println("match " + seq);
            ConsecutiveDto conDto = consList.stream().filter(consecutiveDto -> consecutiveDto.getSeq() == seq).findFirst().get();
            conDto.setCount(conDto.getCount() + 1);
            List<Integer> rounds = conDto.getRounds();
            rounds.add(comround);
            consList.add(conDto);
        }
    }

    private boolean checkConNumhasRound(int num, int round){
        System.out.println("checkConNumhasRound");
         return consList.stream().anyMatch(dto -> dto.getSeq() == seq && dto.getConNum() == num && dto.getRounds().stream().anyMatch(r -> r == round ));
         //if true 초복 중복 말복
    }

    @Override
    public void insert(Lotto lotto) {
        //당첨회차 validation
        log.info("service insert");
        boolean isPresent = Optional.ofNullable(lottoRepository.finOne(lotto.getRound())).isPresent();
        if (!isPresent) {
            log.info("is not Present ", isPresent);
            lottoRepository.insert(lotto);
        } else {
            log.info("이미 존재하는 회차");
            //exception 추가
            throw new RoundAlreadyExistException(String.format("[%s] round already exist", lotto.getRound()));
        }
    }

    private List<Integer> transformToIntList(List<Lotto> lottoList) {
        List<Integer> numberList = new ArrayList<>();
        for (Lotto lotto : lottoList) {
            numberList.add(lotto.getFirst());
            numberList.add(lotto.getSecond());
            numberList.add(lotto.getThird());
            numberList.add(lotto.getFourth());
            numberList.add(lotto.getFifth());
            numberList.add(lotto.getSixth());
            numberList.add(lotto.getBonus());
        }

        return numberList;
    }

    private List<Integer> transformDtoToIntList(LottoDto lottoDto) {
        List<Integer> numberList = new ArrayList<>();
        numberList.add(lottoDto.getFirst());
        numberList.add(lottoDto.getSecond());
        numberList.add(lottoDto.getThird());
        numberList.add(lottoDto.getFourth());
        numberList.add(lottoDto.getFifth());
        numberList.add(lottoDto.getSixth());
        numberList.add(lottoDto.getBonus());

        return numberList;
    }

    private List<NumberDto> transformToNumberDtoList(List<Integer> numberList) {
        //로또 번호들 중복횟수
        Map<Integer, Integer> map = numberList.stream().collect(Collectors.toMap(Function.identity(), value -> 1, Integer::sum));

        List<Integer> keyList = new ArrayList<>(map.keySet());
        List<Integer> valueList = new ArrayList<>(map.values());

        List<NumberDto> numberDtoList = new ArrayList<>();
        IntStream.range(0, keyList.size()).forEach(i -> {
            int key = keyList.get(i);
            int dup = valueList.get(i);
            numberDtoList.add(new NumberDto(key, dup));
        });

        List<NumberDto> sortedList = numberDtoList.stream()
                .sorted(Comparator.comparingInt(NumberDto::getDuplication).reversed())
                .collect(Collectors.toList())
                .subList(0, 10);

        return sortedList;
    }

}
