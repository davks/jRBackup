package jrbackup;

public class ParametrPrikazu {
    private final String parametr;
    private final String tooltip;

    public ParametrPrikazu(String parametr, String tooltip) {
        this.parametr = parametr;
        this.tooltip = tooltip;
    }

    public String getParametr() {
        return parametr;
    }

    public String getTooltip() {
        return tooltip;
    }
}
