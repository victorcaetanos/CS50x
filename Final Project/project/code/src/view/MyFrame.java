package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class MyFrame extends JFrame {
    public MyFrame() throws HeadlessException {
        this.setIcon("../icons/AppIcon.png");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    /**
     * Sets this frames icon.
     *
     * @param imagePath the path of the image to be displayed.
     */
    public void setIcon(String imagePath) {
        InputStream imgStream = CustomerPanel.class.getResourceAsStream(imagePath);
        assert imgStream != null;
        BufferedImage myImg;
        try {
            myImg = ImageIO.read(imgStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setIconImage(myImg);
    }

    /**
     * I made a few changes but
     * See https://stackoverflow.com/a/10625471/1694043 for more information
     */
    public static TableModel buildTableModel(final ResultSet resultSet, Vector<String> columnNames)
            throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();

        // Data of the table.
        Vector<Vector<Object>> dataVector = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> rowVector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                rowVector.add(resultSet.getObject(columnIndex));
            }
            dataVector.add(rowVector);
        }

        // A little extra code for non-editable cells
        return new DefaultTableModel(dataVector, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void showErrorMessage(String message) {
        if (message == null) return;
        JOptionPane.showMessageDialog(null, message);
    }
}
