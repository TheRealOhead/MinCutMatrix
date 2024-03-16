package org.owenparker;

import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class MatrixGraph {
    private int[][] matrix;
    private int size;
    private Random random = new Random();
    public MatrixGraph(int size) {
        matrix = new int[size][size];
        this.size = size;
        clear();
    }

    /**
     * Sets the weight of an edge in the graph
     * @param u Vertex U
     * @param v Vertex V
     * @param weight Weight to set the edge to. Use 0 for no edge
     */
    public void setEdge(int u, int v, int weight) {
        matrix[u][v] = matrix[v][u] = weight;
    }

    /**
     * Returns the weight of the edge between the given vertices
     * @param u Vertex U
     * @param v Vertex V
     * @return The weight between the vertices, or 0 if there is no edge present
     */
    public int getEdge(int u, int v) {
        return matrix[u][v];
    }

    /**
     * Destroys all edges in the graph
     */
    private void clear() {
        for (int[] row : matrix) {
            Arrays.fill(row, 0);
        }
    }

    /**
     * Merges two gives vertices
     * @param u First vertex to merge
     * @param v Second vertex to merge
     */
    public void mergeVertices(int u, int v) {
        for (int index = 0; index < size; index++) {
            // Add all of v's edges to u's edges
            setEdge(u, index, (
                    getEdge(u, index) + getEdge(v, index)
            ));

            // Set self edge to 0
            if (index == u) {
                setEdge(u, index, 0);
            }

            // Clear all of V's edges
            setEdge(v, index, 0);
        }
    }

    /**
     * Finds two random vertices that are adjacent and merges them using the mergeVertices() method
     */
    public void mergeTwoRandomVertices() {
        int u;
        int v;

        do {
            u = (int) (random.nextDouble() * size);
            v = (int) (random.nextDouble() * size);
        } while (u == v || getEdge(u, v) == 0);

        mergeVertices(u, v);
    }

    /**
     * Returns the amount of vertices in the graph
     * @return Amount of vertices
     */
    public int getVertexCount() {
        int sum = 0;
        int rowIndex = 0;
        for (int[] row : matrix) {
            int edgeIndex = 0;
            for (int edge : row) {
                if (edge > 0 && edgeIndex != rowIndex) {
                    sum++;
                    break;
                }
                edgeIndex++;
            }
            rowIndex++;
        }
        return sum;
    }

    /**
     * Repeatedly applies the merge operation to random vertices until the graph has only two vertices left
     */
    public void squash() throws IOException {
        int step = 0;
        while (getVertexCount() > 2) {
            if (Main.draw && !Main.drawOnlyLast) draw(step + ".png");
            mergeTwoRandomVertices();
            step++;
        }
        if (Main.draw) draw(step + ".png");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int[] row : matrix) {
            for (int edge : row) {
                result.append(edge).append(", ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int getMinCut() throws NoValidEdgePresentException, IOException {
        squash();

        int rowIndex = 0;
        for (int[] row : matrix) {
            int edgeIndex = 0;
            for (int edge : row) {
                if (edge > 0 && edgeIndex != rowIndex) {
                    return edge;
                }
                edgeIndex++;
            }
            rowIndex++;
        }
        throw new NoValidEdgePresentException("Vertex count: " + this.getVertexCount());
    }

    private MatrixGraph(int[][] matrix) {
        this.size = matrix.length;
        this.matrix = matrix;
    }

    @Override
    public MatrixGraph clone() {
        int[][] newMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            newMatrix[i] = matrix[i].clone();
        }

        return new MatrixGraph(newMatrix);
    }

    private String generateGraphString() {
        StringBuilder result = new StringBuilder("Graph {\n");

        for (int u = 0; u < size; u++) {
            for (int v = u; v < size; v++) {
                for (int i = 0; i < getEdge(u, v); i++) {
                    result.append(u).append(" -- ").append(v).append("\n");
                }
            }
        }

        result.append("}\n");

        return result.toString();
    }

    public void draw(String fileName) throws IOException {
        MutableGraph g = new Parser().read(generateGraphString());
        Graphviz.fromGraph(g).engine(Engine.FDP).render(Format.PNG).toFile(new File("./graphImages/" + fileName));
    }

    public int getSize() {
        return size;
    }
}
