package lambda;

/**
 * Created by lenaskarlat on 6/21/17.
 */
public class ResponseClass {

    String dataFrameProcessed;

    public ResponseClass() {
    }

    public ResponseClass(String dataFrameProcessed) {
        this.dataFrameProcessed = dataFrameProcessed;
    }

    public String getDataFrameProcessed() {
        return dataFrameProcessed;
    }

    public void setDataFrameProcessed(String dataFrameProcessed) {
        this.dataFrameProcessed = dataFrameProcessed;
    }

}
