package ua.ksa.ae.parser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JsoupFindByIdSnippet implements Searcher {
    private List<ElementDescriptor> elementChain;
    private TargetElement searchedElement;
    private Element resultElement;
    private static Logger LOGGER = LoggerFactory.getLogger(JsoupFindByIdSnippet.class);

    private static String CHARSET_NAME = "utf8";

    @Deprecated
    public void method(String path, String elementName) {
        Optional<Element> buttonOpt = findElementById(new File(path), elementName);
        Optional<String> stringifiedAttributesOpt = buttonOpt.map(button ->
                button.attributes().asList().stream()
                        .map(attr -> attr.getKey() + " = " + attr.getValue())
                        .collect(Collectors.joining(", "))
        );
        stringifiedAttributesOpt.ifPresent(attrs -> LOGGER.info("Target element attrs: [{}]", attrs));
        if (buttonOpt.isPresent()) {
            List<ElementDescriptor> path1 = getElementDescriptors(buttonOpt.get());
            LOGGER.debug(path1.stream()
                    .map(ElementDescriptor::toString)
                    .collect(Collectors.joining(",")));
        }
    }

    @Override
    public Element loadOriginal(String path, String elementName) {
        Optional<Element> buttonOpt = findElementById(new File(path), elementName);
        Optional<String> stringifiedAttributesOpt = buttonOpt.map(button ->
                button.attributes().asList().stream()
                        .map(attr -> attr.getKey() + " = " + attr.getValue())
                        .collect(Collectors.joining(", "))
        );
        stringifiedAttributesOpt.ifPresent(attrs -> LOGGER.info("Target element attrs: [{}]", attrs));
        if (buttonOpt.isPresent()) {
            elementChain = findPath(buttonOpt.get());
            searchedElement = new TargetElement(buttonOpt.get(), elementChain.size());
            Collections.reverse(elementChain);
            LOGGER.debug(elementChain.stream()
                    .map(ElementDescriptor::toString)
                    .collect(Collectors.joining("->")));
        }
        return buttonOpt.orElseThrow(() -> new IllegalStateException("element not found"));
    }

    @Override
    public Element search(String path) {
        Optional<Document> doc = target(new File(path));
        if (!doc.isPresent()) throw new IllegalStateException("file not found " + path);
        int i = 0;
        Element root = findRootElement(elementChain.get(i), doc.get())
                .orElseThrow(() -> new IllegalStateException("root element not found"));
        List<Candidate> container = new ArrayList<>();
        processNode(++i, elementChain, root, searchedElement, container);
        LOGGER.debug(container.toString());
        Candidate finish = container.stream()
                .max(Comparator.comparingInt(Candidate::getResult))
                .orElseThrow(() -> new IllegalStateException("element not found"));
        printTrace(finish.getElement());
        resultElement = finish.getElement();
        return finish.getElement();
    }

    @Override
    public void path() {
        LOGGER.info("path: {}", getElementDescriptors(resultElement).stream()
                .map(ElementDescriptor::getTag)
                .collect(Collectors.joining("->")));
    }

    private void processNode(int level, List<ElementDescriptor> chain, Element currentElement,
                             TargetElement searchedElement, List<Candidate> candidates) {
        searchedElement.evaluate(currentElement, level).ifPresent(candidates::add);
        if (chain.size() >= level) {
            Iterator<Element> it = currentElement.children().iterator();
            while (it.hasNext()) {
                Element element = it.next();
                ElementDescriptor descriptor = ElementDescriptor.get(element);
                //template case
                if (chain.size() > level && descriptor.equals(chain.get(level))) {
                    processNode(level + 1, elementChain, element, searchedElement, candidates);
                } else {
                    //no template case
                    processNode(level, elementChain, element, searchedElement, candidates);
                }
            }
        }
    }

    private void printTrace(Element element) {
        List<ElementDescriptor> descriptors = getElementDescriptors(element);
        LOGGER.debug(descriptors.stream()
                .map(ElementDescriptor::toString)
                .collect(Collectors.joining("->")));
    }

    private List<ElementDescriptor> getElementDescriptors(Element element) {
        List<ElementDescriptor> descriptors = findPath(element);
        Collections.reverse(descriptors);
        return descriptors;
    }

    private List<ElementDescriptor> findPath(Element element) {
        return processParent(element, new ArrayList<>());
    }

    private List<ElementDescriptor> processParent(Element parent, List<ElementDescriptor> chain) {
        if (parent != null) {
            chain.add(ElementDescriptor.get(parent));
            processParent(parent.parent(), chain);
        }
        return chain;
    }

    private Optional<Element> findRootElement(ElementDescriptor template, Document target) {
        Iterator<Element> it = target.getAllElements().iterator();
        List<Element> elements = getElements(template, it);
        if (!elements.isEmpty()) return Optional.ofNullable(elements.get(0));
        return Optional.empty();
    }

    private List<Element> getElements(ElementDescriptor template, Iterator<Element> it) {
        List<Element> elements = new ArrayList<>();
        while (it.hasNext()) {
            Element element = it.next();
            ElementDescriptor descriptor = ElementDescriptor.get(element);
            if (descriptor.equals(template))
                elements.add(element);
        }
        return elements;
    }


    private static Optional<Document> target(File htmlFile) {
        try {
            return Optional.of(parseFile(htmlFile));
        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    private static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = parseFile(htmlFile);
            return Optional.of(doc.getElementById(targetElementId));
        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    private static Document parseFile(File htmlFile) throws IOException {
        return Jsoup.parse(
                htmlFile,
                CHARSET_NAME,
                htmlFile.getAbsolutePath());
    }

}