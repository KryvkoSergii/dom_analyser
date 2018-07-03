package ua.ksa.ae.parser;

/**
 * @author ksa on 7/3/18.
 * @project parser
 */
public class Runner {

    public static void main(String[] args) {
        String id = "make-everything-ok-button";
        if(args.length != 2 || validParam(args[0]) ||validParam(args[1]))
            throw new IllegalArgumentException("incorrect parameters");
        Searcher searcher = new JsoupFindByIdSnippet();
        searcher.loadOriginal(args[0],id);
        searcher.search(args[1]);
        searcher.path();
    }

    private static boolean validParam(String arg) {
        return arg == null || arg.isEmpty();
    }
}
