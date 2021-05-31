package rs.raf.transformer;

import org.json.JSONArray;
import org.json.JSONObject;

public class Transformer {

    public JSONObject transform(String model) {
        JSONObject fum = new JSONObject();

        JSONObject rqm = new JSONObject(model);
        JSONObject details = rqm.getJSONObject("details");
        JSONArray requirements = details.getJSONArray("requirements");
        JSONArray actors = details.getJSONArray("actors");

        int key = 0;

        JSONArray nodes = new JSONArray();

        for (int i = 0; i < actors.length(); i++) {
            JSONObject actor = actors.getJSONObject(i);

            JSONObject node = new JSONObject();
            node.put("img", "https://cdn.discordapp.com/attachments/558738094795522049/839263865623019551/unknown.png");
            node.put("category", "actor");
            node.put("key", key++);
            node.put("text", actor.getString("name"));

            nodes.put(node);
        }

        addRequirements(requirements, nodes, key);

        JSONObject id = new JSONObject();
        id.put("name", String.format("%s - fum %d", rqm.getJSONObject("_id").getString("name"), System.currentTimeMillis() / 1000L));
        id.put("project", rqm.getJSONObject("_id").getString("project"));

        fum.put("_id", id);
        fum.put("nodes", nodes);

        return fum;
    }

    private void addRequirements(JSONArray requirements, JSONArray nodes, int key) {
        for (int i = 0; i < requirements.length(); i++) {
            JSONObject requirement = requirements.getJSONObject(i);
			System.out.println(requirement.getString("title"));

            boolean functional = requirement.getString("requirement_type").equals("functional");

            if (functional) {
				JSONObject node = new JSONObject();
				node.put("img", "https://cdn.discordapp.com/attachments/558738094795522049/839263865623019551/unknown.png");
				node.put("category", "use_case");
				node.put("key", key++);
				node.put("text", requirement.getString("title"));
            	
				nodes.put(node);
            }

            addRequirements(requirement.getJSONArray("children"), nodes, key);
        }
    }
}
