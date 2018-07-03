package ua.ksa.ae.parser;

import org.jsoup.nodes.Element;

/**
 * @author ksa on 7/3/18.
 * @project parser
 */
public interface Searcher {
    Element loadOriginal(String path, String elementName);
    Element search(String path);
    void path();
}
