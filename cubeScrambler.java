import java.util.HashMap;
import java.awt.event.KeyEvent;
import java.util.Random;

public class cubeScrambler {
    private static char[] possibleMoves = new char[5];
    private static char[] moveType = new char[2];
    private static HashMap<String, Integer> moves = new HashMap<String, Integer>();
    private static int[] scram;

    public cubeScrambler(){
        setMoves();
    }
    //generates random moves to pass to VirtualCube.java
    public int[] generateScramble(int len){
        String move;
        scram = new int[len];
        Random rand = new Random();
        int prev=0;

        for(int i=0; i<len; i++){
            int randMove, randType;
            do{
                randMove = rand.nextInt(5);
            }while(isRedundant(i, randMove, prev));

            prev = randMove;

            randType = rand.nextInt(3);
            if(randType<2){
                move = "" + possibleMoves[randMove] + moveType[randType];
            }else{
                move = "" + possibleMoves[randMove];
            }
            
            System.out.print(move + " ");

            scram[i] = moves.get(move);
        }
        return scram;
    }

    private static boolean isRedundant(int i, int randMove,int prev){
        char a = possibleMoves[randMove], b = possibleMoves[prev];
        if(i>0 && randMove == prev || (i>1 && a==scram[i-2] && matches(a, b))){
            return true;
        }
        return false;
    }

    private static boolean matches(char a, char b){
        if( (a=='R' && b == 'L')||
            (a=='L' && b == 'R')||
            (a=='D' && b == 'U')||
            (a=='U' && b == 'D')||
            (a=='B' && b == 'F')||
            (a=='F' && b == 'B')){
                return true;
        }
            return false;
     }
    public static void setMoves(){
        moves.put("R", KeyEvent.VK_I);
        moves.put("R'", KeyEvent.VK_K);
        moves.put("R2", 2);

        moves.put("L", KeyEvent.VK_D);
        moves.put("L'", KeyEvent.VK_E);
        moves.put("L2", 3);

        moves.put("U", KeyEvent.VK_U);
        moves.put("U'", KeyEvent.VK_R);
        moves.put("U2", 1);

        moves.put("D", KeyEvent.VK_V);
        moves.put("D'", KeyEvent.VK_N);
        moves.put("D2", 4);

        moves.put("F", KeyEvent.VK_J);
        moves.put("F'", KeyEvent.VK_F);
        moves.put("F2", 5);

        possibleMoves[0] = 'R';
        possibleMoves[1] = 'L';
        possibleMoves[2] = 'U';
        possibleMoves[3] = 'F';
        possibleMoves[4] = 'D';

        moveType[0] = '\'';
        moveType[1] = '2';
    }
}