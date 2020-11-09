package ahk.solace.receiver.model;

public enum Queue {

    QUEUE1("QUEUE1"),
    QUEUE2("QUEUE2"),
    QUEUE3("QUEUE3"),
    QUEUE4("QUEUE4");

    private String queueName;

    Queue(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }
}
