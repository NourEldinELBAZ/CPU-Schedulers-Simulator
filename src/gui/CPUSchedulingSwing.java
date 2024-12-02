//demo to understand what we should do.

package gui;

import javax.swing.*;
        import java.awt.*;

public class CPUSchedulingSwing extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        // Draw timeline
        g.setColor(Color.BLACK);
        g.drawLine(50, height / 2, width - 50, height / 2);

        // Draw processes
        drawProcess(g, "P1", 50, 100, Color.BLUE);
        drawProcess(g, "P2", 150, 50, Color.RED);

        // Draw time markers
        for (int i = 0; i <= 10; i++) {
            int x = 50 + i * (width - 100) / 10;
            g.drawLine(x, height / 2 - 5, x, height / 2 + 5);
            g.drawString(String.valueOf(i * 10), x - 5, height / 2 + 20);
        }
    }

    private void drawProcess(Graphics g, String processName, int startX, int width, Color color) {
        int barHeight = 30;
        int barY = 70;

        g.setColor(color);
        g.fillRect(startX, barY, width, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(startX, barY, width, barHeight);

        // Draw Process label
        g.drawString(processName, startX + width / 2 - 10, barY + barHeight / 2 + 5);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CPU Scheduling Graph");
        CPUSchedulingSwing panel = new CPUSchedulingSwing();
        frame.add(panel);
        frame.setSize(800, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
