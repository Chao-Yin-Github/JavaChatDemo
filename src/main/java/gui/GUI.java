package gui;

import util.FileAndStringTransformUtil;
import util.Transmission;

import javax.swing.*;
import java.awt.*;

/**
 * @author yinchao
 * @Date 2019/12/10 23:11
 */
public class GUI extends JFrame {

    public void init(Transmission transmission) {
        FlowLayout layout = new FlowLayout();

        JFrame jFrame = new JFrame("write file saving path");
        JTextArea jTextArea = new JTextArea();
        JPanel jPanelTextArea = new JPanel();
        JButton jButton = new JButton("save files");

        jTextArea.setBackground(new Color(248, 248, 255));
        jTextArea.setRows(1);
        jTextArea.setColumns(30);

        jPanelTextArea.setBackground(new Color(222, 220, 238));
        jPanelTextArea.add(jTextArea);

        jButton.setSize(30, 10);
        jButton.setBackground(new Color(163, 161, 161));
//        jButton.setForeground(new Color(37, 44, 65));
        jButton.setForeground(new Color(73, 37, 64));
        jButton.setBorderPainted(false);
        jButton.addActionListener(e -> {
            FileAndStringTransformUtil.stringToFile(transmission.getContent(), jTextArea.getText().trim());
            jFrame.dispose();
        });

        jFrame.setLayout(layout);
        jFrame.setForeground(new Color(37, 44, 65));
        jFrame.setSize(500, 80);
        jFrame.setLocation(450, 400);
        jFrame.add(jPanelTextArea);
        jFrame.add(jButton);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
