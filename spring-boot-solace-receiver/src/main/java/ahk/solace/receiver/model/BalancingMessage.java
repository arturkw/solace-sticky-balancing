package ahk.solace.receiver.model;

import java.io.Serializable;

public class BalancingMessage implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

    private int instanceId;
    private int numOfQueuesConnected;

    public BalancingMessage() {

    }

    public BalancingMessage(int instanceId, int numOfQueuesConnected) {
        this.instanceId = instanceId;
        this.numOfQueuesConnected = numOfQueuesConnected;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public int getNumOfQueuesConnected() {
        return numOfQueuesConnected;
    }

    public void setNumOfQueuesConnected(int numOfQueuesConnected) {
        this.numOfQueuesConnected = numOfQueuesConnected;
    }
}
