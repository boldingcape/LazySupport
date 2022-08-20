package console;

import java.io.IOException;

public class Query {
    private int requestState=1;
    public void request(String[] commands) throws IOException {
        try{
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(commands);
            proc.waitFor();
            setRequestState(proc.exitValue());
        } catch (InterruptedException e) {
            throw new RuntimeException("processing of command failed: " + e);
        } catch (IOException e){
            throw new IOException("execution of command failed: " + e);
        }
    }


    public int getRequestState() {
        return requestState;
    }

    public void setRequestState(int requestState) {
        this.requestState = requestState;
    }
}
