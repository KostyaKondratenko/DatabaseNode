package Interfaces;

import java.io.IOException;
import java.net.UnknownHostException;

public interface INodeLinker {
    void connectNodes() throws UnknownHostException, IOException;
}
