import com.sun.deploy.util.ArrayUtil;
import org.jfree.util.ArrayUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * Created by Leonardo on 26/08/2016.
 */
public class ImageClass {

    public static final int displayWidth = 512;
    public static final int displayHeight = 384;

    BufferedImage image;
    JFrame editorFrame;
    int height;
    int width;

    public ImageClass (File jpegFile){
        try {
            this.image = ImageIO.read(jpegFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        this.width = this.image.getWidth();
        this.height= this.image.getHeight();

        this.editorFrame = new JFrame("Image");
        editorFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public ImageClass (BufferedImage image){
        this.image = image;
        this.width = this.image.getWidth();
        this.height= this.image.getHeight();

        this.editorFrame = new JFrame("Image");
        editorFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }


    public void saveImage (File outImage){
        try {
            ImageIO.write(this.image, "jpg", outImage);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getWidth (){ return this.width;}
    public int getHeight () { return this.height;}
    public Color getRGB (int x,int y) {return new Color (this.image.getRGB(x,y));}

    public int[] getColumn (int x){
        int y  = this.getHeight();
        int column[] = new int[y];
        for (int i = 0; i < y ; i++) {
            column[i] = this.image.getRGB(x,i);
        }
        return column;
    }

    public int[] getRow (int y){
        int x  = this.getWidth();
        int row[] = new int[x];
        for (int i = 0; i < x ; i++) {
            row[i] = this.image.getRGB(i,y);
        }
        return row;
    }

    public void setColumn (int column[], int x){
        int y  = this.getHeight();
        for (int i = 0; i < y ; i++) {
            this.image.setRGB(x, i, column[i]);
        }
    }

    public void setRow (int row[], int y){
        int x  = this.getWidth();
        for (int i = 0; i < x ; i++) {
            this.image.setRGB(i, y, row[i]);
        }
    }


    public void close (){
        this.editorFrame.setVisible(false);
        this.editorFrame.dispose();
    }

    public void greyShades (){
        for (int i = 0; i < this.getHeight() ; i++) {
            for (int j = 0; j < this.getWidth() ; j++) {
                Color rgb;
                double lumi = 0;
                rgb = this.getRGB(j,i);
                lumi= rgb.getRed()*0.299 + rgb.getGreen()*0.587 + rgb.getBlue()*0.114;
                Color greyShade = new Color ((int)lumi, (int)lumi, (int)lumi);
                this.image.setRGB(j, i, greyShade.getRGB());
            }
        }
    }

    public void horizontalFlip (){
        int width = this.getWidth() -1;
        for (int i = 0; i < width/2 ; i++) {
            int aux[] = this.getColumn(i);
            this.setColumn(getColumn(width-i),i);
            this.setColumn(aux,width-i);
        }
    }

    public void verticalFlip (){
        int height = this.getHeight() -1;
        for (int i = 0; i <= height/2 ; i++) {
            int aux[] = this.getRow(i);
            this.setRow(getRow(height-i),i);
            this.setRow(aux,height-i);
        }
    }

    public void quantization (int numShades){
        int currentShades = this.numShades();
        float multiplier = (float)numShades/(float)currentShades; //this is the cluster aize

        for (int i = 0; i <this.getHeight() ; i++) {
            for (int j = 0; j <this.getWidth() ; j++) {
                int thisShade = new Color(this.image.getRGB(j, i)).getRed();
                int newShade = ((int)(thisShade*multiplier))*currentShades/numShades;
                newShade = new Color(newShade,newShade,newShade).getRGB();
                this.image.setRGB(j,i,newShade);
            }
        }
    }

    public int numShades (){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <this.getHeight() ; i++) {
            for (int j = 0; j <this.getWidth() ; j++) {
                int rgb = this.image.getRGB(j,i);
                if (!list.contains(rgb)) {
                    list.add(rgb);
                }
            }
        }
        return list.size();
    }

    boolean isGreyShades (){
        for (int i = 0; i <this.getHeight() ; i++) {
            for (int j = 0; j <this.getWidth() ; j++) {
                Color rgb = new Color (this.image.getRGB(j,i));
                if(!(rgb.getBlue()==rgb.getGreen() && rgb.getGreen()==rgb.getRed())){
                    return false;
                }
            }
        }
        return true;
    }

    public void scale (){
        Image img = this.image.getScaledInstance(displayWidth,displayHeight,image.SCALE_DEFAULT);

        BufferedImage buffered = new BufferedImage(displayWidth, displayHeight, this.image.getType());
        buffered.getGraphics().drawImage(img, 0, 0 ,null);
        buffered.getGraphics().dispose();
        this.image = buffered;
    }

    public void display (int x, int y){



        ImageIcon imageIcon = new ImageIcon(this.image);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);



        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        //editorFrame.setSize(new Dimension(displayWidth,displayHeight));
        //editorFrame.setSize(new Dimension(this.getWidth(),this.getHeight()));
        editorFrame.setResizable(false);
        editorFrame.setLocation(x,y);
        editorFrame.setVisible(true);
    }

    public int[] histogramCalculation (){
        int hist[] = new int[256];

        for (int i = 0; i <this.getWidth() ; i++) {
            for (int j = 0; j <this.getHeight() ; j++) {
                Color lumi = new Color(this.image.getRGB(i,j));
                int k = lumi.getRed();
                hist[k] = hist[k] + 1;
            }
        }
        return hist;
    }

    public void equalize (int hist[]){
        for (int i = 0; i <this.getWidth() ; i++) {
            for (int j = 0; j <this.getHeight() ; j++) {
                Color color = this.getRGB(i,j);
                int red = hist[color.getRed()];
                int green = hist[color.getGreen()];
                int blue = hist[color.getBlue()];

                this.image.setRGB(i,j, new Color(red, green,blue).getRGB());
            }
        }
    }

    public int[] normalizeHistogram (int hist[]){
        float alpha = 255/(float)(this.getHeight()*this.getWidth());
        int hist_cum[] = new int[256];

        hist_cum[0] = (int) alpha*hist[0];

        for (int i = 1; i <256 ; i++) {
            hist_cum[i] = (int)(hist_cum[i-1] +alpha*hist[i]);
        }
        return hist_cum;
    }

    public void brightnessEnhancement (int b){
        for (int i = 0; i <this.getWidth() ; i++) {
            for (int j = 0; j <this.getHeight() ; j++) {
                Color rgb = new Color (this.image.getRGB(i,j));
                int blue = rgb.getBlue()+b;
                int red = rgb.getRed()+b;
                int green = rgb.getGreen() +b;
                blue = checkRange(blue);
                red = checkRange(red);
                green = checkRange(green);

                this.image.setRGB(i,j, new Color (red, green, blue).getRGB());
            }
        }
    }

    public void contrastEnhancement (float a){
        for (int i = 0; i <this.getWidth() ; i++) {
            for (int j = 0; j <this.getHeight() ; j++) {
                Color rgb = new Color (this.image.getRGB(i,j));
                int blue =(int)(rgb.getBlue()*a);
                int red = (int)(rgb.getRed()*a);
                int green =(int) (rgb.getGreen() *a);
                blue = checkRange(blue);
                red = checkRange(red);
                green = checkRange(green);
                this.image.setRGB(i,j, new Color (red, green, blue).getRGB());
            }
        }
    }

    public void negative (){
        for (int i = 0; i <this.getWidth() ; i++) {
            for (int j = 0; j <this.getHeight() ; j++) {
                Color rgb = new Color (this.image.getRGB(i,j));
                int blue = 255- rgb.getBlue();
                int red = 255-rgb.getRed();
                int green =255- rgb.getGreen();

                this.image.setRGB(i,j, new Color (red, green, blue).getRGB());
            }
        }
    }

    public static int roundUpDiv(int num, int divisor) {
        return (num + divisor - 1) / divisor;
    }

    public BufferedImage zoomOut (int sx, int sy){
        int width = roundUpDiv(this.getWidth(),sx);
        int height = roundUpDiv(this.getHeight(),sy);
        BufferedImage image = new BufferedImage(width,height, this.image.getType());

        for (int i = 0; i <height ; i++) {
            for (int j = 0; j <width ; j++) {
                int redMean = 0, greenMean = 0, blueMean = 0;
                int pixcount=0;
                int x = j*sx;
                int y = i*sy;
                int k = 0;
                while (k<sy && y<this.getHeight()){
                    int l =0;
                    while(l<sx && x<this.getWidth()){
                        Color rgb = new Color(this.image.getRGB(x,y));
                        redMean += rgb.getRed();
                        greenMean += rgb.getGreen();
                        blueMean += rgb.getBlue();
                        pixcount++;
                        x++;
                        l++;
                    }
                    y++;
                    k++;
                }
                Color rgb = new Color(redMean/pixcount,greenMean/pixcount,blueMean/pixcount);
                image.setRGB(j,i,rgb.getRGB());
            }
        }
        return image;
    }

    public BufferedImage zoomIn (){
        int width = this.getWidth()*2-1;
        int height = this.getHeight()*2-1;
        BufferedImage image = new BufferedImage(width,height, this.image.getType());

        for (int i = 0; i <height ; i+= 2) {
            for (int j = 0; j <width ; j+= 2) {
                Color rgb = new Color(this.image.getRGB(j/2,i/2));
                image.setRGB(j,i,rgb.getRGB());
            }
        }
        for (int i = 0; i <height ; i+= 2) {
            for (int j = 1; j <width ; j+= 2) {
                Color rgb1 = new Color(image.getRGB(j-1,i));
                Color rgb2 = new Color(image.getRGB(j+1,i));
                int redMean = (rgb1.getRed()+rgb2.getRed())/2;
                int greenMean = (rgb1.getGreen()+rgb2.getGreen())/2;
                int blueMean = (rgb1.getBlue()+rgb2.getBlue())/2;
                image.setRGB(j,i,new Color(redMean,greenMean,blueMean).getRGB());
            }
        }
        for (int i = 1; i <height ; i+= 2) {
            for (int j = 0; j <width ; j++) {
                Color rgb1 = new Color(image.getRGB(j,i-1));
                Color rgb2 = new Color(image.getRGB(j,i+1));
                int redMean = (rgb1.getRed()+rgb2.getRed())/2;
                int greenMean = (rgb1.getGreen()+rgb2.getGreen())/2;
                int blueMean = (rgb1.getBlue()+rgb2.getBlue())/2;
                image.setRGB(j,i,new Color(redMean,greenMean,blueMean).getRGB());
            }
        }

        return image;
    }

    public BufferedImage clockwiseFlip (){
        int width = this.getHeight();
        int height = this.getWidth();
        BufferedImage image = new BufferedImage(width,height, this.image.getType());

        for (int i = 0; i < this.getHeight(); i++) {
            ImageClass.setColumnAux(this.getRow(i),width -i-1, image,height);
        }

        return image;
    }

    public BufferedImage counterclockwiseFlip (){
        int width = this.getHeight();
        int height = this.getWidth();
        BufferedImage image = new BufferedImage(width,height, this.image.getType());

        for (int i = 0; i < this.getWidth(); i++) {
            ImageClass.setRowAux(this.getColumn(height -i -1),i, image,width);
        }

        return image;
    }

    public static void setColumnAux (int column[], int x, BufferedImage image, int y){
        for (int i = 0; i < y ; i++) {
            image.setRGB(x, i, column[i]);
        }
    }

    public static void setRowAux (int row[], int y, BufferedImage image , int x){
        for (int i = 0; i < x ; i++) {
            image.setRGB(i, y, row[i]);
        }
    }

    public BufferedImage convolution (float kernel[][]){
        BufferedImage image = new BufferedImage(this.getWidth(),this.getHeight(),this.image.getType());
        int kwidth = kernel.length/2;
        int kheight = kernel[0].length/2;

        for (int y = kheight; y <this.getHeight()-kheight ; y++) {
            for (int x = kwidth; x <this.getWidth()-kwidth ; x++) {
                float sum = 0;
                for (int i = -kheight; i <=kheight ; i++) {
                    for (int j = -kwidth; j <=kwidth ; j++) {
                        Color rgb = this.getRGB(x-j,y-i);
                        int shade = rgb.getRed();
                        sum += kernel[j+kwidth][i+kheight]*shade;
                    }
                }
                sum = checkRange((int)sum);
                Color newShade = new Color((int)sum,(int)sum,(int)sum);
                image.setRGB(x,y,newShade.getRGB());
            }
        }
        return image;
    }

    public static int checkRange (int value){
        if(value > 255){
            value = 255;
        }
        if(value<0){
            value = 0;
        }
        return value;
    }
}
