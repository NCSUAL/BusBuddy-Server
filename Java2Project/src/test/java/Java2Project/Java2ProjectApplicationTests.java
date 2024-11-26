package Java2Project;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
@ActiveProfiles("test") //test 프로파일을 활성화하여 테스트용 설정을 적용

class Java2ProjectApplicationTests {

	@Test
	void contextLoads() {
		Flux<String> arrays = Flux.just("A", "B", "C")
				.parallel() //데이터 스트림을 병렬로 변경
				.runOn(Schedulers.parallel()) //병렬 스케줄러에서 실행
				.doOnNext(s -> System.out.println("데이터 스트림의 원소: " + s))
				.sequential(); //다시 flux로 변환

		//구독
		arrays
				.subscribe(s -> System.out.println( s+ " 원소 구독 끝(종료 처리)"));
	}

}
