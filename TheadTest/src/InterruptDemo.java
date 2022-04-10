public class InterruptDemo {
    static class InterruptThread implements Runnable {

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) {
        InterruptThread interruptThread = new InterruptThread();
        Thread thread = new Thread(interruptThread);

        thread.start();

        thread.interrupt();
        thread.isInterrupted();

        thread.interrupted();

        Thread.interrupted();
    }
}
