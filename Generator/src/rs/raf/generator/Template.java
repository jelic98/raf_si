package rs.raf.generator;

public class Template {

    public static final String NODE = "{{visibility}} {{type}} {{name}} {{link}} {{parent}} {\n\n{{properties}}\n\n{{methods}}\n}";
    public static final String METHOD = "\t{{visibility}} {{type}} {{name}}({{parameters}}) {\n\n\t}";
    public static final String PROPERTY = "\t{{visibility}} {{type}} {{name}};";
    public static final String PARAMETER = "{{type}} {{name}}";

    public static class Separator {

        public static final String NODE = "\n\n";
        public static final String METHOD = "\n\n";
        public static final String PROPERTY = "\n";
        public static final String PARAMETER = ", ";
    }
}
