package view;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainPanel extends MyFrame {
    private JButton buttonProduct;
    private JButton buttonOrder;
    private JButton buttonCustomer;
    private JLabel labelTitle;
    private JPanel panelMain;
    private CustomerPanel customerPanel;
    private ProductPanel productPanel;
    private OrderPanel orderPanel;

    public MainPanel() {

        buttonCustomer.addActionListener(e -> {
            if (e.getSource() != buttonCustomer) {
                return;
            }

            customerPanel = new CustomerPanel();
            customerPanel.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    buttonCustomer.setEnabled(true);
                }
            });

            buttonCustomer.setEnabled(false);
        });

        buttonProduct.addActionListener(e -> {
            if (e.getSource() != buttonProduct) {
                return;
            }

            productPanel = new ProductPanel();
            productPanel.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    buttonProduct.setEnabled(true);
                }
            });

            buttonProduct.setEnabled(false);
        });

        buttonOrder.addActionListener(e -> {
            if (e.getSource() != buttonOrder) {
                return;
            }

            orderPanel = new OrderPanel();
            orderPanel.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    buttonOrder.setEnabled(true);
                }
            });

            buttonOrder.setEnabled(false);
        });


        this.setContentPane(panelMain);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MainPanel();
    }
}

