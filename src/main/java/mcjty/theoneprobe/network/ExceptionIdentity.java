package mcjty.theoneprobe.network;

public class ExceptionIdentity {
    private final String message;
    private final String className;
    private final String fileName;
    private final String methodName;
    private final int linenumber;

    public ExceptionIdentity(Exception e) {
        this.message = e.getMessage();
        StackTraceElement[] st = e.getStackTrace();
        if (st != null && st.length > 0) {
            className = st[0].getClassName();
            fileName = st[0].getFileName();
            methodName = st[0].getMethodName();
            linenumber = st[0].getLineNumber();
        } else {
            className = "_";
            fileName = "_";
            methodName = "_";
            linenumber = -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExceptionIdentity that = (ExceptionIdentity) o;

        if (linenumber != that.linenumber) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + linenumber;
        return result;
    }
}
