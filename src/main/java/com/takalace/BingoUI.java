package com.takalace;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class BingoUI extends JFrame {
    private JLabel lastNumberLabel;
    private JButton generateNumberButton, restartButton, checkWinnerButton, printCardsButton, generateCardButton;
    private JPanel gridPanel;

    private BingoNumberManager numberManager;
    private List<BingoCard> bingoCards;
    private int cardCount = 0;

    public BingoUI() {
        numberManager = new BingoNumberManager();
        bingoCards = new ArrayList<>();

        setTitle("¡Bingo!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Fondo blanco
        getContentPane().setBackground(Color.WHITE);

        // Panel para números llamados
        JPanel numbersPanel = new JPanel(new BorderLayout());
        numbersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lastNumberLabel = new JLabel("Último número: -", SwingConstants.CENTER);
        lastNumberLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 50));
        lastNumberLabel.setForeground(Color.BLACK);
        numbersPanel.add(lastNumberLabel, BorderLayout.CENTER);

        // Panel de control del juego
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        generateNumberButton = createStyledButton("Generar número");
        restartButton = createStyledButton("Reiniciar juego");
        checkWinnerButton = createStyledButton("Verificar ganador");
        generateCardButton = createStyledButton("Generar cartón");
        printCardsButton = createStyledButton("Imprimir cartones");

        controlPanel.add(generateNumberButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(restartButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(checkWinnerButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(generateCardButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(printCardsButton);

        // Panel para la grid de números
        gridPanel = new JPanel(new GridLayout(5, 15));
        gridPanel.setBackground(Color.WHITE);
        for (int i = 1; i <= 75; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
            label.setOpaque(true);
            label.setBackground(new Color(245, 245, 245));
            gridPanel.add(label);
        }

        // Agregar paneles al frame
        add(numbersPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.WEST);
        add(gridPanel, BorderLayout.CENTER);

        // Lógica de botones
        generateNumberButton.addActionListener(e -> {
            int nextNumber = numberManager.generateNextNumber();
            if (nextNumber == -1) {
                JOptionPane.showMessageDialog(this, "¡Ya no quedan números por llamar!");
            } else {
                updateLastNumber(nextNumber);
                updateGridPanel();

                for (BingoCard card : bingoCards) {
                    if (card.markNumberAndCheck(nextNumber)) {
                        System.out.println("¡Bingo encontrado en el cartón " + card.getId() + "!");
                    }
                }
            }
        });

        restartButton.addActionListener(e -> {
            numberManager.reset();
            bingoCards.clear();
            cardCount = 0;
            updateLastNumber(-1);
            updateGridPanel();
            JOptionPane.showMessageDialog(this, "¡El juego ha sido reiniciado!");
        });

        generateCardButton.addActionListener(e -> {
            BingoCard card = new BingoCard("Cartón-" + (++cardCount));
            bingoCards.add(card);
            JOptionPane.showMessageDialog(this, "¡Cartón generado!\n" + card);
        });

        checkWinnerButton.addActionListener(e -> {
            for (BingoCard card : bingoCards) {
                if (card.checkBingo()) {
                    JOptionPane.showMessageDialog(this, "¡Bingo encontrado!\n" + card);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Ningún cartón tiene Bingo aún.");
        });

        printCardsButton.addActionListener(e -> {
            for (BingoCard card : bingoCards) {
                try {
                    generatePDF(card);
                } catch (FileNotFoundException | DocumentException ex) {
                    JOptionPane.showMessageDialog(this, "Error al generar PDF para " + card.getId());
                }
            }
            JOptionPane.showMessageDialog(this, "¡Cartones impresos guardados como PDF!");
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.BLACK);
        button.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        return button;
    }

    // Métodos para actualizar la interfaz
    public void updateLastNumber(int number) {
        lastNumberLabel.setText("Último número: " + number);
    }

    public void updateGridPanel() {
        for (Component comp : gridPanel.getComponents()) {
            JLabel label = (JLabel) comp;
            int number = Integer.parseInt(label.getText());
            if (numberManager.getCalledNumbers().contains(number)) {
                label.setOpaque(true);
                label.setBackground(new Color(144, 238, 144)); // Verde claro
                label.setForeground(Color.BLACK);
            } else {
                label.setOpaque(true);
                label.setBackground(new Color(245, 245, 245)); // Gris claro
                label.setForeground(Color.BLACK);
            }
            // Ajustar la fuente del número para que sea lo más grande posible
            int height = label.getHeight();
            label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, height / 2));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void generatePDF(BingoCard card) throws FileNotFoundException, DocumentException {
        String dest = card.getId() + ".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        com.itextpdf.text.Font idFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 24, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font numberFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.NORMAL);

        Paragraph idParagraph = new Paragraph("Cartón de Bingo - " + card.getId(), idFont);
        idParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(idParagraph);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        for (int[] row : card.getGrid()) {
            for (int number : row) {
                PdfPCell cell = new PdfPCell(new Paragraph(String.valueOf(number == 0 ? "Libre" : number), numberFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(10);
                table.addCell(cell);
            }
        }
        document.add(table);
        document.close();
    }
}
