package hu.doxasoft.buzz.entities;

import hu.doxasoft.buzz.enums.ProcessStatus;

public class OutputLine {
    private int lineNumber;
    private ProcessStatus status;
    private String message;

    public OutputLine(int lineNumber, ProcessStatus status, String message) {
        this.lineNumber = lineNumber;
        this.status = status;
        this.message = message;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s", lineNumber, status, message);
    }
}
