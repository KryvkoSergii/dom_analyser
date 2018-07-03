package ua.ksa.ae.parser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsoup.nodes.Element;

/**
 * @author ksa on 7/2/18.
 * @project parser
 */
@Data
@EqualsAndHashCode
public class ElementDescriptor {
    private final String tag;
    private final String className;
    private final String id;

    private ElementDescriptor(String tag, String className, String id) {
        this.tag = tag;
        this.className = className;
        this.id = id;
    }

    public static ElementDescriptor get(Element element) {
        return new ElementDescriptor(element.tag().getName(), element.className(),element.id());
    }

    @Override
    public String toString() {
        return String.format("[%s/%s/%s]",tag,id,className);
    }
}
