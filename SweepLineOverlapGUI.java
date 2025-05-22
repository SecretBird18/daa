import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SweepLineOverlapGUI extends JFrame {
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JButton computeButton;

    public SweepLineOverlapGUI() {
        setTitle("Sweep Line - Max Overlapping Open Intervals");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputArea = new JTextArea(10, 40);
        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        computeButton = new JButton("Compute Max Overlap");

        computeButton.addActionListener(e -> computeMaxOverlap());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel("Enter intervals (start end):"), BorderLayout.NORTH);
        panel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        panel.add(computeButton, BorderLayout.SOUTH);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void computeMaxOverlap() {
        List<double[]> intervals = new ArrayList<>();
        String[] lines = inputArea.getText().split("\\n");

        for (String line : lines) {
            try {
                String[] parts = line.trim().split("\\s+");
                double start = Double.parseDouble(parts[0]);
                double end = Double.parseDouble(parts[1]);
                if (start >= end) {
                    outputArea.setText("Invalid interval: " + line);
                    return;
                }
                intervals.add(new double[]{start, end});
            } catch (Exception e) {
                outputArea.setText("Error parsing: " + line);
                return;
            }
        }

        int result = maxOverlappingIntervalsSweep(intervals);
        outputArea.setText("Max Overlap (Sweep Line): " + result);
    }

    private int maxOverlappingIntervalsSweep(List<double[]> intervals) {
        List<Event> events = new ArrayList<>();
        for (double[] interval : intervals) {
            events.add(new Event(interval[0], "start"));
            events.add(new Event(interval[1], "end"));
        }

        events.sort((e1, e2) -> {
            if (e1.time != e2.time)
                return Double.compare(e1.time, e2.time);
            return e1.type.equals("end") ? -1 : 1;
        });

        int current = 0, max = 0;
        for (Event e : events) {
            if (e.type.equals("start")) {
                current++;
                max = Math.max(max, current);
            } else {
                current--;
            }
        }
        return max;
    }

    static class Event {
        double time;
        String type;

        Event(double time, String type) {
            this.time = time;
            this.type = type;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SweepLineOverlapGUI::new);
    }
}