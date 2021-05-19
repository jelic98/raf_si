package rs.raf.generator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Generator {

    public JSONArray generate(String model) {
        JSONArray files = new JSONArray();

        JSONObject details = new JSONObject(model).getJSONObject("details");
        JSONArray nodes = details.getJSONArray("nodes");
        JSONArray links = details.getJSONArray("links");

        Map<Integer, String> keys = new HashMap<>();

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            String nodeName = node.getString("name");
            int nodeKey = node.getInt("key");

            keys.put(nodeKey, nodeName);
        }

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject node = nodes.getJSONObject(i);
            JSONArray methods = node.getJSONArray("methods");
            JSONArray properties = node.getJSONArray("properties");
            String nodeName = node.getString("name");
            int nodeKey = node.getInt("key");

            for (int j = 0; j < links.length(); j++) {
                JSONObject link = links.getJSONObject(j);
                int from = link.getInt("from");
                int to = link.getInt("to");
                String relationship = link.getString("relationship");

                if (relationship.equals("generalization")) {
                    if (from == nodeKey) {
                        node.put("link", "extends");
                        node.put("parent", keys.get(to));
                    }
                } else if (relationship.equals("aggregation") || relationship.equals("composition")) {
                    if (from == nodeKey) {
                        JSONObject property = new JSONObject();
                        property.put("name", "Link_" + j++);
                        property.put("type", keys.get(to));
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
                    JSONObject parameter = parameters.getJSONObject(k);

                    String parameterGen = getParameter(parameter);

                    if (k > 0) {
                        parameterBuild.append(Template.Separator.PARAMETER);
                    }

                    parameterBuild.append(parameterGen);
                }

                if (j > 0) {
                    methodBuild.append(Template.Separator.METHOD);
                }

                methodBuild.append(methodGen
                        .replace("{{parameters}}", parameterBuild.toString()));
            }

            StringBuilder propertyBuild = new StringBuilder();

            for (int j = 0; j < properties.length(); j++) {
                JSONObject property = properties.getJSONObject(j);

                String propertyGen = getProperty(property);

                if (j > 0) {
                    propertyBuild.append(Template.Separator.PROPERTY);
                }

                propertyBuild.append(propertyGen);
            }

            JSONObject file = new JSONObject();
            file.put("name", nodeName + ".java");
            file.put("content", nodeGen
                    .replace("{{methods}}", methodBuild.toString())
                    .replace("{{properties}}", propertyBuild.toString()));
            files.put(file);
        }

        return files;
    }

    private String getNode(JSONObject node) {
        String name = node.getString("name");
        String type = node.getString("type");
        String visibility = node.has("visibility") ? node.getString("visibility") : "";
        String link = node.has("link") ? node.getString("link") : "";
        String parent = node.has("parent") ? node.getString("parent") : "";

        return Template.NODE
                .replace("{{name}}", name)
                .replace("{{type}}", type)
                .replace("{{visibility}}", visibility)
                .replace("{{link}}", link)
                .replace("{{parent}}", parent);
    }

    private String getMethod(JSONObject method) {
        String name = method.getString("name");
        String type = method.getString("type");
        String visibility = method.getString("visibility");

        return Template.METHOD
                .replace("{{name}}", name)
                .replace("{{type}}", type)
                .replace("{{visibility}}", visibility);
    }

    private String getProperty(JSONObject property) {
        String name = property.getString("name");
        String type = property.getString("type");
        String visibility = property.getString("visibility");

        return Template.PROPERTY
                .replace("{{name}}", name)
                .replace("{{type}}", type)
                .replace("{{visibility}}", visibility);
    }

    private String getParameter(JSONObject property) {
        String name = property.getString("name");
        String type = property.getString("type");

        return Template.PARAMETER
                .replace("{{name}}", name)
                .replace("{{type}}", type);
    }
}
