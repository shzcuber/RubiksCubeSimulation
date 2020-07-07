import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

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

    Face[] edgeFaces;
    int[] edgeLocations;
    public ArrayList<Integer> solveCross(){
        crossSolution = new ArrayList<Integer>();

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
            System.out.println(edgeFaces[i] + " " + edgeLocations[i]);
        }

        System.out.println("Horizontal distance " + checkHorizontalRelationship(VirtualCube.YELLOW, VirtualCube.ORANGE));

        return crossSolution;
    }

    public int findEdges(boolean good){
        //search 2 colors at one, a whole edge
        
        

        //find good edges
        if(good){
            int c=0;
            for(int i=1; i<8 && c<4; i+=2){
                if(downColors[i]==crossColor){ //search all edges in top/bottom
                    edgeFaces[c] = Down;
                    edgeLocations[c++] = i;
                }if(upColors[i]==crossColor){
                    edgeFaces[c] = Up;
                    edgeLocations[c++] = i;
                }       
                if(i==1 || i==7){ //search middle slice
                    if(leftColors[i]==crossColor){
                        edgeFaces[c] = Left;
                        edgeLocations[c++] = i;
                    }if(rightColors[i]==crossColor){
                        edgeFaces[c] = Right;
                        edgeLocations[c++] = i;
                    }if(frontColors[i]==crossColor){
                        edgeFaces[c] = Front;
                        edgeLocations[c++] = i;
                    }if(backColors[i]==crossColor){
                        edgeFaces[c] = Back;
                        edgeLocations[c++] = i;
                    }
                }
            }
            return c;
        }else{ //find all edges
            for(int i=1, c=0; i<8 && c<4; i+=2){
                if(downColors[i]==crossColor){
                    edgeFaces[c] = Down;
                    edgeLocations[c++] = i;
                }if(upColors[i]==crossColor){
                    edgeFaces[c] = Up;
                    edgeLocations[c++] = i;
                }if(leftColors[i]==crossColor){
                    edgeFaces[c] = Left;
                    edgeLocations[c++] = i;
                }if(rightColors[i]==crossColor){
                    edgeFaces[c] = Right;
                    edgeLocations[c++] = i;
                }if(frontColors[i]==crossColor){
                    edgeFaces[c] = Front;
                    edgeLocations[c++] = i;
                }if(backColors[i]==crossColor){
                    edgeFaces[c] = Back;
                    if(i==1){
                        edgeLocations[c++]=7;
                    }else if(i==7){
                        edgeLocations[c++]=1;
                    }else{
                        edgeLocations[c++] = i;
                    }
                }
            }
        }
        return 0;
    }

    public int checkHorizontalRelationship(Color c1, Color c2){ //-1 = left from color, 1 means right, 2 means 180, 0 means equal, -5 = N/A
        if(c1==c2){
            return 0;
        }

        if(c1==Up.getColors()[4] || c1==Down.getColors()[4] || c2==Up.getColors()[4] || c2 ==Down.getColors()[4]){
            return -5;
        }

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