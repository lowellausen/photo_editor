import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * Created by Leonardo on 25/08/2016.
 */
public class UI {
    public static final int displayX = 300;
    public static final int displayY = 100;

    public static ImageClass inImage= null;
    public static ImageClass opImage= null;

    private JButton opnImage;
    private JPanel panelMain;
    private JButton greyShadesbtn;
    private JButton horFlip;
    private JButton verticalFlip;
    private JButton btnQuanti;
    private JFormattedTextField quantShades;
    private JFormattedTextField currShades;
    private JLabel currShadesText;
    private JLabel quantShadesText;
    private JButton btnSave;
    private JButton btnHist;
    private JTextField brightVal;
    private JButton btnBright;
    private JButton contrastButton;
    private JButton negativeButton;
    private JButton histEqualizeButton;
    private JButton reloadImageButton;
    private JButton zoomOutButton;
    private JButton zoomInButton;
    private JButton clockwiseButton;
    private JButton counterclockwiseButton;
    private JButton convolutionButton;
    private JButton btnNormHist;
    private JTextField sxinput;
    private JTextField syinput;

    public UI() {
        currShades.setEnabled(false);
        opnImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inImage!= null){
                    inImage.close();
                    opImage.close();
                    inImage = null;
                    opImage = null;
                }


                File file = IO.openFile(panelMain);

                if(file==null){
                    return;
                }

                inImage = new ImageClass(file);
                opImage = new ImageClass(file);
//                inImage.scale();
//                opImage.scale();
                inImage.display( displayX, displayY);
                opImage.display( ImageClass.displayWidth+displayX+5 , displayY);

                if (inImage.isGreyShades()) {
                    currShades.setText(Integer.toString(inImage.numShades()));
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opImage == null){
                    JOptionPane.showMessageDialog(panelMain, " No image to save!!");
                    return;
                }
                File outImage = new File(IO.saveFile(panelMain).concat(".jpg"));
                opImage.saveImage(outImage);
            }
        });
        greyShadesbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(opImage == null){
                    JOptionPane.showMessageDialog(panelMain,"An image is needed!!");
                    return;
                }

                opImage.close();
                opImage.greyShades();
                opImage.display(ImageClass.displayWidth+displayX+5,100);
                currShades.setText(Integer.toString(opImage.numShades()));

            }
        });
        horFlip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(opImage == null){
                    JOptionPane.showMessageDialog(panelMain,"An image is needed!!");
                    return;
                }

                opImage.close();
                opImage.horizontalFlip();
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        verticalFlip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(opImage == null){
                    JOptionPane.showMessageDialog(panelMain,"An image is needed!!");
                    return;
                }

                opImage.close();
                opImage.verticalFlip();
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        btnQuanti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(opImage == null){
                    JOptionPane.showMessageDialog(panelMain,"An image is needed!!");
                    return;
                }
                if(!opImage.isGreyShades()) {
                    JOptionPane.showMessageDialog(panelMain,"Grey scale image needed!!");
                    return;
                }

                int shades=0;
                if(isInt(quantShades.getText())){
                    shades = Integer.parseInt(quantShades.getText());
                }
                else {
                    JOptionPane.showMessageDialog(panelMain, "Need a valid number of shades to quantize to");
                    return;
                }




                opImage.close();
                opImage.quantization(shades);
                opImage.display(ImageClass.displayWidth+displayX+5,100);
                currShades.setText(Integer.toString(opImage.numShades()));
            }
        });
        btnHist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hist[] = opImage.histogramCalculation();

                JFrame frame = new Histogram(hist);
                frame.setVisible(true);
            }
        });
        btnNormHist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hist[] = opImage.normalizeHistogram(opImage.histogramCalculation());

                JFrame frame = new Histogram(hist);
                frame.setVisible(true);
            }
        });
        btnBright.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opImage.close();
                opImage.brightnessEnhancement(Integer.parseInt(brightVal.getText()));
                opImage.display(ImageClass.displayWidth+displayX+5,100);

            }
        });
        contrastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float brightness;
                if(isFloat(brightVal.getText())){
                    brightness = Float.parseFloat(brightVal.getText());
                }
                else{
                    return;
                }
                opImage.close();
                opImage.contrastEnhancement(brightness);
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        negativeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opImage.close();
                opImage.negative();
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        histEqualizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int hist[] = opImage.histogramCalculation();

                JFrame frame = new Histogram(hist);
                frame.setVisible(true);

                opImage.close();
                opImage.equalize(opImage.normalizeHistogram(hist));
                opImage.display(ImageClass.displayWidth+displayX+5,100);

                int newHist[] = opImage.histogramCalculation();
                JFrame newframe = new Histogram(newHist);
                newframe.setTitle("New Histogram");
                newframe.setVisible(true);
            }
        });
        reloadImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opImage.close();
                ImageClass im = new ImageClass(inImage.image);
                opImage = im;
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isInt(sxinput.getText()) || !isInt(syinput.getText())){return;}

                int sx = Integer.parseInt(sxinput.getText());
                int sy = Integer.parseInt(syinput.getText());

                opImage.close();
                BufferedImage image = opImage.zoomOut(sx,sy);
                ImageClass im = new ImageClass(image);
                opImage = im;
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opImage.close();
                BufferedImage image = opImage.zoomIn();
                ImageClass im = new ImageClass(image);
                opImage = im;
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        clockwiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opImage.close();
                BufferedImage image = opImage.clockwiseFlip();
                ImageClass im = new ImageClass(image);
                opImage = im;
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        counterclockwiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opImage.close();
                BufferedImage image = opImage.counterclockwiseFlip();
                ImageClass im = new ImageClass(image);
                opImage = im;
                opImage.display(ImageClass.displayWidth+displayX+5,100);
            }
        });
        convolutionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Kernel Maker");
                frame.setContentPane(new KernelMaker().panelMain);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocation(0, displayY);
                frame.setVisible(true);





            }
        });


    }

    public static boolean isInt (String entry){
        if(entry.contentEquals("")){
            return false;
        }
        else {
            if (entry.matches("^[0-9]*")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isFloat (String entry){
        if(entry.contentEquals("")){
            return false;
        }
        else {
            if (entry.matches("^([+-]?\\d*\\.?\\d*)$")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FPI");
        frame.setContentPane(new UI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(0, displayY);
        frame.setVisible(true);
    }
}
