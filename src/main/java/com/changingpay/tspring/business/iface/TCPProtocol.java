package com.changingpay.tspring.business.iface;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by 公司 on 2017/2/20.
 */
public interface TCPProtocol {
    void handleAccept(SelectionKey key) throws IOException;

    void handleRead(SelectionKey key) throws IOException;

    void handleWrite(SelectionKey key) throws IOException;
}
