import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Created by Leonardo on 25/08/2016.
 */
public class IO {
    public static File openFile(Component parent){
        String user = System.getProperty("user.home");
        JFileChooser fileChooser = new JFileChooser(user+"/desktop");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG FILES", "jpg", "jpeg"));

        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            return selectedFile;
        } else {
            return null;
        }
    }

    public static String saveFile(Component parent){
        String user = System.getProperty("user.home");
        JFileChooser fileChooser = new JFileChooser(user+"/desktop");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG FILES", "jpg", "jpeg"));

        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }
}
