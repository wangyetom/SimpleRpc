package remoting;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangye on 17/12/7.
 */
public class RpcResponse {
    private Object data;


    private CountDownLatch countDownLatch = new CountDownLatch(1);



    public void setData(Object data) {
        this.data = data;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void wait(int timeMills) throws InterruptedException {
        countDownLatch.await(timeMills, TimeUnit.MILLISECONDS);
    }

    public void done(Object data) {
        this.data = (Object) data;
        countDownLatch.countDown();
    }

    public Object getData() {

        return data;
    }
}
