package me.ansandr.transwarp.util;

import static me.ansandr.transwarp.util.MessageManager.tl;

public class TransportNotFoundException extends Exception {

    public TransportNotFoundException() {
        super(tl("transport_not_found"));
    }

    public TransportNotFoundException(String message) {
        super(message);
    }
}
