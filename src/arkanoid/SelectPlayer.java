package arkanoid;

import arkanoid.menu.Splash;
import atariCore.Helper;
import atariCore.Sound;

import javax.swing.*;
import java.awt.*;

import static arkanoid.arkHelper.pathImage;
import static atariCore.Helper.*;

public class SelectPlayer extends JPanel {

    private JButton startButton, backButton;
    JTextField textName;
    JLabel labelName = new JLabel("Enter Your Name");


    public SelectPlayer() {
        initFrame();
    }

    private void initFrame() {

        frame.setTitle("Select Player");
        frame.getContentPane().remove(panel);

        panel = this;

        setDesign();
        setActions();

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void setDesign() {

        setLayout(null);

        setStartButton(screenWidth / 3 + 150, 450);
        setBackButton(screenWidth / 3 - 150, 450);

        textName = new JTextField(20);
        textName.setFont(font);
        textName.setBounds(screenWidth / 3 - 190, 300, 800, 70);
        textName.setHorizontalAlignment(JTextField.CENTER);
        textName.setForeground(buttonBackgroundColor);
        textName.setBackground(foregroundColor);
        textName.requestFocus();

        labelName.setFont(font);
        labelName.setForeground(buttonBackgroundColor);
        labelName.setBounds(screenWidth / 3 - 50, 80, screenWidth / 3 - 50 + 710, 300);

        add(labelName);
        add(textName);

        setCursorImage(panel, pathCursor);
    }

    private void setActions() {

        startButton.addActionListener(e -> {
            if(sounds)
            Sound.Play(arkHelper.clickSound,true);
            if (textName.getText().length() > 0 && textName.getText().length() <= 20) {
                String name = textName.getText();
                int level = arkFile.getPlayerLevel(textName.getText());
                new SelectEra(name, level);
            }
        });

        backButton.addActionListener(e->{
            if(sounds)
            Sound.Play(arkHelper.clickSound,true);
            new Splash();
        });
    }
    private void setBackButton(int x, int y) {
        backButton = Helper.buttonHelper("Back", x, y, new Dimension(250, 100));
    }

    private void setStartButton(int x, int y) {
        startButton = Helper.buttonHelper("Start!", x, y, new Dimension(400, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(getImage(pathImage + "background/bgName.jpg", 1), 0, 0, null);
    }
}
