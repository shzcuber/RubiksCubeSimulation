import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Dimension;


public class Face extends JPanel{
    private Color[] colors = new Color[9];
    public static final int CW = 0, CCW = 1, VERTICAL = 2, HORIZONTAL = 3;

    public Face(Color[] c){
        for(int i=0; i<9; i++){
            colors[i] = c[i];
        }
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(150, 150);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int p = 0;
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                g2d.setColor(colors[p++]);
                g2d.fillRect(i*50, j*50, 50, 50);
            }
        }
    }

    public void shift(int direction){
        Color[] newColors = new Color[9];
        int c;
        switch(direction){
            case CW:
                c=2;
                for(int i=0; i<9; i++){
                    newColors[i] = colors[c];
                    c+=3;
                    if(c>8){
                        c = (c%3)-1;
                    }
                }
                break;
            case CCW:
                c=6;
                for(int i=0; i<9; i++){
                    newColors[i] = colors[c];
                    c-=3;
                    if(c<0){
                        c%=3;
                        if(c<0){
                            c+=10;
                        }else{
                            c+=7;
                        }
                    }
                }
                break;
            case VERTICAL:
                for(int i=0; i<3; i++){
                    newColors[i] = colors[i+6];
                    newColors[i+6] = colors[i];
                }
                for(int i=3; i<6; i++){
                    newColors[i] = colors[i];
                }
                break;
            case HORIZONTAL:
                for(int i=0; i<7; i+=3){
                    newColors[i] = colors[i+2];
                    newColors[i+2] = colors[i];
                }
                for(int i=1; i<8; i+=3){
                    newColors[i] = colors[i];
                }
                break;
        }
        for(int i=0; i<9; i++){
            colors[i] = newColors[i];
        }
    }

    public Color[] getReverseColorsOn(char layer){
        Color[] colorsOnLayer = new Color[3];
        int c=2;
        switch(layer){
            case 'L':
                for(int i=0; i<3; i++){
                    colorsOnLayer[c--] = colors[i];
                }
                break;
            case 'R':
                for(int i=6; i<9; i++){
                    colorsOnLayer[c--] = colors[i];
                }
                break;
            case 'D':
                for(int i=2; i<9; i+=3){
                    colorsOnLayer[c--] = colors[i];
                }
                break;
            case 'U':
                for(int i=0; i<9; i+=3){
                    colorsOnLayer[c--] = colors[i];
                }
                break;
        }
        return colorsOnLayer;
    }

    public Color[] getColorsOn(char layer)
    {
        Color[] colorsOnLayer = new Color[3];
        int c=0;
        switch(layer){
            case 'L':
                for(int i=0; i<3; i++){
                    colorsOnLayer[c++] = colors[i];
                }
                break;
            case 'R':
                for(int i=6; i<9; i++){
                    colorsOnLayer[c++] = colors[i];
                }
                break;
            case 'D':
                for(int i=2; i<9; i+=3){
                    colorsOnLayer[c++] = colors[i];
                }
                break;
            case 'U':
                for(int i=0; i<9; i+=3){
                    colorsOnLayer[c++] = colors[i];
                }
                break;
        }
        return colorsOnLayer;
    }

    public void setColorsOn(char layer, Color[] colorsOnLayer){
        int c=0;
        switch(layer){
            case 'L':
                for(int i=0; i<3; i++){
                    colors[i] = colorsOnLayer[c++];
                }
                break;
            case 'R':
                for(int i=6; i<9; i++){
                    colors[i] = colorsOnLayer[c++];
                }
                break;
            case 'D':
                for(int i=2; i<9; i+=3){
                    colors[i] = colorsOnLayer[c++];
                }
                break;
            case 'U':
                for(int i=0; i<9; i+=3){
                    colors[i] = colorsOnLayer[c++];
                }
                break;
        }
    }

    public Color[] getColors(){
        return colors;
    }

    public Color getCenterColor(){
        return getColors()[4];
    }

    public String toString(){
        if(colors[4]==VirtualCube.WHITE){
            return "WHITE";
        }else if(colors[4]==VirtualCube.YELLOW){
            return "YELLOW";
        }else if(colors[4]==VirtualCube.ORANGE){
            return "ORANGE";
        }
        else if(colors[4]==VirtualCube.BLUE){
            return "BLUE";
        }
        else if(colors[4]==VirtualCube.RED){
            return "RED";
        }
        return "GREEN";
    }

    public void setColors(Color[] colors){
        this.colors = colors;
    }
}
