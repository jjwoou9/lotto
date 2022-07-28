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
         *  ex 29, 41
         * dto29추가, dto41추가
         *
         * */


        /*
        1023 1022 비교
        retain? > 0
        retain한 list 만큼 loop 돌려서 저장
         */

        for (int i = 0; i < lottoDtoList.size(); i++) {
            System.out.println(" i : " + i);
            compareHaveSameNum(lottoDtoList, i, i+1);
        }

        return consList;
    }

    private void compareHaveSameNum(List<LottoDto> lottoDtoList, int currentIdx, int nextIdx) {
        System.out.println("compareHaveSameNum : " + currentIdx + "  next : " + nextIdx  + " size: " + lottoDtoList.size());
        // 여기에 && currentIdx == lottoDtoList.size()


        if(nextIdx < lottoDtoList.size()){
            System.out.println("??????????????????");
            List<Integer> list1 = transformDtoToIntList(lottoDtoList.get(currentIdx));
            List<Integer> list2 = transformDtoToIntList(lottoDtoList.get(nextIdx));

            list1.retainAll(list2);

            System.out.println(" retain1 " + list1);

            if(list1.size() > 0) {

                for(int i=0; i< list1.size() ; i++ ){
//                    saveConsecutiveData(currList.get(i), lottoDtoList.get(currentIdx).getRound(),lottoDtoList.get(nextIdx).getRound() ,isCon);
                    checkBeforeSave(list1.get(i), lottoDtoList.get(currentIdx).getRound(),lottoDtoList.get(nextIdx).getRound());
                }

//                IntStream.range(0, currList.size()).forEach(i -> { //여기가 문제였어 ~~
//                    saveConsecutiveData(currList.get(i), lottoDtoList.get(currentIdx).getRound(),lottoDtoList.get(nextIdx).getRound() ,isCon);
//                });
                if(list1.size() > 1) compareHaveSameNum(lottoDtoList,currentIdx, nextIdx+1);
            }
        }
        else{
            System.out.println("else");
        }
    }

    private void checkBeforeSave(int num, int round, int comround){
        boolean isExist = false;

        for(ConsecutiveDto dto : consList){
            if(dto.getConNum() == num){
                List<Integer> rounds = dto.getRounds();
                if(rounds.stream().noneMatch(r -> r == round )){
                    saveConsecutiveData(num, round ,false);
                }else if(rounds.stream().noneMatch(r -> r == comround)){
                    saveConsecutiveData(num, comround, false);
                }else{
                    //이미 존재
                }
            }
            else{
                //넘버 없음
                isExist = false;
                saveConsecutiveData(num, round, true);
                saveConsecutiveData(num, comround, true);
            }
        }
    }

    private void saveConsecutiveData(int num,int round, boolean isNew){

        if(isNew){
            List<Integer> tempRounds = new ArrayList<>();
            tempRounds.add(round);

            ConsecutiveDto conDto = new ConsecutiveDto(seq, num, tempRounds.size(), tempRounds);
            consList.add(conDto);

        }else{
            ConsecutiveDto conDto = consList.stream().filter(consecutiveDto -> consecutiveDto.getConNum() == num).findFirst().get();
            List<Integer> tempRounds = conDto.getRounds();
            tempRounds.add(round);
            conDto.setRounds(tempRounds);
            conDto.setCount(tempRounds.size());
            consList.add(conDto);
        }
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
