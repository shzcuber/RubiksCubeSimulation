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

    public LBLSolver(Face[] cubeFaces, Color crossColor){
        this.crossColor = crossColor;

        updateFaces(cubeFaces);

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
        edgeFaces = new Face[4];
        edgeLocations = new int[4];
        // int c=findEdges(true);
        // for(int i=0; i<c; i++){
        //     //solve good edges
        // }

        findEdges(false);
        // for(int i=c-1; i<4; i++){
        //     //solve bad edges
        // }

        for(int i=0; i<4; i++){
            Edge edge = crossEdges[i];
            System.out.println(edge.a + " " + edge.aFace + " " + edge.aLocation + " " + edge.b + " " + edge.bFace + " " + edge.bLocation);
        }

        System.out.println("Horizontal distance " + checkHorizontalRelationship(VirtualCube.YELLOW, VirtualCube.ORANGE));

        return crossSolution;
    }

    public void findEdges(boolean good){
        //find good edges
        if(good){
            int c=0;
            for(int i=1; i<8 && c<4; i+=2){
                if(downColors[i]==crossColor){ //search all edges in top/bottom
                    crossEdges[c++] = new Edge(downColors[i], Down, i, getMatchingEdge(Down, i));
                    // edgeFaces[c] = Down;
                    // edgeLocations[c++] = i;
                }if(upColors[i]==crossColor){
                    crossEdges[c++] = new Edge(upColors[i], Up, i, getMatchingEdge(Up, i));
                }       
                if(i==1 || i==7){ //search middle slice
                    if(leftColors[i]==crossColor){
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
                        crossEdges[c++] = new Edge(backColors[j], Back, j, getMatchingEdge(Back, j));
                    }
                }
            }
        }else{ //find all edges
            for(int i=1, c=0; i<8 && c<4; i+=2){
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
                    crossEdges[c++] = new Edge(backColors[j], Back, j, getMatchingEdge(Back, j));
                }
            }
        }
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
                return new Edge(getFaceToTheLeft(f).getColors()[7], getFaceToTheLeft(f), 7);
            }else if(pos==7){
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