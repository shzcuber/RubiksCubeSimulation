import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LBLSolver {
    private ArrayList<Integer> entireSolution, crossSolution, cornerSolution, secondLayerSolution, OLLSolution, PLLSolution;
    private ArrayList<ArrayList<Integer>> combinedSolutions;
    private Face Front, Back, Up, Down, Left, Right; //getting the faces to check move potential lookahead
    private Color[] frontColors, upColors,downColors, leftColors, rightColors, backColors;
    private Color crossColor;
    private HashMap<String, Integer> moves;
    private HashMap<Integer, String> reverseMoves;

    public LBLSolver(Face[] cubeFaces, Color crossColor){
        this.crossColor = crossColor;

        updateFaces(cubeFaces);
        this.moves = cubeScrambler.moves;
        this.reverseMoves = cubeScrambler.reverseMoves;

        putCrossOnButtom();
    }

    public ArrayList<Integer> getSolution(){
        entireSolution = new ArrayList<Integer>();
        
        solveCross();

        combinedSolutions = new ArrayList<ArrayList<Integer>>(Arrays.asList(
            crossSolution, cornerSolution, secondLayerSolution, OLLSolution, PLLSolution
        ));

        // for(ArrayList<Integer> solution: combinedSolutions){
        //     for(Integer i : solution){
        //         entireSolution.add(i);
        //     }
        // } 

        return entireSolution;
    }

    Edge[] crossEdges;
    Face[] edgeFaces;
    int[] edgeLocations;
    public ArrayList<Integer> solveCross(){
        crossSolution = new ArrayList<Integer>();

        crossEdges = new Edge[4];

        Edge edge;
        
        while((edge=findEdge(true)) != null){
            if(!edge.isSolved()){
                //System.out.println(edge.toString());
                ArrayList<Integer> insertEdgeSolution = insertEdge(edge);
                for(int i : insertEdgeSolution){
                    crossSolution.add(i);
                    VirtualCube.turnCube(i);
                    System.out.print(reverseMoves.get(i) + " ");
                }
            }
        }
        System.out.println("//good edges solved");

        //BAD EDGE UNDER DEVELOPMENT
        edge=findEdge(false);
        while((edge=findEdge(false)) != null){
            if(!edge.isSolved()){
                //System.out.println(edge.toString());
                int alreadyDone = edge.isGood ? 0 : 1;
                ArrayList<Integer> insertEdgeSolution = insertEdge(edge);
                for(int i : insertEdgeSolution){
                    crossSolution.add(i);
                    if(alreadyDone--<1){
                        VirtualCube.turnCube(i);
                    }
                    System.out.print(reverseMoves.get(i) + " ");
                }
            }
        }
        
        System.out.println("//bad edges solved");

        // for(int i=0; i<4; i++){
        //     Edge e = crossEdges[i];
        //     System.out.println(e.a + " " + e.aFace + " " + e.aLocation + " " + e.b + " " + e.bFace + " " + e.bLocation);
        // }

        //System.out.println("Horizontal distance " + checkHorizontalRelationship(VirtualCube.YELLOW, VirtualCube.ORANGE));

        return crossSolution;
    }

    

    private ArrayList<Integer> insertEdge(Edge e){
        boolean isGood = e.isGood;
        ArrayList<Integer> insertEdgeSolution = new ArrayList<>();
        int loc = e.aLocation;
        if(isGood){
            //System.out.println(loc + " " + e.aFace.toString() + " " + e.bFace.toString());
            if(e.aFace == Down){
                if(loc==1){
                    insertEdgeSolution.add(moves.get("L2"));
                }else if(loc==3){
                    insertEdgeSolution.add(moves.get("F2"));
                }else if(loc==5){
                    insertEdgeSolution.add(moves.get("B2"));
                }else if(loc==7){
                    insertEdgeSolution.add(moves.get("R2"));
                }
                return insertEdgeSolution;
            }
            int alignment = checkHorizontalRelationship(e.b, e.bFace.getCenterColor());
            switch(alignment){
                case -1:
                    insertEdgeSolution.add(moves.get("D'"));
                    insertEdgeSolution.add(insert(e));
                    insertEdgeSolution.add(moves.get("D"));
                    break;
                case 0:
                    insertEdgeSolution.add(insert(e));
                    break;
                case 1:
                    insertEdgeSolution.add(moves.get("D"));
                    insertEdgeSolution.add(insert(e));
                    insertEdgeSolution.add(moves.get("D'"));
                    break;
                case 2:
                    insertEdgeSolution.add(moves.get("D2"));
                    insertEdgeSolution.add(insert(e));
                    insertEdgeSolution.add(moves.get("D2"));
                    break;
                
            }   
        }else{
            Face f = e.aFace;
            e.aLocation = 7;
            e.bLocation = 1;
            e.isGood = true;
            if(f==Front){
                insertEdgeSolution.add(moves.get("F"));
                VirtualCube.turnCube(moves.get("F"));
                e.bFace = Right;
                e.aFace = Front;
                insertEdgeSolution.addAll(insertEdge(e));
                if(checkHorizontalRelationship(e.b, f.getCenterColor())!=0){
                    insertEdgeSolution.add(moves.get("F'"));
                }
            }else if(f==Back){
                insertEdgeSolution.add(moves.get("B"));
                VirtualCube.turnCube(moves.get("B"));
                e.bFace = Left;
                insertEdgeSolution.addAll(insertEdge(e));
                if(checkHorizontalRelationship(e.b, f.getCenterColor())!=0){
                    insertEdgeSolution.add(moves.get("B'"));
                }
            }else if(f==Right){
                insertEdgeSolution.add(moves.get("R"));
                VirtualCube.turnCube(moves.get("R"));
                e.bFace = Back;
                insertEdgeSolution.addAll(insertEdge(e));
                if(checkHorizontalRelationship(e.b, e.aFace.getCenterColor())!=0){
                    insertEdgeSolution.add(moves.get("R'"));
                }
            }else if(f==Left){
                insertEdgeSolution.add(moves.get("L"));
                VirtualCube.turnCube(moves.get("L"));
                e.bFace = Front;
                insertEdgeSolution.addAll(insertEdge(e));
                if(checkHorizontalRelationship(e.b, e.aFace.getCenterColor())!=0){
                    insertEdgeSolution.add(moves.get("L'"));
                }
            }
        }
        return insertEdgeSolution;
    }

    public int insert(Edge e){
        Face f = e.aFace;
        int loc = e.aLocation;
        if(f==Front){
            if(loc==1){
                return moves.get("L");
            }else{
                return moves.get("R'");
            }
        }else if(f==Back){
            if(loc==7){
                return moves.get("L'");
            }else{
                return moves.get("R");
            }
        }else if(f==Left){
            if(loc==1){
                return moves.get("B");
            }else{
                return moves.get("F'");
            }
        }else if(f==Right){
            if(loc==7){
                return moves.get("B'");
            }else{
                return moves.get("F");
            }
        }else if(f==Up){
            if(loc==1){
                return moves.get("L2");
            }else if(loc==3){
                return moves.get("B2");
            }else if(loc==5){
                return moves.get("F2");
            }else if(loc==7){
                return moves.get("R2");
            }
        }
        return 0;
    }

    public Edge findEdge(boolean good){
        Edge e;
        for(int i=1; i<8; i+=2){
            if(downColors[i]==crossColor){ //search all edges in top/bottom
                e = new Edge(downColors[i], Down, i, getMatchingEdge(Down, i),true);
                if(e.isSolved()==false){
                    return e;
                }
            }if(upColors[i]==crossColor){
                e = new Edge(upColors[i], Up, i, getMatchingEdge(Up, i), true);
                if(e.isSolved()==false){
                    return e;
                }
            }       
            if(!good || i==1 || i==7){ //search middle slice
                if(leftColors[i]==crossColor){
                    e = new Edge(leftColors[i], Left, i, getMatchingEdge(Left, i));
                    if(i==1 || i==7){
                        e.isGood = true;
                    }
                    if(e.isSolved()==false){
                        return e;
                    }
                }if(rightColors[i]==crossColor){
                    e = new Edge(rightColors[i], Right, i, getMatchingEdge(Right, i));
                    if(i==1 || i==7){
                        e.isGood = true;
                    }
                    if(e.isSolved()==false){
                        return e;
                    }
                }if(frontColors[i]==crossColor){
                    e = new Edge(frontColors[i], Front, i, getMatchingEdge(Front, i));
                    if(i==1 || i==7){
                        e.isGood = true;
                    }
                    if(e.isSolved()==false){
                        return e;
                    }
                }if(backColors[i]==crossColor){
                    int j;
                    if(i==1){
                        j=7;
                    }else if(i==7){
                        j=1;
                    }else{
                        j=i;
                    }
                    e = new Edge(backColors[i], Back, j, getMatchingEdge(Back, j));
                    if(i==1 || i==7){
                        e.isGood = true;
                    }
                    if(e.isSolved()==false){
                        return e;
                    }
                }
            }
        }
        return null;
    }

    public int findEdges(boolean good){
        int c=0;
        //find good edges
        if(good){
            for(int i=1; i<8 && c<4; i+=2){
                if(downColors[i]==crossColor){ //search all edges in top/bottom
                    crossEdges[c++] = new Edge(downColors[i], Down, i, getMatchingEdge(Down, i),true);
                }if(upColors[i]==crossColor){
                    crossEdges[c++] = new Edge(upColors[i], Up, i, getMatchingEdge(Up, i), true);
                }       
                if(i==1 || i==7){ //search middle slice
                    if(leftColors[i]==crossColor){
                        crossEdges[c++] = new Edge(leftColors[i], Left, i, getMatchingEdge(Left, i), true);
                    }if(rightColors[i]==crossColor){
                        crossEdges[c++] = new Edge(rightColors[i], Right, i, getMatchingEdge(Right, i), true);
                    }if(frontColors[i]==crossColor){
                        crossEdges[c++] = new Edge(frontColors[i], Front, i, getMatchingEdge(Front, i), true);
                    }if(backColors[i]==crossColor){
                        int j;
                        if(i==1){
                            j=7;
                        }else if(i==7){
                            j=1;
                        }else{
                            j=i;
                        }
                        crossEdges[c++] = new Edge(backColors[i], Back, j, getMatchingEdge(Back, j), true);
                    }
                }
            }
            return c;
        }else{ //find all edges
            for(int i=1; i<8 && c<4; i+=2){
                if(downColors[i]==crossColor){
                    crossEdges[c++] = new Edge(downColors[i], Down, i, getMatchingEdge(Down, i));
                }if(upColors[i]==crossColor){
                    crossEdges[c++] = new Edge(upColors[i], Up, i, getMatchingEdge(Up, i));
                }if(leftColors[i]==crossColor){
                    crossEdges[c++] = new Edge(leftColors[i], Left, i, getMatchingEdge(Left, i));
                }if(rightColors[i]==crossColor){
                    crossEdges[c++] = new Edge(rightColors[i], Right, i, getMatchingEdge(Right, i));
                }if(frontColors[i]==crossColor){
                    crossEdges[c++] = new Edge(frontColors[i], Front, i, getMatchingEdge(Front, i));
                }if(backColors[i]==crossColor){
                    int j;
                    if(i==1){
                        j=7;
                    }else if(i==7){
                        j=1;
                    }else{
                        j=i;
                    }
                    crossEdges[c++] = new Edge(backColors[i], Back, j, getMatchingEdge(Back, j));
                }
            }
        }
        return c;
    }

    public Edge getMatchingEdge(Face f, int pos){
        HashMap<Face, Color> correspondingTopEdgeColor = new HashMap<Face, Color>();
        correspondingTopEdgeColor.put(Left, upColors[1]);
        correspondingTopEdgeColor.put(Back, upColors[3]);
        correspondingTopEdgeColor.put(Front, upColors[5]);
        correspondingTopEdgeColor.put(Right, upColors[7]);

        HashMap<Face, Color> correspondingBottomEdgeColor = new HashMap<Face, Color>();
        correspondingBottomEdgeColor.put(Left, downColors[1]);
        correspondingBottomEdgeColor.put(Front, downColors[3]);
        correspondingBottomEdgeColor.put(Back, downColors[5]);
        correspondingBottomEdgeColor.put(Right, downColors[7]);

        if(f==Up || f==Down){
            return getVerticalEdge(f==Up ? 'U' : 'D', pos);
        }else{
            if(pos==1){
                if(f==Left){
                    return new Edge(getFaceToTheRight(f).getColors()[1], getFaceToTheRight(f), 1);
                }
                return new Edge(getFaceToTheLeft(f).getColors()[7], getFaceToTheLeft(f), 7);
            }else if(pos==7){
                if(f==Right){
                    return new Edge(getFaceToTheRight(f).getColors()[7], getFaceToTheRight(f), 1);
                }
                return new Edge(getFaceToTheRight(f).getColors()[1], getFaceToTheRight(f), 1);
            }else if(pos==3){
                return new Edge(correspondingTopEdgeColor.get(f), Up, 3);
            }else if(pos==5){
                return new Edge(correspondingBottomEdgeColor.get(f), Down, 5);
            }
        }
        return null;
    }

    public Edge getVerticalEdge(char face, int pos){
        int i = face=='U' ? 3 : 5;
        if(pos==1){
            return new Edge(leftColors[i], Left, i);
        }else if(pos==7){
            return new Edge(rightColors[i], Right, i);
        }else if(pos==3){
            if(face=='U'){return new Edge(backColors[i], Back, i);}
            return new Edge(frontColors[i], Front, i);
        }else if(pos==5){
            if(face=='U'){return new Edge(frontColors[i], Front, i);}
            return new Edge(backColors[i], Back, i);
        }
        return null;
    }

    private Face getFaceToTheLeft(Face f){
        Face[] horizontalFaces = {Front, Left, Back, Right, Front};
        int i=0;
        while(horizontalFaces[i++]!=f){}
        return horizontalFaces[i];
    }

    private Face getFaceToTheRight(Face f){
        Face[] horizontalFaces = {Front, Right, Back, Left, Front};
        int i=0;
        while(horizontalFaces[i++]!=f){}
        return horizontalFaces[i];
    }

    public int checkHorizontalRelationship(Color c1, Color c2){ //-1 = left from color, 1 means right, 2 means 180, 0 means equal, -5 = N/A
        if(c1==c2){return 0;}
        if(c1==Up.getColors()[4] || c1==Down.getColors()[4] || c2==Up.getColors()[4] || c2 ==Down.getColors()[4]){return -5;}
        
        Color[] horizontalLayer = 
            {Front.getColors()[4], Right.getColors()[4], Back.getColors()[4], Left.getColors()[4]};
        int CWDistance=0;   
       
        //find matching color
        boolean foundFirst = false;
        for(int i=0; ; i++){
            if(foundFirst){
                CWDistance++;
                if(horizontalLayer[i%4]==c2){
                    break;
                }
            }
            if(horizontalLayer[i%4]==c1){
                foundFirst=true;
            }
        }
        
        return CWDistance==3 ? -1 : CWDistance;
    }

    public void putCrossOnButtom(){
        if(crossColor==Up.getColors()[4]){
            VirtualCube.rotateCube(40); //down
        }if(crossColor==Left.getColors()[4]){
            VirtualCube.rotateCube(39); //right
        }if(crossColor==Right.getColors()[4]){
            VirtualCube.rotateCube(37); //left
        }if(crossColor==Back.getColors()[4]){
            VirtualCube.rotateCube(37); //left
            VirtualCube.rotateCube(37);
        }if(crossColor==Front.getColors()[4]){
            VirtualCube.rotateCube(40); //down
        }
        updateFaces(VirtualCube.getFaces());
    }

    private void updateFaces(Face[] cubeFaces){
        Front = cubeFaces[0];
        frontColors = Front.getColors();

        Right = cubeFaces[1];
        rightColors = Right.getColors();

        Back = cubeFaces[2];
        backColors = Back.getColors();

        Left = cubeFaces[3];
        leftColors = Left.getColors();

        Up = cubeFaces[4];
        upColors = Up.getColors();

        Down = cubeFaces[5];
        downColors = Down.getColors();
    }

}