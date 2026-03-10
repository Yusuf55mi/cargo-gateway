package dev.yusufaydin.cargogate.provider.yurtici;

import dev.yusufaydin.cargogate.common.exception.ApiException;
import dev.yusufaydin.cargogate.provider.yurtici.model.YurticiCreateResponse;
import dev.yusufaydin.cargogate.provider.yurtici.model.YurticiQueryResponse;
import dev.yusufaydin.cargogate.provider.yurtici.model.YurticiTrackEvent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Yurtici icin:
 *  - Freemarker ile SOAP XML uretimi
 *  - DOM parser ile SOAP XML parse
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class YurticiXmlService {

    private final Configuration freemarkerConfiguration;

    public String generateSoapXml(String templateName, Map<String, Object> model) {
        try {
            Template template = freemarkerConfiguration.getTemplate("soap/yurtici/" + templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            String xml = writer.toString();
            log.debug("Yurtici SOAP istek XML:\n{}", xml);
            return xml;
        } catch (Exception e) {
            log.error("SOAP XML uretilemedi: template={}, hata={}", templateName, e.getMessage());
            throw new ApiException("SOAP XML uretilemedi: " + e.getMessage(), 500);
        }
    }

    public YurticiCreateResponse parseCreateResponse(String xml) {
        log.debug("Yurtici createShipment yaniti:\n{}", xml);
        Document doc = parseXml(xml);
        YurticiCreateResponse resp = new YurticiCreateResponse();
        resp.setResultCode(getNodeValue(doc, "resultCode"));
        resp.setResultMessage(getNodeValue(doc, "resultMessage"));
        resp.setBarcode(getNodeValue(doc, "barcode"));
        return resp;
    }

    public YurticiQueryResponse parseQueryResponse(String xml) {
        log.debug("Yurtici queryShipment yaniti:\n{}", xml);
        Document doc = parseXml(xml);
        YurticiQueryResponse resp = new YurticiQueryResponse();
        resp.setOperationStatus(getNodeValue(doc, "operationStatus"));
        resp.setSenderName(getNodeValue(doc, "senderName"));
        resp.setReceiverName(getNodeValue(doc, "receiverName"));

        // Gecmis olaylar
        List<YurticiTrackEvent> events = new ArrayList<>();
        NodeList cargoMovements = doc.getElementsByTagNameNS("*", "cargoMovements");
        if (cargoMovements == null || cargoMovements.getLength() == 0) {
            cargoMovements = doc.getElementsByTagName("cargoMovements");
        }
        for (int i = 0; i < cargoMovements.getLength(); i++) {
            Node node = cargoMovements.item(i);
            YurticiTrackEvent event = new YurticiTrackEvent();
            event.setDate(getChildValue(node, "date"));
            event.setTime(getChildValue(node, "time"));
            event.setLocation(getChildValue(node, "location"));
            event.setDescription(getChildValue(node, "description"));
            events.add(event);
        }
        resp.setEvents(events);
        return resp;
    }

    private Document parseXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            // XXE onlemi
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("XML parse hatasi: {}", e.getMessage());
            throw new ApiException("Yurtici yaniti parse edilemedi: " + e.getMessage(), 502);
        }
    }

    private String getNodeValue(Document doc, String tagName) {
        // Once namespace'li dene, sonra namespace'siz
        NodeList nodes = doc.getElementsByTagNameNS("*", tagName);
        if (nodes == null || nodes.getLength() == 0) {
            nodes = doc.getElementsByTagName(tagName);
        }
        if (nodes != null && nodes.getLength() > 0) {
            Node node = nodes.item(0);
            if (node != null && node.getFirstChild() != null) {
                return node.getFirstChild().getNodeValue();
            }
        }
        return null;
    }

    private String getChildValue(Node parent, String childName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (childName.equals(child.getLocalName()) || childName.equals(child.getNodeName())) {
                if (child.getFirstChild() != null) {
                    return child.getFirstChild().getNodeValue();
                }
            }
        }
        return null;
    }
}
