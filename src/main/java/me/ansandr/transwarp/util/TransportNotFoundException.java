package me.ansandr.transwarp.util;

import static me.ansandr.utils.message.MessageManager.tl;

public class TransportNotFoundException extends Exception {

    public TransportNotFoundException() {
        super(tl("error.transport_not_found"));
    }

    public TransportNotFoundException(String message) {
        super(message);
    }
}
