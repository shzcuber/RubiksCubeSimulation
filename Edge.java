import java.awt.Color;

public class Edge {
    public Color a, b;
    public Face aFace, bFace;
    public int aLocation, bLocation;
    public boolean isGood;

    Edge(Color c, Face f, int location) {
        this.a = c;
        this.aFace = f;
        this.aLocation = location;
    }

    Edge(Color c, Face f, int location, Edge edge) {
        this.a = c;
        this.aFace = f;
        this.aLocation = location;
        this.b = edge.a;
        this.bFace = edge.aFace;
        this.bLocation = edge.aLocation;
    }

    Edge(Color c, Face f, int location, Edge edge, boolean isGood) {
        this.a = c;
        this.aFace = f;
        this.aLocation = location;
        this.b = edge.a;
        this.bFace = edge.aFace;
        this.bLocation = edge.aLocation;
        this.isGood = isGood;
    }

    Edge(Color a, Face aFace, int aLocation, Color b, Face bFace, int bLocation) {
        this.a = a;
        this.aFace = aFace;
        this.aLocation = aLocation;
        this.b = b;
        this.bFace = bFace;
        this.bLocation = bLocation;
    }

    public void add(Color b, Face bFace, int bLocation) {
        this.b = b;
        this.bFace = bFace;
        this.bLocation = bLocation;
    }

    public boolean isSolved(){
        if(a==aFace.getColors()[4] && b==bFace.getColors()[4]){
            return true;
        }
        return false;
    }

    

    public void add(Edge edge) {
        this.b = edge.a;
        this.bFace = edge.aFace;
        this.bLocation = edge.aLocation;
    }
}
