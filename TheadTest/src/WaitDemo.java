public class WaitDemo {
    private static Object lock = new Object();

    static class WaitThread implements Runnable {

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println("before wait");
                    lock.wait();

                    // 进入等待被唤醒之后，会从这里重新执行
                    System.out.println("after wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {

        WaitThread waitThread = new WaitThread();
        Thread thread = new Thread(waitThread);

        thread.start();

        Thread.sleep(2000);
        System.out.println("sleep");
        synchronized (lock) {
            lock.notify();
        }
    }
}

