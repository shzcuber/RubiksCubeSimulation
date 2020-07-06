import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class LBLSolver {
    private ArrayList<Integer> entireSolution, crossSolution, cornerSolution, secondLayerSolution, OLLSolution, PLLSolution;
    private ArrayList<ArrayList<Integer>> combinedSolutions;
    private Face Front, Back, Up, Down, Left, Right; //getting the faces to check move potential lookahead
    private Color[] frontColors, upColors,downColors, leftColors, rightColors, backColors;

    public LBLSolver(Face[] cubeFaces){
        Front = cubeFaces[0];
        frontColors = Front.getColors();

        Back = cubeFaces[1];
        backColors = Back.getColors();

        Up = cubeFaces[2];
        upColors = Up.getColors();

        Down = cubeFaces[3];
        downColors = Down.getColors();

        Left = cubeFaces[4];
        leftColors = Left.getColors();

        Right = cubeFaces[5];
        rightColors = Right.getColors();
    }

    public ArrayList<Integer> getSolution(Color crossColor){
        entireSolution = new ArrayList<Integer>();
        
        solveCross(crossColor);

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
    public ArrayList<Integer> solveCross(Color crossColor){
        crossSolution = new ArrayList<Integer>();

        findEdges(crossColor);
        for(int i=0; i<4; i++){
            System.out.println(edgeFaces[i] + " " + edgeLocations[i]);
        }

        return crossSolution;
    }

    public void findEdges(Color crossColor){
        //search 2 colors at one, a whole edge
        
        edgeFaces = new Face[4];
        edgeLocations = new int[4];

            //search bottom layer
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
                edgeLocations[c++] = i;
            }
        }
            //search middle layer
            //search top layer
    }


}