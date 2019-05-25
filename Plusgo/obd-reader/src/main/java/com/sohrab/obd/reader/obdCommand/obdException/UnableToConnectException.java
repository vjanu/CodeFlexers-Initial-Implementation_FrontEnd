package com.sohrab.obd.reader.obdCommand.obdException;

/**
 * Thrown when there is a "UNABLE TO CONNECT" message.
 *
 * @author pires
 * @version $Id: $Id
 */
public class UnableToConnectException extends ResponseException {

    /**
     * <p>Constructor for UnableToConnectException.</p>
     */
    public UnableToConnectException() {
        super("UNABLE TO CONNECT");
    }

}
