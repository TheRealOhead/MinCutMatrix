package org.owenparker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static int iterations = 64;
    // private static String graphPath = "./data/kargerMinCut.txt";
    private static String graphPath = "./data/smallGraph.txt";
    public static boolean draw = false;
    public static boolean drawOnlyLast = false;

    public static MatrixGraph getGraph() throws IOException {
        String input = Files.readString(Paths.get(graphPath));
        String[] arrayOfLines = input.split("\n");

        // Get largest entry
        int max = 0;
        String[] rawListOfEntries = input.split("[\t\n]");
        for (String entry : rawListOfEntries) {
            if (entry.isBlank()) continue;
            max = Math.max(Integer.parseInt(entry),max);
        }

        // Create graph with size equal to the largest entry we found plus 1
        MatrixGraph graph = new MatrixGraph(max + 1);

        // Generate the graph from file
        for (String line : arrayOfLines) { // Each line

            if (line.isBlank()) continue;

            String[] vertexStrings = line.split("\t");

            int firstVertex = Integer.parseInt(vertexStrings[0]);

            for (String vertexString : vertexStrings) { // Each vertex

                // Bail if empty string
                if (vertexString.isBlank()) continue;

                int vertex = Integer.parseInt(vertexString);

                // Bail if first
                if (firstVertex == vertex) continue;

                graph.setEdge(firstVertex, vertex, 1);
            }
        }

        return graph;
    }

    public static void main(String[] args) throws IOException, NoValidEdgePresentException {
        MatrixGraph graph = getGraph();

        clearImageFolder();

        // Perform the minimum cut algorithm a number of times equal to "iterations" and
        // output the largest and the smallest result

        int minMinCut = Integer.MAX_VALUE;
        int maxMinCut = Integer.MIN_VALUE;

        for (int i = 0; i < iterations; i++) {
            int minCut = graph.clone().getMinCut();
            minMinCut = Math.min(minMinCut, minCut);
            maxMinCut = Math.max(maxMinCut, minCut);
            System.out.println("Iteration " + (i + 1) + " minCut = " + minCut);
        }

        System.out.println("minMinCut " + minMinCut);
        System.out.println("maxMinCut " + maxMinCut);
    }

    /**
     * Deletes all the images in the image folder
     * @throws FileNotFoundException
     */

    public static void clearImageFolder() throws FileNotFoundException {
        String path = "./graphImages/";
        File[] files = new File(path).listFiles();
        assert files != null;
        for (File file : files) {
            if (!file.delete()) throw new FileNotFoundException();
        }
    }
}