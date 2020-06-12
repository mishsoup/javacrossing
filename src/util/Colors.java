package util;

public enum Colors {

    RED("red"),
    GREEN("green"),
    BLUE("blue"),
    BLACK("black"),
    YELLOW("yellow"),
    ORANGE("orange"),
    PINK("pink"),
    PURPLE("purple");
    
    private final String name;


    private Colors(String s) {
        name = s;
    }
}
