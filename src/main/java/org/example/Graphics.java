package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.border.Border;
import javax.swing.*;
import java.awt.Color;
import java.util.Map;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;


public class Graphics extends JFrame {

    public Graphics(Map<String, Double> internetUsers) {
        initUI(internetUsers);
    }

    private void configureChartPanel(ChartPanel chartPanel) {
        chartPanel.setVerticalAxisTrace(true);
        chartPanel.setBorder(createEmptyBorderWithMargin(15));
        chartPanel.setBackground(Color.WHITE);
    }

    private Border createEmptyBorderWithMargin(int margin) {
        return BorderFactory.createEmptyBorder(margin, margin, margin, margin);
    }

    private JFreeChart createChart(Map<String, Double> internetUsers) {
        CategoryDataset dataset = createDataset(internetUsers);
        return createChart(dataset);
    }

    private void initUI(Map<String, Double> internetUsers) {
        JFreeChart chart = createChart(internetUsers);
        ChartPanel chartPanel = new ChartPanel(chart);

        configureChartPanel(chartPanel);

        add(chartPanel);

        pack();
        setTitle("Bar chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createDataset(Map<String, Double> internetUsers) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        internetUsers.forEach((key, value) -> {
            // Если категория уже существует, то объединяем значения
            if (dataset.getRowKeys().contains("internetUsers") && dataset.getColumnKeys().contains(key)) {
                double existingValue = dataset.getValue("internetUsers", key).doubleValue();
                dataset.setValue(existingValue + value, "internetUsers", key);
            } else {
                // В противном случае, устанавливаем новое значение
                dataset.setValue(value, "internetUsers", key);
            }
        });
        return dataset;
    }



    private JFreeChart createChart(CategoryDataset dataset) {
        String title = "Пользователи интернета по субрегионам";
        String xAxisLabel = "Субрегионы";
        String yAxisLabel = "Пользователи";

        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean legend = false;
        boolean tooltips = true;
        boolean urls = false;

        JFreeChart chart = ChartFactory.createBarChart(title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls);


        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);

        // Установка цвета колонок
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);

        return chart;
    }

}