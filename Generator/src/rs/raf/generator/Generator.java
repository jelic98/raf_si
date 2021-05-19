package rs.raf.generator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Generator {

     public String generate(String request) {
        JSONObject model = new JSONObject(request).getJSONObject("model").getJSONObject("details");
        JSONArray nodes = model.getJSONArray("nodes");
        JSONArray links = model.getJSONArray("links");

        StringBuilder nodeBuild = new StringBuilder();

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            JSONArray methods = node.getJSONArray("methods");
            JSONArray properties = node.getJSONArray("properties");
            String nodeName = node.get("name").toString();

            for (int j = 0; j < links.length(); j++) {
                JSONObject link = links.getJSONObject(j);
                JSONObject from = link.getJSONObject("from");
                JSONObject to = link.getJSONObject("to");
                String relationship = link.get("relationship").toString();
                String fromName = from.get("name").toString();
                String toName = to.get("name").toString();

                if (relationship.equals("generalization")) {
                    if (fromName.equals(nodeName)) {
                        node.put("link", "extends");
                        node.put("parent", toName);
                    }
                }else if (relationship.equals("aggregation") || relationship.equals("composition")) {
                    if (fromName.equals(nodeName)) {
                        JSONObject property = new JSONObject();
                        property.put("name", "Link_" + j++);
                        property.put("type", toName);
                        property.put("visibility", "private");
                        properties.put(property);
                    }
                }
            }

            String nodeGen = getNode(node);

            StringBuilder methodBuild = new StringBuilder();

            for (int j = 0; j < methods.length(); j++) {
                JSONObject method = methods.getJSONObject(j);
                JSONArray parameters = method.getJSONArray("parameters");

                String methodGen = getMethod(method);

                StringBuilder parameterBuild = new StringBuilder();

                for (int k = 0; k < parameters.length(); k++) {
                    JSONObject parameter = methods.getJSONObject(k);

                    String parameterGen = getParameter(parameter);

                    parameterBuild.append(parameterGen);

                    if(k < parameter.length() - 1) {
                        parameterBuild.append(Template.Separator.PARAMETER);
                    }
                }

                methodGen = methodGen
                        .replace("{{parameters}}", parameterBuild.toString());
                methodBuild.append(methodGen);
                methodBuild.append(Template.Separator.METHOD);
            }

            StringBuilder propertyBuild = new StringBuilder();

            for (int j = 0; j < properties.length(); j++) {
                JSONObject property = methods.getJSONObject(j);

                String propertyGen = getProperty(property);

                propertyBuild.append(propertyGen);
                propertyBuild.append(Template.Separator.PROPERTY);
            }

            nodeGen = nodeGen
                    .replace("{{methods}}", methodBuild.toString())
                    .replace("{{properties}}", propertyBuild.toString());
            nodeBuild.append(nodeGen);
            nodeBuild.append(Template.Separator.NODE);
        }

        return nodeBuild.toString();
    }

    private String getNode(JSONObject node) {
        String name = node.get("name").toString();
        String type = node.get("type").toString();
        String visibility = node.get("visibility").toString();
        String link = node.get("link").toString();
        String parent = node.get("parent").toString();

        return Template.NODE
                .replace("{{name}}", name)
                .replace("{{type}}", type)
                .replace("{{visibility}}", visibility)
                .replace("{{link}}", link)
                .replace("{{parent}}", parent);
    }

    private String getMethod(JSONObject method) {
        String name = method.get("name").toString();
        String type = method.get("type").toString();
        String visibility = method.get("visibility").toString();

        return Template.METHOD
                .replace("{{name}}", name)
                .replace("{{type}}", type)
                .replace("{{visibility}}", visibility);
    }

    private String getProperty(JSONObject property) {
        String name = property.get("name").toString();
        String type = property.get("type").toString();
        String visibility = property.get("visibility").toString();

        return Template.PROPERTY
                .replace("{{name}}", name)
                .replace("{{type}}", type)
                .replace("{{visibility}}", visibility);
    }

    private String getParameter(JSONObject property) {
        String name = property.get("name").toString();
        String type = property.get("type").toString();

        return Template.PARAMETER
                .replace("{{name}}", name)
                .replace("{{type}}", type);
    }
}
