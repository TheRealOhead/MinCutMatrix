package org.owenparker;
public class NoValidEdgePresentException extends Exception {
    public NoValidEdgePresentException(String message) {
        System.err.println(message);
    }
}
