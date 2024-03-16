package org.owenparker.test;

import org.junit.Assert;
import org.junit.Test;
import org.owenparker.Main;
import org.owenparker.MatrixGraph;

import java.io.IOException;

public class GraphTests {

    public MatrixGraph getSimpleGraph() {
        MatrixGraph graph = new MatrixGraph(6);

        graph.setEdge(1, 2, 1);
        graph.setEdge(1, 3, 2);
        graph.setEdge(2, 3, 3);
        graph.setEdge(4, 2, 4);
        graph.setEdge(4, 3, 5);

        return graph;
    }
    @Test
    public void createGraphTest() {
        MatrixGraph graph = new MatrixGraph(6);
        graph.setEdge(1, 5, 1);
        Assert.assertEquals(graph.getEdge(1, 5), 1);
    }

    @Test
    public void vertexCountTest() {
        MatrixGraph graph = new MatrixGraph(6);
        graph.setEdge(1, 5, 2);
        graph.setEdge(5, 2, 1);
        graph.setEdge(2, 1, 500);
        Assert.assertEquals(graph.getVertexCount(), 3);
    }

    @Test
    public void mergeTest() {
        MatrixGraph graph = getSimpleGraph();

        graph.mergeVertices(2, 3);

        Assert.assertEquals(graph.getEdge(1, 2), 3);
        Assert.assertEquals(graph.getEdge(4, 2), 9);

        Assert.assertEquals(graph.getEdge(2, 1), 3);
        Assert.assertEquals(graph.getEdge(2, 4), 9);

        Assert.assertEquals(graph.getVertexCount(), 3);
    }

    @Test
    public void randomMergeTest() throws IOException {
        MatrixGraph graph = Main.getGraph();

        for (int i = 0; i < 10; i++) {
            int before = graph.getVertexCount();

            graph.mergeTwoRandomVertices();

            int after = graph.getVertexCount();

            Assert.assertEquals(before - 1, after);
        }
    }

    @Test
    public void listEdges() throws IOException {
        MatrixGraph graph = Main.getGraph();

        for (int u = 0; u < graph.getSize(); u++) {
            for (int v = 0; v < graph.getSize(); v++) {
                int edgeValue = graph.getEdge(u, v);

                if (edgeValue != 0) {
                    System.out.printf("%d -- %d == %d%n", u, v, edgeValue);
                }
            }
        }
    }
}
