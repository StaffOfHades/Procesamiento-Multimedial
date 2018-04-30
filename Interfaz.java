import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.Color;

public class Interfaz extends JFrame implements ActionListener {

    JFrame interfaz = new JFrame("Procesamiento de Imagenes");
    //Con la variable Font pones tipo y tamaño de letra
    Font fondo = new Font("Arial", Font.BOLD,15);

    Button botonInverseOperator;
    Button botonThresholdOperator;
    Button botonInvertedThresholdOperator;
    Button botonBinaryThresholdInterval;
    Button botonInvertedBinaryThresholdOperator;
    Button botonGrayScaleThresholdOperator;
    Button botonInvertedGrayScaleThresholdOperator;
    Button botonStretchOperator;
    Button botonGrayLevelReductionOperator;
    Button botonImageAddition;
    Button botonImageSubtraction;
    Button botonImageMultiplication;
    Button botonImageConvolution;
    Button botonMotionBlur;
    Button botonSharpen;
    Button botonCrispen;
    Button botonEmboss;
    Button botonLowFilter;
    Button botonHighPassFilter;
    Button botonNoiseReductionFilterAverage;
    Button botonSobel;
    Button botonPrewitt;
    Button CargarImagen;

    ImageProcessing original;
    ImageProcessing current;

    public Interfaz(){

        interfaz.setSize(1440,1450);
        interfaz.getContentPane().setBackground(Color.LIGHT_GRAY);
        interfaz.setLocationRelativeTo(null);
        // Para que la Interfaz este en medio
        interfaz.setLayout(null);
        interfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interfaz.setVisible(true);

        // Creacion de Botones

        botonInverseOperator = new Button("Inverse Operator");
        botonThresholdOperator = new Button("Threshold Operator");
        botonInvertedThresholdOperator = new Button("Inverted Threshold Operator");
        botonBinaryThresholdInterval = new Button("Binary Threshold Interval");
        botonInvertedBinaryThresholdOperator = new Button("Inverted Binary Threshold Operator");
        botonGrayScaleThresholdOperator = new Button("Gray Scale Threshold Operator");
        botonInvertedGrayScaleThresholdOperator = new Button("Inverted Gray Scale Threshold Operator");
        botonStretchOperator = new Button("Stretch Operator");
        botonGrayLevelReductionOperator = new Button("Gray Level Reduction Operator");
        botonImageAddition = new Button("Image Addition");
        botonImageSubtraction = new Button("Image Subtraction");
        botonImageMultiplication = new Button("Image Multiplication");
        botonImageConvolution = new Button("Reset Image");
        botonMotionBlur = new Button("Motion Blur");
        botonSharpen = new Button("Sharpen");
        botonCrispen = new Button("Crispen");
        botonEmboss = new Button("Emboss");
        botonLowFilter = new Button("Low Filter");
        botonHighPassFilter = new Button("High Pass Filter");
        botonNoiseReductionFilterAverage = new Button("Noise Reduction Filter Average");
        botonSobel = new Button("Sobel");
        botonPrewitt = new Button("Prewitt");
        CargarImagen = new Button("Cargar Imagen");

        // Tamaño del boton y color de letra

        botonInverseOperator.setBounds(10, 5, 145, 35);
        botonInverseOperator.setForeground(Color.gray);
        botonThresholdOperator.setBounds(155, 5, 145, 35);
        botonThresholdOperator.setForeground(Color.gray);
        botonInvertedThresholdOperator.setBounds(500, 40, 200, 35);
        botonInvertedThresholdOperator.setForeground(Color.gray);
        botonBinaryThresholdInterval.setBounds(500, 5, 200, 35);
        botonBinaryThresholdInterval.setForeground(Color.gray);
        botonInvertedBinaryThresholdOperator.setBounds(700, 5, 235, 35);
        botonInvertedBinaryThresholdOperator.setForeground(Color.gray);
        botonGrayScaleThresholdOperator.setBounds(700, 40, 235, 35);
        botonGrayScaleThresholdOperator.setForeground(Color.gray);
        botonInvertedGrayScaleThresholdOperator.setBounds(1000, 5, 260, 35);
        botonInvertedGrayScaleThresholdOperator.setForeground(Color.gray);
        botonStretchOperator.setBounds(10, 40, 145, 35);
        botonStretchOperator.setForeground(Color.gray);
        botonGrayLevelReductionOperator.setBounds(1000, 40, 260, 35);
        botonGrayLevelReductionOperator.setForeground(Color.gray);
        botonImageAddition.setBounds(155, 40, 145, 35);
        botonImageAddition.setForeground(Color.gray);
        botonImageSubtraction.setBounds(300, 40, 145, 35);
        botonImageSubtraction.setForeground(Color.gray);
        botonImageMultiplication.setBounds(300, 5, 145, 35);
        botonImageMultiplication.setForeground(Color.gray);
        botonImageConvolution.setBounds(10, 75, 145, 35);
        botonImageConvolution.setForeground(Color.gray);
        botonMotionBlur.setBounds(155, 75, 145, 35);
        botonMotionBlur.setForeground(Color.gray);
        botonSharpen.setBounds(300, 75, 145, 35);
        botonSharpen.setForeground(Color.gray);
        botonCrispen.setBounds(10, 110, 145, 35);
        botonCrispen.setForeground(Color.gray);
        botonEmboss.setBounds(155, 110, 145, 35);
        botonEmboss.setForeground(Color.gray);
        botonLowFilter.setBounds(300, 110, 145, 35);
        botonLowFilter.setForeground(Color.gray);
        botonHighPassFilter.setBounds(500, 75, 200, 35);
        botonHighPassFilter.setForeground(Color.gray);
        botonNoiseReductionFilterAverage.setBounds(1000, 75, 260, 35);
        botonNoiseReductionFilterAverage.setForeground(Color.gray);
        botonSobel.setBounds(10, 145, 145, 35);
        botonSobel.setForeground(Color.gray);
        botonPrewitt.setBounds(155, 145, 145, 35);
        botonPrewitt.setForeground(Color.gray);
        CargarImagen.setBounds(300, 145, 145, 35);
        CargarImagen.setForeground(Color.gray);


        botonInverseOperator.addActionListener(this);
        botonThresholdOperator.addActionListener(this);
        botonInvertedThresholdOperator.addActionListener(this);
        botonBinaryThresholdInterval.addActionListener(this);
        botonInvertedBinaryThresholdOperator.addActionListener(this);
        botonGrayScaleThresholdOperator.addActionListener(this);
        botonInvertedGrayScaleThresholdOperator.addActionListener(this);
        botonStretchOperator.addActionListener(this);
        botonGrayLevelReductionOperator.addActionListener(this);
        botonImageAddition.addActionListener(this);
        botonImageSubtraction.addActionListener(this);
        botonImageMultiplication.addActionListener(this);
        botonImageConvolution.addActionListener(this);
        botonMotionBlur.addActionListener(this);
        botonSharpen.addActionListener(this);
        botonCrispen.addActionListener(this);
        botonEmboss.addActionListener(this);
        botonLowFilter.addActionListener(this);
        botonHighPassFilter.addActionListener(this);
        botonNoiseReductionFilterAverage.addActionListener(this);
        botonSobel.addActionListener(this);
        botonPrewitt.addActionListener(this);
        CargarImagen.addActionListener(this);


        // Añado boton

        interfaz.add(botonInverseOperator);
        interfaz.add(botonThresholdOperator);
        interfaz.add(botonInvertedThresholdOperator);
        interfaz.add(botonBinaryThresholdInterval);
        interfaz.add(botonInvertedBinaryThresholdOperator);
        interfaz.add(botonGrayScaleThresholdOperator);
        interfaz.add(botonInvertedGrayScaleThresholdOperator);
        interfaz.add(botonStretchOperator);
        interfaz.add(botonGrayLevelReductionOperator);
        interfaz.add(botonImageAddition);
        interfaz.add(botonImageSubtraction);
        interfaz.add(botonImageMultiplication);
        interfaz.add(botonImageConvolution);
        interfaz.add(botonMotionBlur);
        interfaz.add(botonSharpen);
        interfaz.add(botonCrispen);
        interfaz.add(botonEmboss);
        interfaz.add(botonLowFilter);
        interfaz.add(botonHighPassFilter);
        interfaz.add(botonNoiseReductionFilterAverage);
        interfaz.add(botonSobel);
        interfaz.add(botonPrewitt);
        interfaz.add(CargarImagen);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Button button = (Button) event.getSource();
        if (button != CargarImagen && current == null) {
            System.err.println("No image to applay operations to");
            return;
        }
        if (button == botonInverseOperator) {
            current = current.inverseOperator();
        } else if (button == botonThresholdOperator) {
            int p = -1;
            while (p < 0 && p > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P Value) { ");
                    p = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.tresholdOperator(p);
            current.showImage();
        } else if (button == botonInvertedThresholdOperator) {
            int p = -1;
            while (p < 0 && p > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P Value) { ");
                    p = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.invertedTresholdOperator(p);
            current.showImage();
        } else if (button == botonBinaryThresholdInterval) {
            int p1 = -1;
            while (p1 < 0 && p1 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P1 Value) { ");
                    p1 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            int p2 = -1;
            while (p2 < 0 && p2 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P2 Value) { ");
                    p2 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.binaryThresholdInterval(p1, p2);
            current.showImage();
        } else if (button == botonInvertedBinaryThresholdOperator) {
            int p1 = -1;
            while (p1 < 0 && p1 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P1 Value) { ");
                    p1 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            int p2 = -1;
            while (p2 < 0 && p2 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P2 Value) { ");
                    p2 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.invertedBinaryTresholdOperator(p1, p2);
            current.showImage();
        } else if (button == botonGrayScaleThresholdOperator) {
            int p1 = -1;
            while (p1 < 0 && p1 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P1 Value) { ");
                    p1 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            int p2 = -1;
            while (p2 < 0 && p2 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P2 Value) { ");
                    p2 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.grayscaleTresholdOperator(p1, p2);
            current.showImage();
        } else if (button == botonInvertedGrayScaleThresholdOperator) {
            int p1 = -1;
            while (p1 < 0 && p1 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P1 Value) { ");
                    p1 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            int p2 = -1;
            while (p2 < 0 && p2 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P2 Value) { ");
                    p2 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.inverseGrayscaleTresholdOperator(p1, p2);
            current.showImage();
        } else if (button == botonStretchOperator) {
            int p1 = -1;
            while (p1 < 0 && p1 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P1 Value) { ");
                    p1 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            int p2 = -1;
            while (p2 < 0 && p2 > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("P2 Value) { ");
                    p2 = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.stretchOperator(p1, p2);
            current.showImage();
        } else if (button == botonGrayLevelReductionOperator) {
            int level = -1;
            while (level < 0 && level > 0xff) {
                try {
                    String test = JOptionPane.showInputDialog("Level Value) { ");
                    level = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.grayLevelReductionOperatorByLevel(level);
            current.showImage();
        } else if (button == botonImageAddition) {
            current = current.imageAddition(openImageFile());
            current.showImage();
        } else if (button == botonImageSubtraction) {
            current = current.imageSubtraction(openImageFile());
            current.showImage();
        } else if (button == botonImageMultiplication) {
            current = current.imageMultiplication(openImageFile());
            current.showImage();
        } else if (button == botonMotionBlur) {
            current = current.imageConvolution(MaskType.motionBlur());
            current.showImage();
        } else if (button == botonSharpen) {
            current = current
                .imageConvolution(MaskType.sharpen(5))
                .imageAddition(current);
            current.showImage();
        } else if (button == botonCrispen) {
            current = current.imageSubtraction(
                    current.imageConvolution(MaskType.spatialWeightedAveraging(5))
                )
                .imageAddition(current);
                current.showImage();
        } else if (button == botonEmboss) {
            current = current
                .grayscaleTransformation()
                .imageConvolution(MaskType.motionBlur());
            current.showImage();
        } else if (button == botonLowFilter) {
            current = current.imageConvolution(MaskType.spatialWeightedAveraging(5));
            current.showImage();
        } else if (button == botonHighPassFilter) {
            current = current.imageSubtraction(
                current.imageConvolution(MaskType.spatialWeightedAveraging(5))
            );
            current.showImage();
        } else if (button == botonNoiseReductionFilterAverage) {
            int mask = -1;
            while (mask < 2 && mask > 0xf) {
                try {
                    String test = JOptionPane.showInputDialog("Mask Size) { ");
                    mask = Integer.parseInt(test);
                } catch(Exception e) {}
            }
            current = current.imageConvolution(MaskType.spatialAveraging(mask));
            current.showImage();
        } else if (button == botonSobel) {
            current = current.imageConvolution(MaskType.sobel(SobelType.horizontal));
            current.showImage();
        } else if (button == botonPrewitt) {
            current = current.imageConvolution(MaskType.prewit(PrewitType.horizontal));
            current.showImage();
        } else if (button == CargarImagen) {
                original = openImageFile();
                current = original.copy();
                original.showImage();
        } else {
            current = original;
        }
    }

    private ImageProcessing openImageFile() {
        File file = null;
        while (file == null) {
            FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
            fd.setDirectory("C:\\");
            fd.setFile("*.jpg");
            fd.setVisible(true);
            if(fd.getFiles().length > 0)
                file = fd.getFiles()[0];
        }
        return new ImageProcessing(file);
    }

    public static void main(String[] args){

        Interfaz interfaz = new Interfaz();
    }
}