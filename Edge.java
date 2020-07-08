import java.awt.Color;

public class Edge {
    public Color a, b;
    public Face aFace, bFace;
    public int aLocation, bLocation;
    Edge(Color c, Face f, int location){
        this.a = c;
        this.aFace = f;
        this.aLocation = location;
    }
    Edge(Color c, Face f, int location, Edge edge){
        this.a = c;
        this.aFace = f;
        this.aLocation = location;
        this.b = edge.a;
        this.bFace = edge.aFace;
        this.bLocation = edge.aLocation;
    }
    Edge(Color a, Face aFace, int aLocation, Color b, Face bFace, int bLocation){
        this.a = a;
        this.aFace = aFace;
        this.aLocation = aLocation;
        this.b = b;
        this.bFace = bFace;
        this.bLocation = bLocation;
    }

    public void add(Color b, Face bFace, int bLocation){
        this.b = b;
        this.bFace= bFace;
        this.bLocation = bLocation;
    }
    public void add(Edge edge){
        this.b = edge.a;
        this.bFace = edge.aFace;
        this.bLocation = edge.aLocation;
    }
}

