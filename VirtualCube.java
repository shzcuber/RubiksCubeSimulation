import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class VirtualCube {
    private static final boolean debugMode = true;
    private static JFrame frame;
    private static JPanel cubeDisplay;
    private static GridBagConstraints gbc;
    private static Face Front, Back, Up, Down, Left, Right; // back face is never visible
    private static Color[] frontColors, upColors,downColors, leftColors, rightColors, backColors;
    public static final Color 
    WHITE = new Color(255, 255, 255), 
    YELLOW = new Color(255, 255, 0),
    ORANGE = new Color(255, 127, 80), 
    BLUE = new Color(0, 0, 128), 
    RED = new Color(255, 0, 0),
    GREEN = new Color(0, 255, 0);

    public VirtualCube() {
        frame = new JFrame("Virtual Cube");
        frame.setResizable(false);

        cubeDisplay = new JPanel(new GridBagLayout());
        cubeDisplay.setFocusable(true);
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;

        initiateCube();
        addControls();
        addButtons();
        
        frame.setContentPane(cubeDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initiateCube(){
        frontColors = new Color[9]; 
        upColors = new Color[9];
        downColors = new Color[9]; 
        leftColors = new Color[9]; 
        rightColors = new Color[9]; 
        backColors = new Color[9];

        for(int i=0; i<9; i++){
            frontColors[i] = GREEN;
            upColors[i] = WHITE;
            downColors[i] = YELLOW;
            leftColors[i] = ORANGE;
            rightColors[i] = RED;
            backColors[i] = BLUE;
        }

        Back = new Face(backColors);
        Up = new Face(upColors);
        Front = new Face(frontColors);
        Down = new Face(downColors);
        Left = new Face(leftColors);
        Right = new Face(rightColors);

        drawCube();
    }

    private void rotateCube(int direction){
        backColors = Back.getColors();
        switch(direction){
            case KeyEvent.VK_UP://X'
                Back.setColors(Up.getColors());
                Up.setColors(Front.getColors());
                Front.setColors(Down.getColors());
                Down.setColors(backColors);
                Back.shift(Face.HORIZONTAL);
                Down.shift(Face.HORIZONTAL);
                Right.shift(Face.CW);
                Left.shift(Face.CCW);
                break;
            case KeyEvent.VK_DOWN://X
                Back.setColors(Down.getColors());
                Down.setColors(Front.getColors());
                Front.setColors(Up.getColors());
                Up.setColors(backColors);
                Back.shift(Face.HORIZONTAL);
                Up.shift(Face.HORIZONTAL);
                Right.shift(Face.CCW);
                Left.shift(Face.CW);
                break;
            case KeyEvent.VK_LEFT://Y'
                Back.setColors(Left.getColors());
                Left.setColors(Front.getColors());
                Front.setColors(Right.getColors());
                Right.setColors(backColors);
                Back.shift(Face.VERTICAL);
                Right.shift(Face.VERTICAL);
                Up.shift(Face.CW);
                Down.shift(Face.CCW);
                break;
            case KeyEvent.VK_RIGHT://Y
                Back.setColors(Right.getColors());
                Right.setColors(Front.getColors());
                Front.setColors(Left.getColors());
                Left.setColors(backColors);
                Back.shift(Face.VERTICAL);
                Left.shift(Face.VERTICAL);
                Up.shift(Face.CCW);
                Down.shift(Face.CW);
                break;
        }
        drawCube();
    }

    private void turnCube(int direction){
        Color[] backColors;
        Color[] upColors;
        switch(direction){
            case KeyEvent.VK_I: //R 
                backColors = Back.getReverseColorsOn('R');
                Back.setColorsOn('R', Up.getReverseColorsOn('R'));
                Up.setColorsOn('R', Front.getColorsOn('R'));
                Front.setColorsOn('R', Down.getColorsOn('R'));
                Down.setColorsOn('R', backColors);
                Right.shift(Face.CW);
                break;
            case KeyEvent.VK_K: //R'
                backColors = Back.getReverseColorsOn('R');
                Back.setColorsOn('R', Down.getReverseColorsOn('R'));
                Down.setColorsOn('R', Front.getColorsOn('R'));
                Front.setColorsOn('R', Up.getColorsOn('R'));
                Up.setColorsOn('R', backColors);
                Right.shift(Face.CCW);
                break;
            case KeyEvent.VK_E: //L'
                backColors = Back.getReverseColorsOn('L');
                Back.setColorsOn('L', Up.getReverseColorsOn('L'));
                Up.setColorsOn('L', Front.getColorsOn('L'));
                Front.setColorsOn('L', Down.getColorsOn('L'));
                Down.setColorsOn('L', backColors);
                Left.shift(Face.CCW);
                break;
            case KeyEvent.VK_D: //L
                backColors = Back.getReverseColorsOn('L');
                Back.setColorsOn('L', Down.getReverseColorsOn('L'));
                Down.setColorsOn('L', Front.getColorsOn('L'));
                Front.setColorsOn('L', Up.getColorsOn('L'));
                Up.setColorsOn('L', backColors);
                Left.shift(Face.CW);
                break;
            case KeyEvent.VK_R: //U'
                backColors = Back.getReverseColorsOn('U');
                Back.setColorsOn('U',Right.getReverseColorsOn('U'));
                Right.setColorsOn('U',Front.getColorsOn('U'));
                Front.setColorsOn('U',Left.getColorsOn('U'));
                Left.setColorsOn('U',backColors);
                Up.shift(Face.CCW);
                break;
            case KeyEvent.VK_U: //U
                backColors = Back.getReverseColorsOn('U');
                Back.setColorsOn('U',Left.getReverseColorsOn('U'));
                Left.setColorsOn('U',Front.getColorsOn('U'));
                Front.setColorsOn('U',Right.getColorsOn('U'));
                Right.setColorsOn('U',backColors);
                Up.shift(Face.CW);
                break;
            case KeyEvent.VK_V: //D
                backColors = Back.getReverseColorsOn('D');
                Back.setColorsOn('D',Right.getReverseColorsOn('D'));
                Right.setColorsOn('D',Front.getColorsOn('D'));
                Front.setColorsOn('D',Left.getColorsOn('D'));
                Left.setColorsOn('D',backColors);
                Down.shift(Face.CW);
                break;
            case KeyEvent.VK_N: //D'
                backColors = Back.getReverseColorsOn('D');
                Back.setColorsOn('D',Left.getReverseColorsOn('D'));
                Left.setColorsOn('D',Front.getColorsOn('D'));
                Front.setColorsOn('D',Right.getColorsOn('D'));
                Right.setColorsOn('D',backColors);
                Down.shift(Face.CCW);
                break;
            case KeyEvent.VK_J: //F
                upColors = Up.getColorsOn('D');
                Up.setColorsOn('D',Left.getReverseColorsOn('R'));
                Left.setColorsOn('R',Down.getColorsOn('U'));
                Down.setColorsOn('U',Right.getReverseColorsOn('L'));
                Right.setColorsOn('L',upColors);
                Front.shift(Face.CW);
                break;
            case KeyEvent.VK_F: //F'
                upColors = Up.getReverseColorsOn('D');
                Up.setColorsOn('D',Right.getColorsOn('L'));
                Right.setColorsOn('L',Down.getReverseColorsOn('U'));
                Down.setColorsOn('U',Left.getColorsOn('R'));
                Left.setColorsOn('R',upColors);
                Front.shift(Face.CCW);
                break;
            case 1: //U2
                turnCube(KeyEvent.VK_U);
                turnCube(KeyEvent.VK_U);
                break;
            case 2: //R2
                turnCube(KeyEvent.VK_K);
                turnCube(KeyEvent.VK_K);
                break;
            case 3://L2
                turnCube(KeyEvent.VK_D);
                turnCube(KeyEvent.VK_D);
                break;
            case 4://D2
                turnCube(KeyEvent.VK_N);
                turnCube(KeyEvent.VK_N);
                break;
            case 5://F2
                turnCube(KeyEvent.VK_J);
                turnCube(KeyEvent.VK_J);
                break;
        }
        drawCube();
    }

    private void drawCube(){
        gbc.gridx = 1;
        gbc.gridy = 0;
        cubeDisplay.add(Up, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        cubeDisplay.add(Front, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        cubeDisplay.add(Down, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        cubeDisplay.add(Left, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        cubeDisplay.add(Right, gbc);

        frame.setContentPane(cubeDisplay);
    }

    

    private void addButtons(){
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new VirtualCube();

            }
            
        });

        JButton scrambleButton = new JButton("Scramble");
        cubeScrambler scrambler = new cubeScrambler();
        scrambleButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                int[] scram = scrambler.generateScramble(21);
                for(int i : scram){
                    turnCube(i);
                }
                //display scramble
            }
            
        });

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                LBLSolver solver = new LBLSolver(getFaces());
                ArrayList<Integer> solution = solver.getSolution(YELLOW);
                
            }

        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cubeDisplay.add(resetButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cubeDisplay.add(scrambleButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cubeDisplay.add(solveButton, gbc);
    }

    private void addControls(){

        addKeyBinding(cubeDisplay, KeyEvent.VK_UP, "rotateUp", (evt) -> {
            rotateCube(KeyEvent.VK_UP);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_DOWN, "rotateDown", (evt) -> {
            rotateCube(KeyEvent.VK_DOWN);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_LEFT, "rotateLeft", (evt) -> {
            rotateCube(KeyEvent.VK_LEFT);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_RIGHT, "rotateRight", (evt) -> {
            rotateCube(KeyEvent.VK_RIGHT);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_I, "turnRight", (evt) -> {
            turnCube(KeyEvent.VK_I);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_K, "turnRightInverted", (evt) -> {
            turnCube(KeyEvent.VK_K);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_E, "turnLeft", (evt) -> {
            turnCube(KeyEvent.VK_E);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_D, "turnLeftInverted", (evt) -> {
            turnCube(KeyEvent.VK_D);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_R, "turnUp", (evt) -> {
            turnCube(KeyEvent.VK_R);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_U, "turnUpInverted", (evt) -> {
            turnCube(KeyEvent.VK_U);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_V, "turnDown", (evt) -> {
            turnCube(KeyEvent.VK_V);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_N, "turnDownInverted", (evt) -> {
            turnCube(KeyEvent.VK_N);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_F, "turnFrontInverted", (evt) -> {
            turnCube(KeyEvent.VK_F);
        });
        addKeyBinding(cubeDisplay, KeyEvent.VK_J, "turnFront", (evt) -> {
            turnCube(KeyEvent.VK_J);
        });
        
    }

    private static void addKeyBinding(JComponent comp, int keyCode, String id, ActionListener actionListener){
        InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap ap = comp.getActionMap();

        im.put(KeyStroke.getKeyStroke(keyCode, 0), id);
        ap.put(id, new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });
    }

    public Face[] getFaces(){
        Face[] cubeFaces = new Face[6];
        cubeFaces[0] = Front;
        cubeFaces[1] = Back;
        cubeFaces[2] = Up;
        cubeFaces[3] = Down;
        cubeFaces[4] = Left;
        cubeFaces[5] = Right;
        return cubeFaces;
    }
    public static void main(String[] args){
        new VirtualCube();
    }
}

