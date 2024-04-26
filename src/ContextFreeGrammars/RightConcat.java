package ContextFreeGrammars;

public class RightConcat implements RightNonEmpty {
    private final RightNonEmpty a;
    private final RightNonEmpty b;

    public RightConcat(RightNonEmpty a, RightNonEmpty b){
        this.a = a;
        this.b = b;
    }
}
