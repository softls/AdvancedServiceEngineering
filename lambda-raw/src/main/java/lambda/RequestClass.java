package lambda;

/**
 * Created by lenaskarlat on 6/21/17.
 */
public class RequestClass {
    String dataFrameMessage;

    public String getDataFrameMessage() {
        return dataFrameMessage;
    }

    public void setDataFrameMessage(String dataFrameMessage) {
        this.dataFrameMessage = dataFrameMessage;
    }

    public RequestClass() {
    }

    public RequestClass(String dataFrameMessage) {
        this.dataFrameMessage = dataFrameMessage;
    }
}
