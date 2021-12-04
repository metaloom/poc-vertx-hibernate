package io.metaloom.poc.rx;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.metaloom.poc.server.RESTException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.Maybe;

public class RxJavaTest {

	@Test
	public void testMaybeSwitch() throws InterruptedException {
		Maybe<String> source = Maybe.just("test");
		CountDownLatch latch = new CountDownLatch(1);
		source.switchIfEmpty(Maybe.error(RESTException.create(404)))
			.flatMapCompletable(result -> {
				return completeOperation(result);
			}).doOnDispose(() -> {
				System.out.println("Abborted");
			}).subscribe(() -> {
				System.out.println("Completed");
				latch.countDown();
			}, err -> {
				err.printStackTrace();
			});
		waitFor(latch);
	}

	@Test
	public void testMaybeSwitchEmpty() throws InterruptedException {
		Maybe<String> source = Maybe.just("test");
		CountDownLatch latch = new CountDownLatch(1);
		source.switchIfEmpty(Maybe.error(RESTException.create(404)))
			.flatMapCompletable(result -> {
				return completeOperation(result);
			}).doOnDispose(() -> {
				System.out.println("Abborted");
			}).subscribe(() -> {
				System.out.println("Completed");
			}, err -> {
				if (err instanceof RESTException) {
					if (((RESTException) err).code() == 404) {
						err.printStackTrace();
						fail("No 404 rest error received.");
					}
				}
				latch.countDown();
			});
		waitFor(latch);
	}

	@Test
	public void testMaybeSwitchDispose() throws InterruptedException {
		Maybe<String> source = Maybe.empty();
		CountDownLatch latch = new CountDownLatch(1);
		source.switchIfEmpty(Maybe.error(RESTException.create(404)))
			.flatMapCompletable(result -> {
				return completeOperation(result);
			}).doOnDispose(() -> {
				latch.countDown();
			})
			.doOnSubscribe(d -> {
				// Abort operation
				d.dispose();
			}).subscribe(() -> {
				// NOOP
			}, err -> {
				// NOOP
			});
		waitFor(latch);
	}

	private CompletableSource completeOperation(String result) {
		assertNotNull(result);
		return Completable.complete();
	}

	private void waitFor(CountDownLatch latch) throws InterruptedException {
		if (!latch.await(1, TimeUnit.SECONDS)) {
			fail("Timeout reached. Latch not triggerd");
		}

	}

}