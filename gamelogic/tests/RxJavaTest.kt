
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class RxJavaTest {

    @Test
    fun asyncTest() {
        val subj = BehaviorSubject.createDefault(1234)

        val subscribeScheduler = Schedulers.from(Executors.newFixedThreadPool(2))
        val observersScheduler = Schedulers.from(Executors.newFixedThreadPool(2))
        for (i in 0..1000) {
            subj.subscribeOn(subscribeScheduler).observeOn(observersScheduler).subscribeBy(
                    onNext = {}
            )
        }
        Thread.sleep(100L)
    }
}