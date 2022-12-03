class MySynchronizeThread implements Runnable {

    private int threadIndex;
    private int threadIndexSkipper;
    private long threadStartTime;

    static int counter;

    public MySynchronizeThread(int threadIndex) {
        this.threadIndex = threadIndex;
        this.threadIndexSkipper = threadIndex;

        threadStartTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        long threadFinalCode = SynchronizeThreadProducer.numbers.get(threadIndexSkipper);
        while (threadIndexSkipper + SynchronizeThreadProducer.threadsCount < SynchronizeThreadProducer.numbers.size()) {
            threadFinalCode ^= SynchronizeThreadProducer.numbers.get(threadIndexSkipper + SynchronizeThreadProducer.threadsCount);
            threadIndexSkipper += SynchronizeThreadProducer.threadsCount;
        }

        if (threadFinalCode == 0)
            System.out.println("Thread " + threadIndex + " final code: " + SynchronizeThreadProducer.finalCode);
        else {
            //>>>>>>>>>>>>  Critical Section
            synchronized (MySynchronizeThread.class) {
                if (System.currentTimeMillis() - counter * 1000 - threadStartTime > 2000)
                    System.out.println("Thread " + threadIndex + " timed out.");
                else {
                    SynchronizeThreadProducer.finalCode ^= threadFinalCode;
                    System.out.println("Thread " + threadIndex + " final code: " + SynchronizeThreadProducer.finalCode);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter++;
                }
            }
            //>>>>>>>>>>>>
        }
    }
}
