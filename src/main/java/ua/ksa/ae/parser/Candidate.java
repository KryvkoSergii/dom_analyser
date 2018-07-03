package ua.ksa.ae.parser;

import lombok.Data;
import org.jsoup.nodes.Element;

/**
 * @author ksa on 7/3/18.
 * @project parser
 */
@Data

public class Candidate {
    private final int result;
    private final Element element;

    @Override
    public String toString() {
        return String.format("%s %s",ElementDescriptor.get(element),result);
    }
}
