package com.gitlet.gitVisual.model.repo;


import com.gitlet.gitVisual.model.CytoscapeObj;

import java.util.ArrayList;
import java.util.List;

public class CytoscapeElements {
    public CytoscapeElements() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }


    public void addNode(CytoscapeObj node) {
        nodes.add(node);
    }
    public void addEdge(CytoscapeObj edge) {
        edges.add(edge);
    }

    public List<CytoscapeObj> getNodes() {
        return nodes;
    }

    public List<CytoscapeObj> getEdges() {
        return edges;
    }

    private List<CytoscapeObj> nodes;
    private List<CytoscapeObj> edges;
}
