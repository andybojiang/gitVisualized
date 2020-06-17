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
    public static CytoscapeObj newNode(String id, String parent) {
        CytoscapeObj node = new CytoscapeObj();
        node.data.put("id", id);
        node.data.put("parent", parent);
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

    private Map<String, String> data = new HashMap<>();
    private static int edgeCount = 0;
}
