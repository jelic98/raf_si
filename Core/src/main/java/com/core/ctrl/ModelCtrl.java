package com.core.ctrl;

import com.core.domain.Element;
import com.core.domain.Model;
import com.core.domain.dao.ElementDao;
import com.core.domain.dao.ModelDao;
import com.core.domain.dto.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "/models")
@RequiredArgsConstructor
public class ModelCtrl {

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private ElementDao elementDao;

    @GetMapping("/all")
    public List<Model> getModel(@RequestParam String project){
        String proj = project.substring(0, project.indexOf(','));
        System.out.println(proj);
        List<Model> models = modelDao.findAll();
        List<Model> modelsRet = new ArrayList<>();

        for(Model m : models){
            if(m.get_id().getProject() == null) continue;
            if(m.get_id().getProject().equals(proj))
                modelsRet.add(m);
        }
        return modelsRet;
    }
    @GetMapping("")
    public Model getModel(@RequestParam String project, @RequestParam String model){
        System.out.println(project + " " + model);
        String name = model;
        Model m = modelDao.findOneBy_id(name.substring(0, name.indexOf(',')), project.substring(0, project.indexOf(',')));
        //System.out.println(m);
        /*List<Object> details = new ArrayList<>();
        if(m.getDetails() != null)
            details = ((List<Object>) m.getDetails());*/
        System.out.println(m);
        return m;
    }

    @PostMapping("")
    public Model postModel(@ModelAttribute ModelReqDto dto){
        Model m = new Model(dto);
        modelDao.insert(m);
        return m;
    }

    @GetMapping("/activeUsers")
    public Set<String> getActiveUsers(@RequestParam String project, @RequestParam String model){
        Set<String> users = new HashSet<>();
        String name = model;
        Model m = modelDao.findOneBy_id(name.substring(0, name.indexOf(',')), project.substring(0, project.indexOf(',')));
        String details = m.getDetails().toString();
        String search = "\"editing\":\"";
        int index = details.indexOf(search);
        while(index >= 0){
            users.add(details.substring(index + search.length(), details.indexOf("\"", index + search.length())));
            index = details.indexOf(search, index + 1);
        }

        if(users.contains(""))
            users.remove("");
        return users;
    }

    @GetMapping("/select")
    public boolean selectNode(@RequestParam String project, @RequestParam String model, @RequestParam String nodeId, @RequestParam String user){
        return select(model, project, nodeId.substring(0, nodeId.indexOf(',')), user.substring(0, user.indexOf(',')),  false);
    }

    @GetMapping("/deselect")
    public boolean deselectNode(@RequestParam String project, @RequestParam String model, @RequestParam String user){
        return select(model, project, null, user.substring(0, user.indexOf(',')), true);
    }

    @PutMapping("")
    public Model updateModel(@ModelAttribute ModelPutReqDto dto){
        Model m = modelDao.findOneBy_id(dto.getModel(), dto.getProject());
        System.out.println(dto + "\n" + m);
        System.out.println(dto.getDetails());
        if(m == null)
            return modelDao.findOneBy_id(dto.getModel(), dto.getProject());
        m.setDetails(dto.getDetails());
        modelDao.save(m);
        return m;
    }

    @DeleteMapping("")
    public void deleteModel(@ModelAttribute ModelGetReqDto dto){
        modelDao.deleteBy_id(dto.getName(), dto.getProject());
    }

    private boolean select(String model, String project, String nodeId, String user, boolean deselect){
        String name = model;
        boolean ret = false;
        Model m = modelDao.findOneBy_id(name.substring(0, name.indexOf(',')), project.substring(0, project.indexOf(',')));
        System.out.println(nodeId + ": "+ user);
        if(m.getType().equals("requirements")){
            return selectRqm(model, project, (m.getDetails()).toString(), nodeId, user, deselect);
        }
        JSONObject nodesAndLinks = new JSONObject((m.getDetails()).toString());
        JSONArray nodes = (JSONArray) nodesAndLinks.get("nodes");
        JSONArray links = (JSONArray) nodesAndLinks.get("links");
        ArrayList<String> details = new ArrayList<>();
        for (int i = 0; i < nodes.length(); i++) {
            JSONObject node = new JSONObject(nodes.get(i).toString());


            if(deselect){
                try {
                    if (node.get("editing").toString().equals(user)) {
                        node.put("editing", "");
                        ret = true;
                    }
                } catch (JSONException e) {
                    node.put("editing", "");
                }
            }else {
                if(node.get("key").toString().equals(nodeId)) {
                    try {
                        if (node.get("editing").toString().isEmpty()) {
                            node.put("editing", user);
                            System.out.println(node);
                            ret = true;
                        }
                    } catch (JSONException e) {
                        node.put("editing", user);
                        ret = true;
                    }
                }
            }
            details.add(node.toString());
        }
        if(ret) {
            ModelPutReqDto mdl = new ModelPutReqDto(name.substring(0, name.indexOf(','))
                    , project.substring(0, project.indexOf(','))
                    , "{\"nodes\":" + details + ",\"links\":" + links + "}");
            updateModel(mdl);
            System.out.println(mdl);
        }
        return ret;
    }

    private boolean selectRqm(String model, String project, String details, String nodeId, String user, boolean deselect){
        boolean ret = false;
        if(deselect){
            String newDetails = details.replaceAll("\"editing\":\"" + user + "\"", "\"editing\":\"\"");
            ret = !newDetails.equals(details);
            if(ret){
                ModelPutReqDto mdl = new ModelPutReqDto(model.substring(0, model.indexOf(','))
                        , project.substring(0, project.indexOf(','))
                        , newDetails);
                updateModel(mdl);
                System.out.println(mdl);
            }
            return ret;
        }
        JSONObject reqsAndActors = new JSONObject(details);
        JSONArray reqs = (JSONArray) reqsAndActors.get("requirements");
        JSONArray actors = (JSONArray) reqsAndActors.get("actors");
        ArrayList<String> indexes = new ArrayList<>(Arrays.asList(nodeId.split("\\.")));

        JSONObject node = (JSONObject) reqs.get(Integer.parseInt(indexes.get(0)) - 1);
        ArrayList<JSONArray> tmp = new ArrayList<>();
        tmp.add(reqs);
        for (int i = 1; i < indexes.size(); i++) {
            tmp.add((JSONArray) node.get("children"));
            node = (JSONObject) tmp.get(i).get(Integer.parseInt(indexes.get(i)) - 1);
        }

        if(deselect){
            try {
                if (node.get("editing").toString().equals(user)) {
                    node.put("editing", "");
                    ret = true;
                }
            } catch (JSONException e) {
                node.put("editing", "");
            }
        }else {
            try {
                if (node.get("editing").toString().isEmpty()) {
                    node.put("editing", user);
                    System.out.println(node);
                    ret = true;
                }
            } catch (JSONException e) {
                node.put("editing", user);
                ret = true;
            }
        }
//        tmp.get(indexes.size() - 1).put(Integer.parseInt(indexes.get(indexes.size() - 1)) - 1, node);
//        for (int i = indexes.size() - 2; i > 0; i++) {
//            JSONArray newNode = tmp.get(i + 1);
//            tmp.get(i).put(Integer.parseInt(indexes.get(i)) - 1, newNode);
//        }
        if(ret) {
            ModelPutReqDto mdl = new ModelPutReqDto(model.substring(0, model.indexOf(','))
                    , project.substring(0, project.indexOf(','))
                    , "{\"requirements\":" + reqs + ",\"actors\":" + actors + "}");
            updateModel(mdl);
            System.out.println(mdl);
        }
        return ret;
    }
}
