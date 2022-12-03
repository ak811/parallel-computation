public class MyThread implements Runnable {

    static long[] blockingQueue = new long[ThreadProducer.threadsCount];

    private int threadIndex;
    private int threadIndexSkipper;

    public MyThread(int threadIndex) {
        this.threadIndex = threadIndex;

        this.threadIndexSkipper = threadIndex;
    }

    @Override
    public void run() {
        blockingQueue[threadIndex] = ThreadProducer.numbers.get(threadIndexSkipper);
        while (threadIndexSkipper + ThreadProducer.threadsCount < ThreadProducer.numbers.size()) {
            blockingQueue[threadIndex] ^= ThreadProducer.numbers.get(threadIndexSkipper + ThreadProducer.threadsCount);
            threadIndexSkipper += ThreadProducer.threadsCount;
        }
    }
}
