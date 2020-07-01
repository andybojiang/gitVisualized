package com.gitlet.gitVisual.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Objects used in the cytoscape.js library. Includes edges and nodes.
 * @author Andy Jiang
 */
public class CytoscapeObj {


    /**
     * Dummy constructor
     */
    CytoscapeObj() {

    }

    /**
     * Constructor used for a node.
     * @param id unique id assigned to the object. For commits, it will be the hash.
     */
    public static CytoscapeObj newNode(String id) {
        CytoscapeObj node = new CytoscapeObj();
        node.data.put("id", id);
        return node;
    }

    /**
     * Constructor used for a node with a parent.
     * @param id unique id assigned to the object. For commits, it will be the hash.
     * @param parent first parent of the node.
     */
    public static CytoscapeObj newNode(String id, String msg, String time, String parent, String parent2) {
        CytoscapeObj node = new CytoscapeObj();
        node.data.put("msg", msg);
        node.data.put("time", time);
        node.data.put("id", id);
        node.data.put("parent", parent);
        node.data.put("parent2", parent2);
        return node;
    }

    /**
     * Constructor used for a node.
     * @param source id of source node.
     * @param target id of target node.
     */
    public static CytoscapeObj newEdge(String source, String target) {
        CytoscapeObj edge = new CytoscapeObj();
        edge.data.put("id", "e" + edgeCount);
        edgeCount++;
        edge.data.put("source", source);
        edge.data.put("target", target);
        return edge;
    }

    public String getId() {
        if (data.containsKey("id")) {
            return data.get("id");
        }
        return null;
    }
    public String getSource() {
        if (data.containsKey("source")) {
            return data.get("source");
        }
        return null;
    }
    public String getTarget() {
        if (data.containsKey("target")) {
            return data.get("target");
        }
        return null;
    }

    public String getParent() {
        if (data.containsKey("parent")) {
            return data.get("parent");
        }
        return null;
    }
    public String getParent2() {
        if (data.containsKey("parent2")) {
            return data.get("parent2");
        }
        return null;
    }

    public String getTime() {
        if (data.containsKey("time")) {
            return data.get("time");
        }
        return null;
    }

    public String getMsg() {
        if (data.containsKey("msg")) {
            return data.get("msg");
        }
        return null;
    }

    private Map<String, String> data = new HashMap<>();
    private static int edgeCount = 0;
}
