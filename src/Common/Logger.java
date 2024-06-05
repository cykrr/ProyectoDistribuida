package Common;

public class Logger {
    private String className;
    public Logger(String className) {
        this.className = className;
        
        // System.out.println(Colors.ANSI_BLUE + "Logger creado para " + className + Colors.ANSI_RESET);
    }

    public void log(String message) {
        System.out.println(
            "[" + className + "] " + 
            Colors.ANSI_GREEN +
                 message +
             Colors.ANSI_RESET);
    }

    public void error(String message) {
        System.out.println(
            "[" + className + "] " + 
            Colors.ANSI_RED +
                 message +
             Colors.ANSI_RESET);
    }

    public void warning(String message) {
        System.out.println(
            "[" + className + "] " + 
            Colors.ANSI_YELLOW +
                 message +
             Colors.ANSI_RESET);
    }
    
}
