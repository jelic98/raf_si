package rs.raf.generator;

public class Template {

    public static final String NODE = "{{visibility}} {{type}} {{name}} {{link}} {{parent}} {\n{{properties}}\n{{methods}}\n}";
    public static final String METHOD = "{{visibility}} {{type}} {{name}} ({{parameters}}) {\n\n}";
    public static final String PROPERTY = "{{visibility}} {{type}} {{name}};";
    public static final String PARAMETER = "{{type}} {{name}}";

    public static class Separator {

        public static final String NODE = "\n\n";
        public static final String METHOD = "\n\n";
        public static final String PROPERTY = "\n";
        public static final String PARAMETER = ", ";
    }
}
