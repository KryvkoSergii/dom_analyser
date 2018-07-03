package ua.ksa.ae.parser;

import lombok.Data;
import org.jsoup.nodes.Element;

import java.util.Optional;

/**
 * @author ksa on 7/3/18.
 * @project parser
 */
@Data
public class TargetElement {
    private final Element origin;
    private final int size;

    Optional<Candidate> evaluate(Element element, int level) {
        int result = getSum(element);
        if (result > 0)
            return Optional.of(new Candidate(level * result, element));
        return Optional.empty();
    }

    private int getSum(Element element) {
        return checkByClassName(element) + checkByTagName(element) + checkByData(element);
    }

    private int checkByClassName(Element element) {
        if (element.className().equals(origin.className())) return 1;
        return 0;
    }

    private int checkByTagName(Element element) {
        if (element.tag().getName().equals(origin.tag().getName())) return 1;
        return 0;
    }

    private int checkByData(Element element) {
        if (element.data().equals(origin.data())) return 1;
        return 0;
    }
}
