import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

/**
 * Created by Leonardo on 13/09/2016.
 */
public class KernelMaker {
    private JButton gaussianButton;
    private JButton laplacianButton;
    private JButton genHighPassButton;
    private JButton prewittHxButton;
    private JButton prewittHyButton;
    private JButton sobelHxButton;
    private JButton sobelHyButton;
    private JTextField entry3;
    private JTextField entry1;
    private JTextField entry2;
    private JTextField entry4;
    private JTextField entry5;
    private JTextField entry6;
    private JTextField entry7;
    private JTextField entry8;
    private JTextField entry9;
    private JButton convolveButton;
    public JPanel panelMain;
    private JCheckBox add127CheckBox;

    public KernelMaker() {
        gaussianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry1.setText("0.0625");
                entry2.setText("0.125");
                entry3.setText("0.0625");
                entry4.setText("0.125");
                entry5.setText("0.25");
                entry6.setText("0.125");
                entry7.setText("0.0625");
                entry8.setText("0.125");
                entry9.setText("0.0625");
            }
        });
        laplacianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry1.setText("0");
                entry2.setText("-1");
                entry3.setText("0");
                entry4.setText("-1");
                entry5.setText("4");
                entry6.setText("-1");
                entry7.setText("0");
                entry8.setText("-1");
                entry9.setText("0");
            }
        });
        genHighPassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry1.setText("-1");
                entry2.setText("-1");
                entry3.setText("-1");
                entry4.setText("-1");
                entry5.setText("8");
                entry6.setText("-1");
                entry7.setText("-1");
                entry8.setText("-1");
                entry9.setText("-1");
            }
        });
        prewittHxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry1.setText("-1");
                entry2.setText("0");
                entry3.setText("1");
                entry4.setText("-1");
                entry5.setText("0");
                entry6.setText("1");
                entry7.setText("-1");
                entry8.setText("0");
                entry9.setText("1");
            }
        });
        prewittHyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry1.setText("-1");
                entry2.setText("-1");
                entry3.setText("-1");
                entry4.setText("0");
                entry5.setText("0");
                entry6.setText("0");
                entry7.setText("1");
                entry8.setText("1");
                entry9.setText("1");
            }
        });
        sobelHxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry1.setText("-1");
                entry2.setText("0");
                entry3.setText("1");
                entry4.setText("-2");
                entry5.setText("0");
                entry6.setText("2");
                entry7.setText("-1");
                entry8.setText("0");
                entry9.setText("1");
            }
        });
        sobelHyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entry1.setText("-1");
                entry2.setText("-2");
                entry3.setText("-1");
                entry4.setText("0");
                entry5.setText("0");
                entry6.setText("0");
                entry7.setText("1");
                entry8.setText("2");
                entry9.setText("1");
            }
        });
        convolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float kernel[][] = new float[3][3];
                if(!(UI.isFloat(entry1.getText()) && UI.isFloat(entry2.getText()) && UI.isFloat(entry3.getText())
                        && UI.isFloat(entry1.getText()) && UI.isFloat(entry1.getText()) && UI.isFloat(entry1.getText())
                        && UI.isFloat(entry1.getText()) && UI.isFloat(entry1.getText()) && UI.isFloat(entry1.getText()))){
                    return;
                }
                kernel[0][0] = Float.parseFloat(entry1.getText());
                kernel[1][0] = Float.parseFloat(entry2.getText());
                kernel[2][0] = Float.parseFloat(entry3.getText());
                kernel[0][1] = Float.parseFloat(entry4.getText());
                kernel[1][1] = Float.parseFloat(entry5.getText());
                kernel[2][1] = Float.parseFloat(entry6.getText());
                kernel[0][2] = Float.parseFloat(entry7.getText());
                kernel[1][2] = Float.parseFloat(entry8.getText());
                kernel[2][2] = Float.parseFloat(entry9.getText());

                UI.opImage.close();
                BufferedImage image = UI.opImage.convolution(kernel);
                ImageClass im = new ImageClass(image);
                UI.opImage = im;
                UI.opImage.display(ImageClass.displayWidth+UI.displayX+5,100);

                if(add127CheckBox.isSelected()){
                    UI.opImage.close();
                    UI.opImage.brightnessEnhancement(127);
                    UI.opImage.display(ImageClass.displayWidth+UI.displayX+5,100);
                }
            }
        });
    }
}
