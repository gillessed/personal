package net.gillessed.icarus.export;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.engine.Callback;
import net.gillessed.icarus.engine.FlameRenderer;
import net.gillessed.icarus.engine.ProgressBarUpdater;
import net.gillessed.icarus.engine.ProgressUpdater;
import net.gillessed.icarus.fileIO.FlameFileFilter;
import net.gillessed.icarus.fileIO.IOUtils;
import net.gillessed.icarus.geometry.ViewRectangle;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class FlameExportDialog {
    private final JFrame parent;
    private final JDialog jDialog;
    private final JButton start;
    private final JButton cancel;
    private final JTextField widthField;
    private final JTextField heightField;
    private final JProgressBar progressBar;

    private FlameModel flameModel;

    private ActionListener exportListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(IOUtils.getDirectory()));
            String ext = "png";
            FlameFileFilter filter = new FlameFileFilter(ext, "PNG Images");
            chooser.setFileFilter(filter);
            int result = chooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                if(!path.contains(".png")) {
                    path = path + ".png";
                }
                final String finalPath = path;
                int pixelWidth = Integer.parseInt(widthField.getText());
                int pixelHeight = Integer.parseInt(heightField.getText());
                ViewRectangle viewRectangle = new ViewRectangle(ViewRectangle.DEFAULT_VALUE, (double) pixelHeight / (double) pixelWidth, null);
                ProgressUpdater progressUpdater = new ProgressBarUpdater(progressBar);
                Callback<BufferedImage> callback = new Callback<BufferedImage>() {
                    @Override
                    public void callback(BufferedImage image) throws Exception {
                        start.setEnabled(true);
                        File fileToWriteTo = new File(finalPath);
                        ImageIO.write(image, "png", fileToWriteTo);
                    }
                };
                try {
                    FlameRenderer.get().renderFlame(flameModel,
                            pixelWidth,
                            pixelHeight,
                            viewRectangle,
                            progressUpdater,
                            callback);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(parent, "Error rendering image: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };

    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jDialog.setVisible(false);
        }
    };

    public FlameExportDialog(JFrame parent) {
        this.parent = parent;
        jDialog = new JDialog(parent);
        jDialog.setTitle("Export");
        jDialog.setSize(270, 190);
        jDialog.setResizable(false);
        jDialog.setLocation(new Point(200,200));

        Container c = jDialog.getContentPane();

        CellConstraints cc = new CellConstraints();
        FormLayout layout = new FormLayout("10dlu, pref, 10dlu, pref, 10dlu",
                                            "10dlu, pref, 10dlu, pref, 10dlu, pref, 10dlu");
        JPanel panel = new JPanel(layout);

        panel.add(new JLabel("Width:"), cc.xy(2, 2));
        widthField = new JTextField(8);
        panel.add(widthField, cc.xy(4, 2));

        panel.add(new JLabel("Height:"), cc.xy(2, 4));
        heightField = new JTextField(8);
        panel.add(heightField, cc.xy(4, 4));

        start = new JButton("Start");
        start.addActionListener(exportListener);
        panel.add(start, cc.xy(2, 6));

        cancel = new JButton("Cancel");
        cancel.addActionListener(cancelListener);
        panel.add(cancel, cc.xy(4, 6));

        c.add(panel);

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        c.add(progressBar, BorderLayout.SOUTH);
    }

    public void show() {
        jDialog.setVisible(true);
    }

    public void setFlameModel(FlameModel flameModel) {
        this.flameModel = flameModel;
    }
}
